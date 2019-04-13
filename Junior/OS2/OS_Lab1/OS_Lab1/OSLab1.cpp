#include "stdafx.h"
#include "mpi.h"
//#include "TGApixel.h"
#include <stdio.h>
#include <fstream>
#include <iostream>
#include <vector>
#include <ctime>
#include <set>
#include <algorithm>

struct Node 
{
	Node* L;
	Node* R;
	char c;
	int n;
	int unique;
	static int ctr;
	Node() 
	{
		L = NULL;
		R = NULL;
		c = 0;
		n = 0;
		unique = ctr++;
	}
};

struct Predicate
{
	bool operator() (const Node* a, const Node* b)
	{
		if (a->n < b->n)
			return true;
		else if (a->n > b->n) 
			return false;
		else
		{
			if (a->c != b->c)
				return a->c < b->c;
			else 
				return a->unique < b->unique;
		}
	}
};

void writeOutput(Node* n, std::ofstream& output, std::string str)
{
	if (n == nullptr) 
		return;
	if (n->c != 0)
	{
		output << str.c_str();
	}
	writeOutput(n->L, output, str + "0");
	writeOutput(n->R, output, str + "1");
}


int main(int argc, char* argv[])
{
	std::ifstream in(argv[1], std::ios::binary);
	std::vector<Node*> nodeVec;
	std::set<Node*, Predicate> Q;


	struct Predicate mPred;

	unsigned int Counts[256];
	char c;
	while (in.get(c))
	{
		Counts[c]++;
	}

	for (int i = 0; i < 256; i++)
	{
		if (Counts[i] >= 1)
		{
			Node* tmp = new Node();
			tmp->c = (char)Counts[i];
			tmp->n = Counts[i];
			nodeVec.push_back(tmp);
		}
	}

	std::sort(nodeVec.begin(), nodeVec.end(), mPred);

	while (nodeVec.size() != 1)
	{
		Node* left = *nodeVec.begin();
		nodeVec.erase(nodeVec.begin());
		Node* right = *nodeVec.begin();
		nodeVec.erase(nodeVec.begin());

		Node* parent = new Node();
		parent->L = left;
		parent->R = right;
		parent->n = left->n + right->n;
		nodeVec.push_back(parent);
	}
	std::ofstream out("output.txt");
	for (int i = 0; i < 255; i++)
	{
		
		out.write((char*)Counts[i] , 1);
		out.write("\n", 1);
	}

	writeOutput(*nodeVec.begin(), out, "");
	
	return 0;
}

int Node::ctr = 0;