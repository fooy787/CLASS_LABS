using System.Collections.Generic;
using System;

public static class Compiler{
    public static Dictionary<string, Dictionary<string,Production>> computeLLTable(string gfile){
        var g = new Grammar(gfile,false);
        LL1Parser p = new LL1Parser(g);
        return p.table;
    }

    public static Grammar.TreeNode parse(string gfile, string ifile)
    {
        var g = new Grammar(gfile, ifile, false);
        Grammar.TreeNode parseTree = g.tree;
        return parseTree;
    }
}
