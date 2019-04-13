using System;
using System.Collections.Generic;

[Serializable]
public class LR0Item
{
    public readonly string Lhs;
    public readonly List<string> Rhs;
    public readonly int Dpos;

    public LR0Item(string lhs, List<string> rhs, int dpos)
    {
        this.Lhs = lhs;
        this.Rhs = rhs;
        this.Dpos = dpos;
    }

    public bool DposAtEnd(){
        return Dpos == Rhs.Count;
    }

    public override int GetHashCode(){
        int x = 0;
        foreach(var s in Rhs)
            x ^= s.GetHashCode();
        return Lhs.GetHashCode() ^ x ^ Dpos;
    }

    public override bool Equals(object oo){
        if(oo == null)
            return false;
        LR0Item o = oo as LR0Item;
        if(o == null)
            return false;
        
        if( Rhs.Count != o.Rhs.Count )
            return false;
        for(int i=0;i<Rhs.Count;++i)
            if( Rhs[i] != o.Rhs[i] )
                return false;
        if( Lhs != o.Lhs )
            return false;
        if( Dpos != o.Dpos)
            return false;
        return true;
    }

    public static bool operator ==(LR0Item o1, LR0Item o2){
        return Object.Equals(o1,o2);
    }
    public static bool operator!=(LR0Item o1, LR0Item o2){
        return !(o1 == o2);
    }

    public override string ToString ()
    {
        string s = Lhs + " -> ";
        for(int i = 0; i < Dpos; ++i)
            s += Rhs[i] + " ";
        s += "# ";
        for(int i = Dpos; i < Rhs.Count; ++i)
            s += Rhs[i] + " ";
        return "{"+s+"}";
    }
        
        
}

