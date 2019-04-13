using System.Collections.Generic;
using System;

class Compiler
{

    public static HashSet<string> computeNullables(string grammarfile)
    {
        HashSet<string> nullable = new HashSet<string>();

        Grammar mGrammar = new Grammar(grammarfile, false);

        foreach(var v in mGrammar.nonterminals)
        {
            if(!nullable.Contains(v.Key))
            {
                foreach(var P in v.Value)
                {
                    if(P.Length == 0)
                    {
                        nullable.Add(v.Key);
                        Console.WriteLine(v.Key);
                        break;
                    }
                }
            }
        }


        return nullable;

    }
    
}
