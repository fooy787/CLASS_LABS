using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;

public class Grammar
{
    public List<Terminal> terminals = new List<Terminal>();
    public HashSet<string> terminalSet = new HashSet<string>();
    public Dictionary< string, List<Production> > nonterminals = new Dictionary<string,List<Production>>();
    public string startSymbol;

    public Grammar(string specfile, bool caseInsensitive)
    {
        string[] lines = System.IO.File.ReadAllLines(specfile);
        int i;
        for(i=0;i<lines.Length;++i){
            string lineT = lines[i].Trim();
            if(lineT.Length == 0)
                break;
            string[] tmp = lineT.Split( new string[]{"->"}, 2, StringSplitOptions.None);
            if(tmp.Length != 2) 
                throw new Exception("Bad grammar line");
            string lhs = tmp[0].Trim();
            string rhs = tmp[1].Trim();
            if( lhs.Length == 0 || rhs.Length == 0 )
                throw new Exception("Bad grammar line: " + lineT);
            if( terminalSet.Contains(lhs) )
                throw new Exception("Duplicate lhs: "+lhs);
            terminalSet.Add(lhs);
            try{
                Regex rex;
                if( caseInsensitive )
                    rex = new Regex(rhs, RegexOptions.IgnoreCase);
                else
                    rex = new Regex(rhs);
                terminals.Add(new Terminal(lhs,rex));
            } catch( Exception ){
                throw new Exception("Bad regex: "+rhs);
            }
        }
        //ensure we always have a regex for whitespace
        terminals.Add(new Terminal("whitespace", new Regex(@"\s+"))); 
        
        for( ; i<lines.Length;++i){
            string line = lines[i].Trim();
            if( line.Length == 0 )
                continue;
            string[] tmp = line.Split( new string[]{"->"}, 2, StringSplitOptions.None);
            if(tmp.Length != 2) 
                throw new Exception("Bad grammar line");
            string lhs = tmp[0].Trim();
            string rhs = tmp[1].Trim();
            if( lhs.Length == 0 || rhs.Length == 0 )
                throw new Exception("Bad grammar line: " + line);
            if( terminalSet.Contains(lhs) )
                throw new Exception("Both terminal and nonterminal: "+lhs);
            if( !nonterminals.ContainsKey( lhs ) )
                nonterminals[lhs] = new List<Production>();
            foreach(string prod in rhs.Split( new string[]{"|"}, StringSplitOptions.None)){
                nonterminals[lhs].Add( new Production(lhs,prod) );
            }
            if( startSymbol == null )
                startSymbol = lhs;
        }
    }
    
    public override string ToString(){
        List<string> L = new List<string>();
        foreach(var v in this.terminals) {
            L.Add(v.ToString());
        }
        return string.Join("\n", L);
    }
    public List<Token> tokenize(string fname){
        List<Token> L = new List<Token>();
        string txt = System.IO.File.ReadAllText(fname);
        int i = 0;
        int line = 1;
        while(i < txt.Length) {
            bool found = false;
            foreach(Terminal T in terminals) {
                Match m = T.rex.Match(txt, i);
                if(m.Success && m.Index == i) {
                    string lexeme = m.Groups[0].Value;
                    if(T.sym != "whitespace" && T.sym != "comment") {
                        L.Add(new Token(T.sym, lexeme, line));
                    }
                    line += lexeme.Length - lexeme.Replace("\n", "").Length;
                    i += lexeme.Length;
                    found = true;
                    break;
                }
            }
            if(!found) {
                throw new Exception("Syntax error on line " + line + ": Could not match token");
            }
        }
        return L;
    }
}

