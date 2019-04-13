#pragma once

#include <boost/asio.hpp>
#include <iostream>
#include <string>
#include <thread>
#include <fstream>
#include <mutex>
#include <tuple>
#include <list>
#include <condition_variable>
#include <regex>
#include <set>
#include <stdio.h>

class RobotChecker {
public:

	/** Return true if the given host's robots.txt file
	permits access to path. Thread safe.
	*/
	static bool ok(
		boost::asio::io_service& svc,
		boost::asio::ip::tcp::resolver& res,
		std::string host,
		std::string port,
		std::string path) {

		using namespace std;

		//somewhat complex locking control flow, 
		//but we don't want to hold the lock
		//while we're fetching the network resource
		L.lock();
		if (robots.find(host) == robots.end()) {
			L.unlock();
			int status;
			string data;
			wget(svc, res, host, port, "/robots.txt", status, data);

			if (status == 404) {
#if VERBOSE
				std::cout << "When getting robots.txt: 404\n";
#endif
				data = "User-agent: *\nDisallow:\n";
			}
			else if (status != 200) {
#if VERBOSE
				std::cout << "When getting robots.txt: " << status << "\n";
#endif
				data = "User-agent: *\nDisallow: /\n";
			}

			vector<std::regex> forbidden = parseRobots(data);

			L.lock();
			robots[host] = forbidden;
		}
		bool ok = isAllowed(robots, host, path);
		L.unlock();
		return ok;
	}

	//reentrant
	static void wget(boost::asio::io_service& svc, boost::asio::ip::tcp::resolver& res,
		std::string host, std::string port, std::string path, int& status, std::string& data,
		int recursiveDepth = 0)
	{
		using namespace std;


#ifdef VERBOSE
		cout << "wget host " << host << " port " << port << " path " << path << "\n";
#endif

		if (recursiveDepth > 8) {
			//prevent infinite recursion
			status = 602;
			return;
		}

		boost::asio::ip::tcp::socket sock(svc);
		try {
			auto addr = res.resolve(boost::asio::ip::tcp::resolver::query(host, port));
			boost::asio::connect(sock, addr);
		}
		catch (boost::system::system_error err) {
			status = 601;
			return;
		}

		{
			ostringstream oss;
			oss << "GET " << path << " HTTP/1.0\r\n";
			oss << "Host: " << host << "\r\n";
			oss << "User-agent: Agent3702\r\n";
			oss << "\r\n";
			boost::asio::write(sock, boost::asio::buffer(oss.str()));
		}

		ostringstream oss;
		vector<char> datav;
		datav.resize(4096);
		while (true) {
			try {
				size_t len = sock.read_some(boost::asio::buffer(datav));
				oss.write(datav.data(), len);
				//failsafe: Restrict to 4MB
				if (oss.tellp() > 4 * 1024 * 1024)
					break;
			}
			catch (boost::system::system_error err) {
				break;
			}
		}
		data = oss.str();

#if VERYVERBOSE
		std::cout << "DATA: " << data << "\n";
#endif

		istringstream iss(data);
		string junk;
		iss >> junk >> status;

		if (status == 301 || status == 302 || status == 303 || status == 307) {
#ifdef VERBOSE
			std::cout << "Redirected\n";
#endif
			while (true) {
				string line;
				getline(iss, line);
#ifdef VERBOSE
				std::cout << "->" << line << "\n";
#endif
				if (iss.fail()) {
#ifdef VERBOSE
					std::cout << "failed to find Location: header\n";
#endif
					break;  //did not find new location
				}
				if (line.find("Location:") == 0) {
					istringstream iss2(line);
					string newurl;
					iss2 >> newurl; //the Location keyword
					iss2 >> std::ws;    //skip whitespace
					getline(iss2, newurl);   //the real data we want
#ifdef VERBOSE
					std::cout << "newurl=" << newurl << "\n";
#endif
					std::regex urlrex("((https?)://([-a-z0-9.]+\\.[-a-z0-9.]+(:\\d+)?))?(.*)",std::regex_constants::icase);
					smatch m;
					if (regex_search(newurl, m, urlrex)) {
						string proto = m[1];
						string host = m[2];
						string port = m[3];
						string path = m[4];
						if (proto == "http") {
							if (port == "")
								port = "80";
							wget(svc, res, host, port, path, status, data, recursiveDepth + 1);
							return;
						}
						else {
#ifdef VERBOSE
							std::cout << "Not http\n";
#endif
							status = 603;
							return;
						}
					}
				}
			}
		}
		return;
	}
private:

	static std::mutex L;
	static std::map<std::string, std::vector<std::regex> > robots;


	//caller must take the lock
	static bool isAllowed(std::map<std::string, std::vector<std::regex> >&robots, std::string host, std::string path) {
		using namespace std;
		for (auto& rex : robots[host]) {
			smatch m;
			if (regex_search(path, m, rex))
				return false;
		}
		return true;
	}

	//reentrant
	static std::vector<std::regex> parseRobots(std::string data) {
		using namespace std;
		vector<std::regex> forbidden;
		istringstream iss(data);
		string line;
		bool payingAttention = false;
		while (true) {
			getline(iss, line);
			if (iss.fail()) {
				break;
			}

#if VERBOSE
			std::cout << payingAttention << "/Line: " << line << "\n";
#endif

			if (line.find("User-agent:") == 0) {
				istringstream iss2(line);
				string junk, agent;
				iss2 >> junk >> agent;
				if (agent == "*")
					payingAttention = true;
				else
					payingAttention = false;
			}
			else if (payingAttention && line.find("Disallow:") == 0) {
				istringstream iss2(line);
				string junk, p;
				iss2 >> junk >> p;
				if (p.length() != 0) {
					ostringstream rexs;
					rexs << "^";
					for (unsigned i = 0; i<p.length(); ++i) {
						char ch = p[i];
						if (ch == '*')
							rexs << ".*";
						else if (ch == '$')
							rexs << "$";
						else
							rexs << "\\x" << hex << (int)ch;
					}
					std::regex rex(rexs.str(), std::regex_constants::icase);
#if VERBOSE
					std::cout << "Forbidding: " << p << "\n";
#endif
					forbidden.push_back(rex);
				}
			}
			else if (payingAttention && (line.find("Crawl-delay:") == 0 || line.find("Visit-time:") == 0)) {
				//meh. Treat the entire site as forbidden.
				forbidden.push_back(std::regex("^/"));
			}
		}
		return forbidden;
	}
};