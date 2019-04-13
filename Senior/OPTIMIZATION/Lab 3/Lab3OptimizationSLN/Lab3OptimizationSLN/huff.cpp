
#include <set>
#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <cmath>
#include <iomanip>
#include <algorithm>
#include "Codeword.h"
#include "bitstream.h"
#include <cassert>

using namespace std;

struct Node{
    Node* L = nullptr;
    Node* R = nullptr;
    unsigned char ch;
    int count;
    Node(char c, int n){
        ch = c;
        count=n;
    }
};

void getCodesRecursively(vector<Codeword>& codes, Node* n, Codeword pfx){
    if(n->L){
        getCodesRecursively(codes,n->L,pfx);
        getCodesRecursively(codes,n->R,pfx+1);
    } else {
        codes[n->ch] = pfx;
    }
}

Node* getTree(vector<int> counts){
    vector<Node*> Q;
	int count = 0;
    for(int ch=0;ch<256;++ch){
        if( counts[ch] > 0 ){
            Q.emplace_back( new Node(ch,counts[ch]) );
			count++;
        }
    }

    assert(count > 0 );
    
    while(count > 1 ){
        sort(Q.begin(),Q.end(), [&](Node* n1, Node* n2){
            if(n1->count == n2->count)
                return n1->ch < n2->ch;
            else
                return (n1->count < n2->count);
        });
        Node* n1 = *(Q.begin());
        Q.erase(Q.begin());
        Node* n2 = *(Q.begin());
        Q.erase(Q.begin());
        
        Node* n3 = new Node(0,n1->count + n2->count);
        n3->L = n1;
        n3->R = n2;
        Q.emplace_back(n3);
		count--;
    }
    
    Node* root = *(Q.begin());
    return root;
}
 
 
void huff(string,string);
void dehuff(string,string);

int main(int argc, char* argv[])
{
    string mode = argv[1];
    string infile = argv[2];
    string outfile = argv[3];
    
    if( mode == "huff" ){
        huff(infile,outfile);
    } else if( mode == "dehuff" ){
        dehuff(infile,outfile);
    }
    return 0;
}

void huff(string infile, string outfile){
    vector<unsigned char> fdata;
	int length;
    
    ifstream in(infile,ios::binary);
    if(!in.good() )
        throw runtime_error("Cannot open file");

	in.seekg(ios::end);
	length = in.tellg();
	in.seekg(ios::beg);
	unsigned char* buffer = new unsigned char[length];

    while(1){
        char ch;
        in.read(&ch,1);
        if( in.fail() )
            break;
        fdata.resize(fdata.size()+1);
        fdata[fdata.size()-1] = ch;
    }
    
    vector<int> counts(256);
    for(auto c : fdata ){
        counts[c]++;
    }

    Node* root = getTree(counts);
    vector<Codeword> codes(256);
    getCodesRecursively(codes,root,Codeword());
    
    obitstream out(outfile);
    
    for(size_t i=0;i<counts.size();++i){
        out  << counts[i] << "\n";
    }

    for(unsigned char c : fdata ){
        out << codes[c];
    }
    
    cout << "Wrote " << outfile << ": " << fdata.size() << " bytes -> " << 
        out.tellp() << " bytes = " << 
        100*out.tellp()/fdata.size() << 
        "% of original size\n" ;
}


void dehuff(string infile, string outfile){
    ibitstream in(infile);
    if(!in.good() )
        throw runtime_error("Cannot open file");

    vector<int> counts(256);
    for(int i=0;i<256;++i){
        string s;
        getline(in,s);
        counts[i] = atoi(s.c_str());
    }

    Node* root = getTree(counts);
   
    vector<Codeword> codes(256);
    getCodesRecursively(codes,root,Codeword());
     
    ofstream out(outfile);
    
    Node* n = root;
    while(1){
        int b = in.getBit();
        if( in.fail() )
            break;
        if( b == 0 )
            n=n->L;
        else
            n=n->R;
        if( n->L == nullptr ){
            out.write( (char*)&(n->ch), 1 );
            n = root;
        }
    }
    
    cout << "Wrote " << outfile << "\n";
    
}


