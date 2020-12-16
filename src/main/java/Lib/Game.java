package Lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Game {

    public static boolean checkAvailable() throws IOException {
        String findProcess = "Cyberpunk2077.exe";
        String filenameFilter = "/nh /fi \"Imagename eq "+findProcess+"\"";
        String tasksCmd = System.getenv("windir") +"/system32/tasklist.exe "+filenameFilter;

        Process p = Runtime.getRuntime().exec(tasksCmd);
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

        ArrayList<String> procs = new ArrayList<String>();
        String line = null;
        while ((line = input.readLine()) != null)
            procs.add(line);

        input.close();

        Boolean processFound = procs.stream().filter(row -> row.indexOf(findProcess) > -1).count() > 0;
        processFound = true;
        return processFound;
    }

    public static boolean waitForGame() throws IOException {
        while (!checkAvailable()) {

        }
        return checkAvailable();
    }

}
