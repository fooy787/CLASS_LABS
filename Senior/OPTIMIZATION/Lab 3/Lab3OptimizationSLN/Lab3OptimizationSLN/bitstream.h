#pragma once

#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <iomanip>
#include <algorithm>
#include <sstream>
#include "Codeword.h"
#include <cassert>

class obitstream{
    std::ofstream o;
    std::vector<int> buffered;  //each entry is 1 or 0
    obitstream(const obitstream&) = delete;
    bool closed=false;
    
    //flush as many bits as we can from the buffered ones.
    //if all is true, pad to a byte boundary and flush
    //all of them and then write a byte telling how many bits
    //in the final byte were significant
    void bitflush(bool all){
        if( closed )
            throw std::runtime_error("Already closed");
        
        char leftover=0;
		size_t bufferedSize = buffered.size();
        if( all ){
            leftover=bufferedSize%8;
            if(leftover == 0 )
                leftover = 8;
            int toPush = (8-leftover)&7;
            buffered.resize(bufferedSize+toPush);
        }
        
        int wrote=0;
        for(size_t i=0;(i+8)<=bufferedSize;i+=8){
            unsigned char x=0;
            for(int j=0;j<8;++j){
                x <<= 1;
				int k = i + j;
                assert(buffered[k] == 0 || buffered[k]==1 );
                x |= buffered[k];
            }
            o.write((char*)&x,1);
            wrote++;
        }
        buffered.erase(buffered.begin(),buffered.begin()+wrote*8);
        if( all ){
            o.write( &leftover, 1 );
            closed=true;
        }
    }
  public:
    obitstream(std::string fname) : o(fname,std::ios::binary) {
    }
    ~obitstream(){
        if(!closed)
            this->close();
    }
    
    size_t tellp(){
        return o.tellp();
    }
    
    void close(){
        if(!closed){
            bitflush(true);
            o.close();
        }
    }
    obitstream& operator<<(std::string s){
		size_t temp = s.length();
        for(size_t i=0;i<temp;++i)
            *this << s[i];
        return *this;
    }
    obitstream& operator<<(const Codeword& c){
        buffered.insert(buffered.end(), c.bits.begin(), c.bits.end() );
        bitflush(false);
        return *this;
    }
    obitstream& operator<<(int c){
        std::ostringstream oss;
        oss << c;
        std::string x = oss.str();
        for(char cc : x )
            *this << cc;
        return *this;
    }
    obitstream& operator<<(const char c){
        for(int i=0;i<8;++i){
            int v = ( (c & (0x80>>i)) ? 1:0 );
            buffered.emplace_back( v );
        }
        bitflush(false);
        return *this;
    }
    obitstream& operator<<(const char* c){
        while(*c){
            char ss = *c;
            (*this) << ss;
            c++;
        }
        return *this;
    }
};

class ibitstream{
    std::ifstream in;
    
    unsigned char curr,next;
    bool nextIsTheCounter;
    int bitsInCurr;
    bool failbit=false;
    ibitstream(const ibitstream&) = delete;
    bool ascii=false;
    
  public:
  
    char getByte(){
        char c=0;
        for(int i=0;i<8;++i){
            c <<= 1;
            c |= getBit();
        }
        return c;
    }
    
    int getBit(){
        
        if( bitsInCurr == 0 ){
            if( nextIsTheCounter ){
                //nothing to do; leave count at zero
            } else {
                curr=next;
                in.read( (char*)&next,1 );
                if( in.peek() == EOF ){
                    nextIsTheCounter = true;
                    bitsInCurr = next;
                } else {
                    bitsInCurr = 8;
                }
            }
        }
        if( bitsInCurr == 0 ){
            failbit=true;
            return EOF;
        }
        int b = ((curr & 0x80) ? 1:0);
        curr <<= 1;
        bitsInCurr--;
        return b;
    }
        
        
    bool fail(){
        return failbit;
    }
    
    bool good(){
        return in.good() && !failbit;
    }
    
    ibitstream(std::string s) : in(s,std::ios::binary){
        in.read((char*)&curr,1);
        if( in.fail() )
            throw std::runtime_error("Bad file");
        bitsInCurr=8;
        in.read((char*)&next,1);
        if( in.fail() )
            throw std::runtime_error("Bad file");
        if( in.peek() == EOF ){
            nextIsTheCounter = true;
            bitsInCurr = next;
        }
    }
    
};

void getline(ibitstream& in, std::string& s){
    while(1){
        char ch = in.getByte();
        if( in.fail() || ch == '\n' ){
            return;
        }
        s += ch;
    }
}
