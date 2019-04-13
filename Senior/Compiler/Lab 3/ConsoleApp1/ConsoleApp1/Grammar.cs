using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using productions;

namespace tokenize
{
    public class Grammar
    {
        class Terminal{
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

        List<Terminal> terminals = new List<Terminal>();
        HashSet<string> terminalSet = new HashSet<string>();
        Dictionary<string, List<Production>> productionList = new Dictionary<string, List<Production>>();
        public Grammar(string specfile, bool caseInsensitive)
        {
            string[] lines = System.IO.File.ReadAllLines(specfile);
            bool mode = true;
            foreach (string line in lines)
            {
                string lineT = line.Trim();
                if (lineT.Length == 0)
                {
                    terminals.Add(new Terminal("whitespace", new Regex(@"\s+")));
                    mode = false;
                }
                if (mode == true)
                {
                    string[] tmp = lineT.Split(new string[] { "->" }, 2, StringSplitOptions.None);
                    if (tmp.Length != 2)
                        throw new Exception("Bad grammar line");
                    string lhs = tmp[0].Trim();
                    string rhs = tmp[1].Trim();
                    if (lhs.Length == 0 || rhs.Length == 0)
                        throw new Exception("Bad grammar line: " + line);
                    if (terminalSet.Contains(lhs))
                        throw new Exception("Duplicate lhs: " + lhs);
                    terminalSet.Add(lhs);
                    try
                    {
                        Regex rex;
                        if (caseInsensitive)
                            rex = new Regex(rhs, RegexOptions.IgnoreCase);
                        else
                            rex = new Regex(rhs);
                        terminals.Add(new Terminal(lhs, rex));
                    }
                    catch (Exception)
                    {
                        throw new Exception("Bad regex: " + rhs);
                    }
                }
                else
                {
                    
                    if (line.Length != 0)
                    {
                        string[] tmp = line.Split(new string[] { "->" }, 2, StringSplitOptions.None);
                        List<Production> tmpProductionList = new List<Production>();
                        string[] tmpRHS = tmp[1].Split(new string[] { "|" }, StringSplitOptions.None);

                        foreach (string stringToAdd in tmpRHS)
                        {
                            Production tmpProduction = new Production();
                            string[] tmpString = stringToAdd.Split(new string[] { " " }, StringSplitOptions.None);
                            foreach (string String in tmpString)
                            {
                                var StringT = String.Trim();
                                if (String != "lambda") tmpProduction.Add(StringT);
                            }
                            tmpProductionList.Add(tmpProduction);
                        }
                        productionList.Add(tmp[0], tmpProductionList);
                    }
                }
            }
            List<string> tmpLongest = new List<string>();
            string tmpLongestString= "";
            string longestOutput = "";
            foreach(var production in productionList)
            {
                Console.WriteLine(production.Key + " " + production.Value.Count);
                foreach(var term in production.Value)
                {
                    if (term.Count > tmpLongest.Count)
                    {
                        tmpLongest = term;
                        tmpLongestString = production.Key;
                        foreach(var termT in term)
                        {
                            longestOutput += termT;
                            longestOutput += " ";
                        }
                    }
                }
            }
            Console.WriteLine(tmpLongest.Count + " " + tmpLongestString + "->" + longestOutput);
            Console.ReadLine();
            //ensure we always have a regex for whitespace
            
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
}

