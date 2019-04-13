using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;

public class Production : List<string> {
    public string lhs;
    //rhs is obtained from superclass info
    
    public Production(string lhs, string rhs){
        this.lhs = lhs;
        string s = rhs.Trim();
        if( s == "lambda" )
            return;
        string[] tmp = s.Split( new string[]{" "}, StringSplitOptions.RemoveEmptyEntries );
        foreach( string x in tmp ){
            this.Add(x);
        }
    }
    public int Length {
        get { return this.Count; }
    }
    public override string ToString(){
        string r;
        if( this.Count == 0 )
            r = "λ";
        else
            r = String.Join(" ", this);
        return this.lhs+" -> "+r;
    }
}
        
