package Lib;

import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Savestate {
    public static String pathFile;

    public static String getRole() throws IOException, ParseException {
        return JSON.readLifePath();
    }

    public static String getLevel() throws IOException, ParseException {
        String[] lvl = JSON.readLevel().split("\\.");
        return String.valueOf(lvl[0]);
    }

    public static boolean savestateExist() {
        int i = 0;
        boolean save = false;
        for (int j = 0; j < 11; j++) {
            Path path = Path.of("C:\\Users\\" + System.getProperty("user.name") + "\\Saved Games\\CD Projekt Red\\Cyberpunk 2077\\AutoSave-" + i);
            if(Files.exists(path)) {
                pathFile = String.valueOf(path);
                save = true;
                j = 100;
            }else if(j == 10) {
                System.out.println("[WARNING] Could not find a savestate! Please keep playing and try again.");
                System.out.println("[INFO] Standard configuration loaded.");
            }
        }
        return save;
    }

}
