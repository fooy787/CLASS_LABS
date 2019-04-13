using System.Collections.Generic;

    
public static class Compiler{
    public static Dictionary<string, HashSet<string> > computeFirsts(string gfile){
        var g = new JH.Grammar(gfile,false);
        var D = new Dictionary<string, HashSet<string> >();
        foreach( var k in g.first.Keys ){
            D[k] = new HashSet<string>();
            foreach( var v in g.first[k] ){
                D[k].Add(v);
            }
        }
        return D;
    }

    public static Dictionary<string, HashSet<string>> computeFollow(string gfile)
    {
        var g = new JH.Grammar(gfile, false);
        var d = new Dictionary<string, HashSet<string>>();
        foreach(var k in g.follows.Keys)
        {
            d[k] = new HashSet<string>();
            foreach(var v in g.follows[k])
            {
                d[k].Add(v);
            }
        }
        return d;
    }
}
