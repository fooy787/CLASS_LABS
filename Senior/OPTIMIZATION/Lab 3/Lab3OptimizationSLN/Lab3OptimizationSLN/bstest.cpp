#include "bitstream.h"
#include <string>
#include <iostream>
#include <fstream>
#include <cassert>

using namespace std;

vector<char> readf(string fname){
    ifstream in(fname,ios::binary);
    in.seekg(0,ios::end);
    size_t sz = in.tellg();
    in.seekg(0);
    vector<char> V;
    V.resize(sz);
    in.read(V.data(),V.size());
    return V;
}

int main(int argc, char* argv[])
{
    {
        obitstream obs("z");
        obs << 123;
        obs << "\n";
        cout << "----\n";
        Codeword cw;
        //65 = 1000001
        cw = cw + 0 + 1 + 0 + 0 + 0 + 0 + 0 + 1 ;
        obs << cw;
        obs.close();
        
        vector<char> v = readf("z");
        assert(v[0]=='1');
        assert(v[1]=='2');
        assert(v[2]=='3');
        assert(v[3]=='\n');
        assert(v[4]=='A');
    }
    {
        obitstream obs("z");
        obs << 123;
        obs << "\n";
        cout << "----\n";
        Codeword cw;
        //65 = 1000001
        cw = cw + 0 + 1 + 0 + 0 + 0 + 0 + 1 + 1 ;
        obs << cw;
        obs.close();
        
        vector<char> v = readf("z");
        assert(v[0]=='1');
        assert(v[1]=='2');
        assert(v[2]=='3');
        assert(v[3]=='\n');
        assert(v[4]=='C');
    }
    {
        obitstream obs("z");
        obs << 123;
        obs << "\n";
        cout << "----\n";
        Codeword cw;
        //65 = 1000001
        cw = cw + 0 + 1 + 0 + 0 + 0 + 0 + 1 + 1 +    
                    1 + 0 + 1 + 1;
        obs << cw;
        obs.close();
        
        ifstream in("z",ios::binary);
        char x;
        in.read(&x,1);
        assert(x=='1');
        in.read(&x,1);
        assert(x=='2');
        in.read(&x,1);
        assert(x=='3');
        in.read(&x,1);
        assert(x=='\n');
        in.read(&x,1);
        assert(x =='C');
        in.read(&x,1);
        assert( (unsigned char)(x) == 0xb0);

        ibitstream ib("z");
        x = ib.getByte();
        assert(x=='1');
        x = ib.getByte();
        assert(x=='2');
        x = ib.getByte();
        assert(x=='3');
        x = ib.getByte();
        assert(x=='\n');
        int bits[] = {0,1,0,0,0,0,1,1,  1,0,1,1};
        for(int z=0;z<12;++z){
            x = ib.getBit();
            assert(!ib.fail());
            assert(x == bits[z]);
        }
        ib.getBit();
        assert(ib.fail());
    }
    
    return 0;
}
    
