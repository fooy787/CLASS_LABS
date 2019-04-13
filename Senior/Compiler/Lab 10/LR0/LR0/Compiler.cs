using System.Collections.Generic;
using System;

  
internal static class Compiler{
    internal static TreeNode parse(string gfile, string ifile){
        var g = new Grammar(gfile,false);
        LL1Parser p = new LL1Parser(g);
        var tokens = g.tokenize(ifile);
        return p.parse(tokens);
    }
    public static LR0DFA.State makelr0dfa(string gfile)
        {
            var g = new Grammar(gfile, false);
            LR0DFA lr0dfa = new LR0DFA(g);
            return lr0dfa.MakeLR0DFA();
        }
    }

public static class TestsuiteCompiler{
    public static TreeNode parse(string gfile, string ifile){
        var g = new Grammar(gfile,false);
        LL1Parser p = new LL1Parser(g);
        var tokens = g.tokenize(ifile);
        return p.parse(tokens);
    }
}

