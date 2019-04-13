using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;

public class Grammar
{ 
    class Terminal {
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

    class Production : List<string> {
        public Production(string s){
            s = s.Trim();
            if( s == "lambda" )
                return;
            string[] tmp = s.Split( new string[]{" "}, StringSplitOptions.RemoveEmptyEntries );
            foreach( string x in tmp ){
                this.Add(x);
            }
        }
        public override string ToString(){
            return String.Join(" ", this);
        }
    }
        
    List<Terminal> terminals = new List<Terminal>();
    SortedSet<string> terminalSet = new SortedSet<string>();
    Dictionary<string, List<Production> > nonterminals = new Dictionary<string,List<Production>>();
    public SortedSet<string> nullable = new SortedSet<string>();

    public Grammar(string specfile, bool caseInsensitive)
    {
        string[] lines = System.IO.File.ReadAllLines(specfile);
        int i = 0;
        while(i < lines.Length){
            string line = lines[i++];            
            string lineT = line.Trim();
            if(lineT.Length == 0)
                break;
            string[] tmp = lineT.Split( new string[]{"->"}, StringSplitOptions.None);
            if(tmp.Length != 2) 
                throw new Exception("Bad grammar line");
            string lhs = tmp[0].Trim();
            string rhs = tmp[1].Trim();
            Regex rex;
            if( caseInsensitive )
                rex = new Regex(rhs, RegexOptions.IgnoreCase);
            else
                rex = new Regex(rhs);
            terminals.Add(new Terminal(lhs,rex));
            terminalSet.Add(lhs);
        }
        //ensure we always have a regex for whitespace
        terminals.Add(new Terminal("whitespace", new Regex(@"\s+"))); 

        while(i < lines.Length) {
            string line = lines[i++];
            string lineT = line.Trim();
            if(lineT.Length > 0) {
                string[] tmp = lineT.Split(new string[]{ "->" }, StringSplitOptions.None);
                if(tmp.Length != 2)
                    throw new Exception("Bad grammar line");
                string lhs = tmp[0].Trim();
                string rhs = tmp[1].Trim();
                string[] tmp2 = rhs.Split(new string[]{ "|" }, StringSplitOptions.None);
                if(!nonterminals.ContainsKey(lhs))
                    nonterminals.Add(lhs, new List<Production>());
                foreach(string p in tmp2) {
                    nonterminals[lhs].Add(new Production(p));
                }
            }
        }

        this.computeNullable();
    }

    private void computeNullable(){
        bool keepLooping = true;
        while(keepLooping){
            keepLooping = false;
            foreach(string N in nonterminals.Keys ){
                foreach(Production P in nonterminals[N]) {
                    bool allNullable = true;
                    foreach(string sym in P) {
                        if(!nullable.Contains(sym)) {
                            allNullable = false;
                            break;
                        }
                    }
                    if(allNullable) {
                        if(nullable.Add(N))
                            keepLooping = true;
                    }
                }
            }
        }
    }

    public Dictionary<string, HashSet<string>> returnFirsts()
    {
        Dictionary<string, HashSet<string>> firsts = new Dictionary<string, HashSet<string>>();
        bool notStabilized = true;
        while(notStabilized)
        {
            foreach(var N in nonterminals)
            {
                foreach(Production P in N.Value)
                {
                    HashSet<string> temp = new HashSet<string>();
                    foreach (string x in P)
                    {
                        if (firsts.ContainsKey(N.Key))
                        {
                            firsts[N.Key].Add(x);
                        }
                        else
                        {
                            temp.Add(x);
                        }
                        if(!nullable.Contains(x))
                        {
                            break;
                        }
                    }
                    if(!firsts.ContainsKey(N.Key)) firsts.Add(N.Key, temp); 
                }
            }
            foreach(var v in firsts)
            {
                foreach(var t in nonterminals.Keys)
                {
                    if(v.Value.Contains(t))
                    {
                        firsts[v.Key].UnionWith(firsts[t]);
                        firsts[v.Key].Remove(t);
                    }
                }
            }
            notStabilized = false;
        }
        return firsts;
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

