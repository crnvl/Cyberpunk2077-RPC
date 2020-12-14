using System;
using System.Diagnostics;

namespace rpcload
{
    class Program
    {
        static void Main(string[] args)
        {
            Process myProcess = new Process();
            Process.Start("run.bat");
            /*myProcess.StartInfo.UseShellExecute = false;
            myProcess.StartInfo.FileName = "java";
            myProcess.StartInfo.Arguments = "-jar mods/richpresence/Cyberpunk2077-RPC.jar";
            myProcess.Start();*/
        }
    }
}
