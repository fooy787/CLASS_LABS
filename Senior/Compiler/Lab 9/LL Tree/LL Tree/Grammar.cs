using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;

public class Grammar
{ 

    public class Terminal {
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

    public class TreeNode
    {
        public List<TreeNode> mChildren = new List<TreeNode>();
        public string sym;
        public Token tok;

        public TreeNode(string sym)
        {
            this.sym = sym;
            tok = null;
        }
        
    }

    public readonly List<Terminal> terminals = new List<Terminal>();
    private readonly SortedSet<string> terminalSet = new SortedSet<string>();
    public readonly Dictionary<string, List<Production> > nonterminals = new Dictionary<string,List<Production>>();

    public SortedSet<string> nullable = new SortedSet<string>();
    public Dictionary<string, SortedSet<string> > first = new Dictionary<string,SortedSet<string>>();
    public Dictionary<string, SortedSet<string> > follow = new Dictionary<string,SortedSet<string>>();
    public string startSymbol;
    public LL1Parser LLTable;
    public TreeNode tree;

    public Grammar(string specfile, string input, bool caseInsensitive)
    {
        string[] lines = System.IO.File.ReadAllLines(specfile);
        int i = 0;
        while (i < lines.Length)
        {
            string line = lines[i++];
            string lineT = line.Trim();
            if (lineT.Length == 0)
                break;
            string[] tmp = lineT.Split(new string[] { "->" }, StringSplitOptions.None);
            if (tmp.Length != 2)
                throw new Exception("Bad grammar line");
            string lhs = tmp[0].Trim();
            string rhs = tmp[1].Trim();
            Regex rex;
            if (caseInsensitive)
                rex = new Regex(rhs, RegexOptions.IgnoreCase);
            else
                rex = new Regex(rhs);
            terminals.Add(new Terminal(lhs, rex));
            terminalSet.Add(lhs);
        }
        //ensure we always have a regex for whitespace
        terminals.Add(new Terminal("whitespace", new Regex(@"\s+")));

        while (i < lines.Length)
        {
            string line = lines[i++];
            string lineT = line.Trim();
            if (lineT.Length > 0)
            {
                string[] tmp = lineT.Split(new string[] { "->" }, StringSplitOptions.None);
                if (tmp.Length != 2)
                    throw new Exception("Bad grammar line");
                string lhs = tmp[0].Trim();
                string rhs = tmp[1].Trim();
                string[] tmp2 = rhs.Split(new string[] { "|" }, StringSplitOptions.None);
                if (startSymbol == null)
                    startSymbol = lhs;
                if (!nonterminals.ContainsKey(lhs))
                    nonterminals.Add(lhs, new List<Production>());
                foreach (string p in tmp2)
                {
                    nonterminals[lhs].Add(new Production(p));
                }
            }
        }

        this.computeNullable();
        this.computeFirst();
        this.computeFollow();
        this.parseTree(input);
        this.LLTable = new LL1Parser(this);
    }

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
                if(startSymbol == null)
                    startSymbol = lhs;
                if(!nonterminals.ContainsKey(lhs))
                    nonterminals.Add(lhs, new List<Production>());
                foreach(string p in tmp2) {
                    nonterminals[lhs].Add(new Production(p));
                }
            }
        }

        this.computeNullable();
        this.computeFirst();
        this.computeFollow();
        this.LLTable = new LL1Parser(this);
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

    public SortedSet<String> findFirst(Production P, SortedSet<String> e){
        SortedSet<String> S = new SortedSet<String>();
        foreach(var x in P) {
            S.UnionWith(first[x]);
            if(!nullable.Contains(x))
                return S;
        }
        S.UnionWith(e);
        return S;
    }
                    
    private void computeFirst(){
        foreach(string T in terminalSet) {
            first[T] = new SortedSet<string>();
            first[T].Add(T);
        }
        
        foreach(string N in nonterminals.Keys)
            first[N] = new SortedSet<string>();
        
        bool keepLooping = true;
        while(keepLooping) {
            keepLooping = false;
            foreach(string N in nonterminals.Keys) {
                foreach(Production P in nonterminals[N]) {
                    foreach(string sym in P) {
                        var n1 = first[N].Count;
                        first[N].UnionWith(first[sym]);
                        var n2 = first[N].Count;
                        if(n1 != n2)
                            keepLooping = true;
                        if(!nullable.Contains(sym))
                            break;
                    }
                }
            }
        }
    }

    private void computeFollow(){
        foreach(string N in nonterminals.Keys)
            follow[N] = new SortedSet<string>();
        follow[startSymbol].Add("$");

        bool keepLooping = true;
        while(keepLooping) {
            keepLooping = false;
            foreach(string N in nonterminals.Keys) {
                foreach(Production P in nonterminals[N]) {
                    for(int i = 0; i < P.Count; ++i) {
                        string x = P[i];
                        if(nonterminals.ContainsKey(x)) {
                            int j;
                            for(j = i + 1; j < P.Count; ++j) {
                                var y = P[j];
                                var n1 = follow[x].Count;
                                follow[x].UnionWith(first[y]);
                                var n2 = follow[x].Count;
                                if(n1 != n2)
                                    keepLooping = true;
                                if(!nullable.Contains(y))
                                    break;
                            }
                            if(j >= P.Count) {
                                var n1 = follow[x].Count;
                                follow[x].UnionWith(follow[N]);
                                var n2 = follow[x].Count;
                            }
                        }
                    }
                }
            }
        }
    }

    private void parseTree(string input)
    {
        TreeNode root = new TreeNode(startSymbol);
        List<Token> tokenList = tokenize(input);
        tokenList.RemoveAll(T => T.sym == "whitespace");
        Stack<TreeNode> stack = new Stack<TreeNode>();
        stack.Push(root);
        while(tokenList.Count > 0)
        {
            Token nt = tokenList[0];
            string symofnt = nt.sym;
            if(stack.Count == 0)
            {
                throw new System.ArgumentNullException("the stack is empty");
            }
            string stnTop = stack.Peek().sym;
            if(terminalSet.Contains(stnTop))
            {
                if(stnTop == symofnt)
                {
                    stack.Peek().tok = nt;
                    tokenList.RemoveAt(0);
                    stack.Pop();
                }
                else
                {
                    throw new System.ArgumentOutOfRangeException("The symbol can't be converted");
                }
            }
            else
            {
                if(nt.sym != "EOF")
                {
                    TreeNode temp = stack.Pop();
                    List<string> tempString = LLTable.table[stnTop][symofnt];
                    tempString.Reverse();
                    if(!LLTable.table[stnTop].ContainsKey(symofnt))
                    {
                        throw new System.AggregateException("Symbol");
                    }
                    foreach(string sym in tempString)
                    {
                        TreeNode child = new TreeNode(sym);
                        temp.mChildren.Insert(0, child);
                        stack.Push(child);
                    }
                    tempString.Reverse();
                }
            }
            if(stack.Count == 0)
            {
                throw new System.AggregateException("End of file, when it shouldnt be");
            }
            tree = root;
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
