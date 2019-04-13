"use strict";

const fs = require("fs");
const {URL} = require("url");
const http = require("http");
const https = require("https");

function main(){
    //argv[0] = 'node'
    //argv[1] = name of this file
    let fname = process.argv[2];
    
    //omit the encoding to read file as a binary stream
    var rs = fs.createReadStream(fname, {encoding: "utf8"} );
	let mData = [];
    
    rs.on("error", (err) => {
        console.log(err);
        process.exit(1);
    });
	rs.on("end", () => {
		let tstr = mData.split("\n");
		
		for(let i in tstr)
		{
			tstr[i] = tstr[i].trim()

			let tempURL = new URL(tstr[i]);
			//let tempURL = tstr[i];
			//tempURL.port = 80;
			//console.log(tstr);
			let outfile = fs.createWriteStream("downloaded"+i+".txt");
			if(tempURL.protocol == "http:")
			{
				
				let req = http.request
				( 
					//host: tempURL.host,
					//port: ,
					//path: tempURL.pathname,
					//method: 'GET'
					tstr[i]
				, 
				
				(rs) => 
				{
					//rs is a ReadableStream
					rs.on("error", (err) => {
						console.log(err);
					});
					rs.on("end", () => {
						
						outfile.end();
					});
					rs.on("data", (buff) => {
						
						outfile.write(buff);
					});
				}
				);
				req.on("error", (err) => {
						console.log(err);
					});
				req.end();  //need to call this!
			}
			else
			{
				let req = https.request
				( 
					//host: tempURL.host,
					//port: ,
					//path: tempURL.pathname,
					//method: 'GET'
					tempURL
				, 
				(rs) => 
				{
					//rs is a ReadableStream
					rs.on("error", (err) => {
						console.log(err);
					});
					rs.on("end", () => {
						
						outfile.end();
					});
					rs.on("data", (buff) => {
						
						outfile.write(buff);
					});
				}
				);
				req.on("error", (err) => {
						console.log(err);
				});
				req.end();  //need to call this!
			}
			i++;
		}
	});
    rs.on("data", (str) => {
		mData += str;
    });
}
main();