#pragma once

#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <cmath>
#include <iomanip>
#include <algorithm>


class ibitstream;
class obitstream;
class Codeword{
    friend class ibitstream;
    friend class obitstream;
    std::vector<int> bits;
    public:
    const std::vector<int>& bits_() const{
        return bits;
    }
    Codeword(){
    }
    Codeword operator+(int x){
        Codeword c;
        c.bits = this->bits;
        if( x == 0 || x == 1 )
            c.bits.emplace_back(x);
        else
            throw std::runtime_error("Bad bit");
        return c;
    }
};

inline
std::ostream& operator<<(std::ostream& o, const Codeword& cw){
    o << "{Codeword:";
	size_t mSize = cw.bits_().size();
    for(size_t i=0; i<mSize;++i){
        o << cw.bits_()[i];
    }
    o << "}";
    return o;
}
