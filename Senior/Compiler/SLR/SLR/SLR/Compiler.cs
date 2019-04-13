using System.Collections.Generic;
using System;

public static class Compiler{
    public static DFAState makelr0dfa(string gfile){
        Grammar g = new Grammar(gfile,false);
        LR0Parser p = new LR0Parser(g);
        return p.startState;
    }

    public static DFAState compile(string gfile, string ifile)
    {
        Grammar g = new Grammar(gfile, false);
        SLR0Parser p = new SLR0Parser(g, ifile);
        return p.startState;
    }
}
