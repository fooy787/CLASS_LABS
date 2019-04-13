using System;
using System.Collections.Generic;

[Serializable]
public class DFAState
{
    public readonly Dictionary<string,DFAState> Transitions = new Dictionary<string,DFAState>();
    public readonly HashSet<LR0Item> Items = new HashSet<LR0Item>();


    static int ctr = 0;
    public readonly int unique;
    static public List<DFAState> states;

    public DFAState(){
        Console.WriteLine("Warning: Calling parameterless constructor");
    }

    public DFAState(HashSet<LR0Item> items)
    {
        Items = items;
        unique = ctr++;
        states.Add(this);
    }
    public void addTransition(string s, DFAState n){
        if(Transitions.ContainsKey(s))
            throw new Exception("Duplicate");
        Transitions[s] = n;
    }
    public override int GetHashCode(){
        int x = 0;
        foreach(var s in Items)
            x ^= s.GetHashCode();
        return x;
    }

    public override bool Equals(object oo){
        if(oo == null)
            return false;
        DFAState o = oo as DFAState;
        if(o == null)
            return false;
        return Items.SetEquals(o.Items);
    }

    public static bool operator==(DFAState o1, DFAState o2){
        return object.Equals(o1, o2);
    }

    public static bool operator!=(DFAState o1, DFAState o2){
        return !(o1 == o2);
    }

    public List<DFAState> getStates()
    {
        return states;
    }
}
