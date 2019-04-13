using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


    public class LR0DFA
    {
        class EQ : IEqualityComparer<HashSet<LR0Item>>
        {
            public EQ() { }
            public bool Equals(HashSet<LR0Item> a, HashSet<LR0Item> b)
            {
                return a.SetEquals(b);
            }
            public int GetHashCode(HashSet<LR0Item> x)
            {
                int h = 0;
                foreach (var i in x)
                {
                    h ^= i.GetHashCode();
                }
                return h;
            }
        }

        Grammar g;
        public LR0DFA(Grammar G)
        {
            this.g = G;
        }
        public class State
        {
            public HashSet<LR0Item> Items;
            public Dictionary<string, State> Transitions;
            public State(HashSet<LR0Item> itemSet)
            {
                Items = itemSet;
                Transitions = new Dictionary<string, State>();
            }
        }
        public class LR0Item
        {
            public readonly string Lhs;
            public readonly List<string> Rhs;
            public readonly int Dpos; //index of thing after dist. pos.
            public LR0Item(string lhs, List<string> rhs, int dpos)
            {
                this.Lhs = lhs;
                this.Rhs = rhs;
                this.Dpos = dpos;
            }
            public bool DposAtEnd()
            {
                return Dpos == Rhs.Count;
            }
            public override int GetHashCode()
            {
                int tempHash = this.Lhs.GetHashCode();
                tempHash ^= this.Dpos.GetHashCode();
                foreach (string item in this.Rhs)
                {
                    tempHash ^= item.GetHashCode();
                }
                return tempHash;
            }
            public override bool Equals(object oo)
            {
                if (oo == null)
                    return false;
                LR0Item o = oo as LR0Item;
                if (o == null)
                    return false;
                LR0Item newoo = oo as LR0Item;
                if (o.Lhs != newoo.Lhs)
                    return false;//...
                else if (o.Rhs != newoo.Rhs)
                    return false;

                return true;
            }
            public static bool operator ==(LR0Item o1, LR0Item o2)
            {
                return Object.Equals(o1, o2);
            }
            public static bool operator !=(LR0Item o1, LR0Item o2)
            {
                return !(o1 == o2);
            }

        }
        public HashSet<LR0Item> computeClosure(HashSet<LR0Item> S)
        {
            HashSet<LR0Item> S2 = S;
            List<LR0Item> toConsider = new List<LR0Item>(S);
            int i = 0;
            while (i < toConsider.Count)
            {
                var item = toConsider[i];
                i++;
                var lhs = item.Lhs;
                var rhs = item.Rhs;
                var dpos = item.Dpos;
                if (item.DposAtEnd() == false)
                {
                    var sym = rhs[dpos];
                    if (g.nonterminals.ContainsKey(sym))
                    {
                        foreach (Production p in g.nonterminals[sym])
                        {
                            var item2 = new LR0Item(sym, p, 0);
                            if (!S2.Contains(item2))
                            {
                                S2.Add(item2);
                                toConsider.Add(item2);
                            }
                        }
                    }
                }
            }
            return S2;
        }
        public Dictionary<string, HashSet<LR0Item>> computeTransitions(State Q)
        {
            var transitions = new Dictionary<string, HashSet<LR0Item>>();
            foreach(var i in Q.Items)
            {
                var lhs = i.Lhs;
                var rhs = i.Rhs;
                var dpos = i.Dpos;

                if(i.DposAtEnd() == false)
                {
                    var sym = rhs[dpos];
                    if(!transitions.ContainsKey(sym))
                    {
                        transitions[sym] = new HashSet<LR0Item>();

                    }
                    transitions[sym].Add(new LR0Item(lhs, rhs, dpos + 1));
                }
            }
            return transitions;
        }
        public void addStates(State Q, Dictionary<string, HashSet<LR0Item>> transitions, Dictionary<HashSet<LR0Item>, State> seen, Stack<State> todo)
        {
            foreach( var sym in transitions.Keys)
            {
                var i2 = computeClosure(transitions[sym]);
                if(!seen.ContainsKey(i2))
                {
                    var q2 = new State(i2);
                    seen[i2] = q2;
                    todo.Push(q2);
                }
                Q.Transitions[sym] = seen[i2];
            }
        }
        public State MakeLR0DFA()
        {
            var s = new HashSet<LR0Item>();
            List<string> temp = new List<string>();
            temp.Add("S");
            s.Add(new LR0Item("S'", temp, 0));

            State startState = new State(computeClosure(s));

            var todo = new Stack<State>();
            todo.Push(startState);
            var seen = new Dictionary<HashSet<LR0Item>, State>(new EQ());
            seen[startState.Items] = startState;

            while(todo.Count > 0)
            {
                var Q = todo.Pop();
                var transitions = computeTransitions(Q);
                addStates(Q, transitions, seen, todo);
            }
            return startState;
        }
    }
