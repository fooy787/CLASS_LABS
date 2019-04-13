﻿using System;
using System.Windows.Forms;
using System.Collections.Generic;

class MainClass
{
    [STAThread]
    public static void Main(string[] args)
    {
        string gfile;
        if (args.Length == 0)
        {
            OpenFileDialog dlg = new OpenFileDialog();
            dlg.Filter = "All stuff|*.*";
            dlg.ShowDialog();
            gfile = dlg.FileName;
            if (gfile.Trim().Length == 0)
                return;
            dlg.Dispose();
        }
        else
        {
            gfile = args[0];
        }

        Dictionary<string, HashSet<string>> firsts = Compiler.computeFirsts(gfile);

        Console.WriteLine("First: ");
        foreach (var sym in firsts.Keys)
        {
            Console.Write(sym + " : ");
            foreach (var f in firsts[sym])
            {
                Console.Write(f + " ");
            }
            Console.WriteLine("");
        }

        Console.ReadLine();
    }
}