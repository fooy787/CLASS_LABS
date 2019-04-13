using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


class SLR0Parser
{
    LR0Parser DFA;
    public DFAState startState;
    Grammar g;
    List<Grammar.Token> tokens;

    public Dictionary<int,  Dictionary<string, Tuple<string, int, string>>> table;

    public SLR0Parser(Grammar grammar, string fname)
    {
        g = grammar;
        tokens = g.tokenize(fname);
        DFA = new LR0Parser(grammar);
        table = new Dictionary<int, Dictionary<string, Tuple<string, int, string>>>();
    }

    void computeTable()
    {
        List<DFAState> list = startState.getStates();
        foreach(var s in list)
        {
            Dictionary<string, Tuple<string, int, string>> row = new Dictionary<string, Tuple<string, int, string>> ();
            foreach(string sym in s.Transitions.Keys)
            {
                if(g.terminalSet.Contains(sym))
                {
                    row[sym] = new Tuple<string, int, string>("S", startState.Transitions[sym].unique,"");
                }
                else
                {
                    row[sym] = new Tuple<string, int, string>("T", startState.Transitions[sym].unique, "");
                }
            }
            foreach(var item in s.Items)
            {
                if(item.DposAtEnd())
                {
                    foreach(var w in g.follow[item.Lhs])
                    {
                        row[w] = new Tuple<string, int, string>("R", item.Rhs.Count, item.Lhs);
                    }
                }
            }

            table.Add(0, row);
        }
    }

    void parse()
    {
        Stack<int> stateStack = new Stack<int>();
        Stack<dynamic> nodeStack = new Stack<dynamic>();
        int ti = 0;
        while(true)
        {
            var s = stateStack.First();
            
            var t = tokens[ti].sym;
            if(table[s][t] == null)
            {
                throw new Exception("ERROR");
            }
            var action = table[s][t];
            if(action.Item1 =="S")
            {
                stateStack.Push(action.Item2);
                nodeStack.Push(new TreeNode(t, tokens[ti]));
            }
            else
            {
                var numpop = action.Item2;
                var reduceTo = action.Item3;

                TreeNode n = new TreeNode(reduceTo, None);

                for(int i = 0; i < numpop; i++)
                {
                    stateStack.Pop();
                    n.Children.Add(nodeStack.Pop());
                }
                if(reduceTo == "S'")
                {
                    if(t == "$")
                    {
                        //Accept
                    }
                    else
                    {
                        //Reject
                    }
                }
                s = stateStack.First();
                stateStack.Push(table[s][reduceTo].Item2);
                nodeStack.Push(n);
            }
        }
    }
}

