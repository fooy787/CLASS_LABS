using System.Collections.Generic;
using JH;

public static class Compiler{
    public static Dictionary<string, HashSet<string> > computeFollow(string gfile){
        var g = new Grammar(gfile,false);
        var D = new Dictionary<string, HashSet<string> >();
        foreach( var k in g.follow.Keys ){
            D[k] = new HashSet<string>();
            foreach( var v in g.follow[k] ){
                D[k].Add(v);
            }
        }
        return D;
    }

    public static Dictionary<string, Dictionary<string, Grammar.Production>> computeLLTable(string gfile)
    {
        var g = new Grammar(gfile, false);
        return g.computeLLTable();
    }
}

        
