using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


public class Interpreter
{
    Dictionary<dynamic, dynamic> symTable = new Dictionary<dynamic, dynamic>();
    public int interpret(TreeNode n, dynamic Inherited = null)
    {
        var t = n.Symbol;
        var ch = n.Children;
 
        if(t == "S")
        {
            if (ch.Count > 0)
            {
                interpret(ch[0]);
                interpret(ch[2]);
            }
            else
            {
                return (Inherited);
            }
        }
        else if(t == "stmt")
        {
            interpret(ch[0]);
        }
        else if (t == "a-o-f")
        {
            interpret(ch[1], ch[0].Token.lexeme);
        }
        else if (t == "a-o-f'")
        {
            if(ch[0].Symbol == "EQ")
            {
                var val = interpret(ch[1]);
                symTable[Inherited] = val;
            }
            else
            {
                interpret(ch[1], Inherited);
            }
        }
        else if(t == "a-o-f''")
        {
            if(ch.Count == 1)
            {
                var val = interpret(ch[0]);
                if(Inherited == "write")
                {
                    Console.WriteLine(val);
                }
                else
                {
                    throw new Exception("ERROR");
                }
            }
            else
            {
                if(Inherited == "halt")
                {
                    return 0;
                }
                else
                {
                    throw new Exception("ERROR");
                }
            }
        }

        else if (t == "f")
        {
            if(ch[0].Symbol == "NUM")
            {
                return Int32.Parse(ch[0].Token.lexeme);
            }
            else if (ch[0].Symbol == "ID")
            {
                var varname = ch[0].Token.Lexeme;
                if(!symTable.ContainsKey(varname))
                {
                    throw new Exception("ERROR");
                }
                else
                {
                    return symTable[varname];
                }
            }
            else
            {
                return interpret(ch[1]);
            }
        }

        else if(t == "t")
        {
            var v = interpret(ch[0]);
            return interpret(ch[1], v);
        }

        else if (t == "t'")
        {
            if(ch.Count > 0)
            {
                var op = ch[0].Token.lexeme;
                var v2 = interpret(ch[1]);
                int v;

                if (op == "*")
                {
                    v = Inherited * v2;
                }
                else if (op == "/")
                {
                    v = Inherited / v2;
                }
                else
                {
                    throw new Exception("ICE");
                }
            }
            else
            {
                return Inherited;
            }
        }
        else if (t == "e")
        {
            var v = interpret(ch[0]);
            return interpret(ch[1], v);
        }

        else if (t == "e'")
        {
            if (ch.Count > 0)
            {
                var op = ch[0].Token.lexeme;
                var v2 = interpret(ch[1]);
                int v;

                if (op == "+")
                {
                    v = Inherited + v2;
                }
                else if (op == "/")
                {
                    v = Inherited - v2;
                }
                else
                {
                    throw new Exception("ICE");
                }
            }
            else
            {
                return Inherited;
            }
        }

        else if (t == "cond")
        {
            if(ch[0].Symbol == "IF")
            {
                interpret(ch[2]);
                interpret(ch[5]);
                return (interpret(ch[7]));
            }
        }

        else if (t == "cond'")
        {
            if(ch[0].Symbol == "ELSE")
            {
                return (interpret(ch[2]));
            }
            else
            {
                return Inherited;
            }
        }

        else if (t == "loop")
        {
            interpret(ch[1]);
            return (interpret(ch[3]));
        }
        return Inherited;
    }

    public void Write(string input)
    {
        Console.WriteLine(symTable[input].Value);
    }

    public void Halt()
    {
        System.Windows.Forms.Application.Exit();
    }
}