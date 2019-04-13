using System;
using System.Collections.Generic;

public class LL1Parser
{
    public readonly Dictionary<string, Dictionary<string, Production>> table = 
        new Dictionary<string,Dictionary<string, Production>>();
    Grammar g;

    public LL1Parser(Grammar g)
    {
        this.g = g;
        computeLLTable();
    }
    private void computeLLTable(){
        foreach(string N in g.nonterminals.Keys ){
            table[N] = new Dictionary<string,Production>();
            foreach(var P in g.nonterminals[N]) {
                foreach( var s in g.findFirst(P,g.follow[N]) ){
                    if( table[N].ContainsKey(s) )
                        throw new Exception("Not LL(1)");
                    table[N][s] = P;
                }
            }
        }
    }
}

