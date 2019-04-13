using System;
using System.Windows.Forms;
using System.Collections.Generic;

class MainClass
{
    [System.STAThread]
    public static void Main(string[] args)
    {
        string infile;
        if (args.Length == 0)
        {
            OpenFileDialog dlg = new OpenFileDialog();
            dlg.Filter = "All stuff|*.*";
            dlg.ShowDialog();
            infile = dlg.FileName;
            if (infile.Trim().Length == 0)
                return;
            dlg.Dispose();
        }
        else
        {
            infile = args[0];
        }

        HashSet<string> nullable = Compiler.computeNullables(infile);
        Console.WriteLine(nullable);
        Console.ReadLine();
    }
}