using System;
using System.Collections.Generic;
using Test;

public class LR0Parser
{
    Grammar g;
    public DFAState startState;
   

    HashSet<LR0Item> computeClosure(HashSet<LR0Item> S){
        HashSet<LR0Item> S2 = new HashSet<LR0Item>();
        S2.UnionWith(S);
        var toConsider = new List<LR0Item>();
        toConsider.AddRange(S);
        int i = 0;
        while(i < toConsider.Count){
            var item = toConsider[i++];
            if(item.Dpos != item.Rhs.Count) {
                var sym = item.Rhs[item.Dpos];
                if(g.nonterminals.ContainsKey(sym)) {
                    foreach(var production in g.nonterminals[sym]) {
                        var item2 = new LR0Item(sym, production, 0);
                        if(!S2.Contains(item2)) {
                            S2.Add(item2);
                            toConsider.Add(item2);
                        }
                    }
                }
            }
        }
        return S2;
    }

//    HashSet<LR0Item> computeClosure(HashSet<LR0Item> S){
//        HashSet<LR0Item> S2 = new HashSet<LR0Item>();
//        S2.UnionWith(S);
//        while(true) {
//            var toAdd = new HashSet<LR0Item>();
//            foreach(var item in S2) {
//                if(item.Dpos != item.Rhs.Count) {
//                    var sym = item.Rhs[item.Dpos];
//                    if(g.nonterminals.ContainsKey(sym)) {
//                        foreach(var production in g.nonterminals[sym]) {
//                            var item2 = new LR0Item(sym, production, 0);
//                            if(!S2.Contains(item2)) {
//                                toAdd.Add(item2);
//                            }
//                        }
//                    }
//                }
//            }
//            if(toAdd.Count == 0)
//                return S2;
//            S2.UnionWith(toAdd);
//        }
//    }

    private class EQ : IEqualityComparer<HashSet<LR0Item> > {
        public EQ(){}
        public bool Equals(HashSet<LR0Item> a, HashSet<LR0Item> b){
            return a.SetEquals(b);
        }
        public int GetHashCode(HashSet<LR0Item> x){
            int h=0;
            foreach(var i in x){
                h ^= i.GetHashCode();
            }
            return h;
        }
                
    }
    private void generateDFA(){
        var S = new HashSet<LR0Item>();
        var L = new List<String>();
        L.Add(g.startSymbol);
        S.Add(new LR0Item("S'", L, 0));
        startState = new DFAState(computeClosure(S));
        var todo = new LinkedList<DFAState>();
        todo.AddLast(startState);
        var seen = new Dictionary<HashSet<LR0Item>,DFAState>( new EQ() );
        seen[startState.Items] = startState;
        while(todo.Count > 0) {
            var Q = todo.First.Value;
            todo.RemoveFirst();
            var transitions = computeTransitions(Q);
            addStates(Q, transitions, seen, todo);
        }
    }

    private Dictionary<string, HashSet<LR0Item> > computeTransitions(DFAState Q){
        var transitions = new  Dictionary<string, HashSet<LR0Item> >();
        foreach(LR0Item I in Q.Items) {
            if(!I.DposAtEnd()) {
                var sym = I.Rhs[I.Dpos];
                if(!transitions.ContainsKey(sym)) {
                    transitions[sym] = new HashSet<LR0Item>();
                }
                transitions[sym].Add(new LR0Item(I.Lhs, I.Rhs, I.Dpos + 1));
            }
        }
        return transitions;
    }

    private void addStates(DFAState Q, 
        Dictionary<string, HashSet<LR0Item>> transitions, 
        Dictionary<HashSet<LR0Item>, DFAState> seen, 
        LinkedList<DFAState> todo)
    {
        foreach(var sym in transitions.Keys) {
            var I2 = computeClosure(transitions[sym]);
            if(!seen.ContainsKey(I2)) {
                var Q2 = new DFAState(I2);
                seen[I2] = Q2;
                todo.AddLast(Q2);
            }
            Q.Transitions[sym] = seen[I2];
        }
        
    }
    public LR0Parser(Grammar g)
    {
        this.g=g;
        generateDFA();
        
    }
}

