using System.Collections.Generic;

class Compiler {
    public static HashSet<string> computeNullables( string infile ){
        var g = new Grammar(infile,false);
        var rv = new HashSet<string>();
        foreach( var x in g.nullable ){
            rv.Add(x);
        }
        return rv;
    }

    public static Dictionary<string, HashSet<string>> computeFirsts(string filename)
    {
        
        Grammar g = new Grammar(filename, false);
        return g.returnFirsts();
    }
}
