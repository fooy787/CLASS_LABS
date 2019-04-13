using System;
using System.Windows.Forms;

namespace tokenize
{
    class MainClass
    {
        [STAThread]
        public static void Main(string[] args)
        {
            string gfile;
            if(args.Length == 0) {
                OpenFileDialog dlg = new OpenFileDialog();
                dlg.Filter = "All stuff|*.*";
                dlg.ShowDialog();
                gfile = dlg.FileName;
                if(gfile.Trim().Length == 0)
                    return;
/*                dlg.ShowDialog();
                infile = dlg.FileName;
                if(infile.Trim().Length == 0)
                    return;*/
                dlg.Dispose();
            } else {
                gfile = args[0];
                //infile = args[1];
            }
            Grammar g = new Grammar(gfile, false);
            //var tokens = g.tokenize(infile);
            //foreach(var T in tokens) {
            //    Console.WriteLine(T);
            //}
        }
    }
}
