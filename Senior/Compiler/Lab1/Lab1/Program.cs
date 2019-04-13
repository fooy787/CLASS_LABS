using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace Lab1
{
    class Program
    {
        static void Main(string[] args)
        {
            if (args.Length == 0)
            {
                Console.Write("Please Input a filename.");
            }
            else
            {
                string filename = args[0];
                string[] fileLines = System.IO.File.ReadAllLines(filename);
                var map = new Dictionary<string, string>();
                foreach(string mLine in fileLines)
                {
                    if(mLine.Length >= 1)
                    { 
                    string[] sides = mLine.Split(new string[] { "->" }, 2, System.StringSplitOptions.None);
                    sides[0].Trim();
                    sides[1].Trim();

                        if (map.ContainsKey(sides[0]))
                        {
                            Console.Write("Left hand side repeated!");
                            break;
                        }
                        else
                        {
                            try
                            {
                                var rex = new Regex(sides[1]);
                                map.Add(sides[0], sides[1]);
                                //Console.Write("Added:" +sides[1]+ " to the dictionary\n");
                            }
                            catch (Exception e)
                            {
                                Console.Write(e);
                                continue;
                            }

                        }
                    }
                    else
                    {
                        break;
                    }
                }
                Console.Write("Completed!");
                Console.ReadLine();
            }
        }
    }
}
