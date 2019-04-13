using System;
using System.Collections.Generic;
using static Grammar;
using tokenize;
using System.Text.RegularExpressions;

class MainClass
{
    [System.STAThread]
    public static void Main(string[] args)
    {
        string infile;
        string tokenFile;
        infile = args[0];
        tokenFile = args[1];
        
        Grammar g = new Grammar(infile, false);
        Tokenize t = new Tokenize(g.returnList(), tokenFile);
        Console.WriteLine(g);
        Console.WriteLine(t);
        Console.ReadLine();
    }
}

class Tokenize
{
    List<Token> T = new List<Token>();
    public Tokenize(List<Terminal> tokenList, string tokenFile)
    {
        string input = System.IO.File.ReadAllText(tokenFile);
        int i = 0;
        bool stillGood = true;
        int line = 1;
        while (i < input.Length) 
        {
            foreach (Terminal t in tokenList)
            {
                Match m = t.rex.Match(input, i);
                stillGood = m.Success;
                if (stillGood && m.Index == i)
                {
                    if (t.sym != "WHITESPACE")
                    {
                        T.Add(new Token(t.sym, m.Groups[0].ToString(), line));
                    }
                    line += m.Value.Split('\n').Length - 1;
                    i = m.Index + m.Length;
                    break;

                }
            }
        }
        if (!stillGood) throw new Exception("I couldnt tokenize uwu");
    }
    public override string ToString()
    {
        List<Token> L = new List<Token>();
        foreach (var v in this.T)
        {
            L.Add(v);
        }
        return string.Join("\n", L);
    }
}