using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;

public class Terminal  {
    public string sym;
    public Regex rex;
    public Terminal(string sym, Regex rex){
        this.sym=sym;
        this.rex = rex;
    }
    public override string ToString(){
        return String.Format("[{0,-20} | {1,-20}]", this.sym, this.rex);
    }
}

