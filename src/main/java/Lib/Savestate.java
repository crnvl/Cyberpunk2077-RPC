package Lib;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Savestate {
    public static String pathFile;
    public static boolean output = true;
    private static String latest;

    public static String getRole() throws IOException, ParseException {
        return JSON.readLifePath();
    }

    public static String getLevel() throws IOException, ParseException {
        String[] lvl = JSON.readLevel().split("\\.");
        return String.valueOf(lvl[0]);
    }

    public static boolean savestateExist() throws IOException {
        int i = 0;
        boolean save = false;
        long count = Files.find(
                Paths.get("C:\\Users\\" + System.getProperty("user.name") + "\\Saved Games\\CD Projekt Red\\Cyberpunk 2077"),
                1,  // how deep do we want to descend
                (path, attributes) -> attributes.isDirectory()
        ).count() - 1; // '-1' because '/tmp' is also counted in

        for (int j = 0; j < count; j++) {
            Path path = Path.of("C:\\Users\\" + System.getProperty("user.name") + "\\Saved Games\\CD Projekt Red\\Cyberpunk 2077\\ManualSave-" + j);

            if(Files.exists(path) && containsUser(String.valueOf(path))) {
                pathFile = String.valueOf(path);
                save = true;
                j = 100;
                if (output) {
                    System.out.println("[INFO] Manual Savestate loaded.");
                }
            }else if(j == count + 1) {

                if (output) {
                    System.out.println("[WARNING] Could not find a Manual Savestate. Looking further.");
                }

                for (int k = 0; k < count; k++) {
                    path = Path.of("C:\\Users\\" + System.getProperty("user.name") + "\\Saved Games\\CD Projekt Red\\Cyberpunk 2077\\AutoSave-" + k);

                    if(Files.exists(path) && containsUser(String.valueOf(path))) {
                        pathFile = String.valueOf(path);
                        save = true;
                        k = 100;
                        if (output) {
                            System.out.println("[INFO] Automatic Savestate loaded.");
                        }
                    }else if(k == count + 1) {
                        if (output) {
                            System.out.println("[WARNING] Could not find a savestate! Please keep playing and try again.");
                            System.out.println("[INFO] Standard configuration loaded.");
                        }
                    }
                }
            }
        }
        return save;
    }

    public static boolean containsUser(String pathFile) throws IOException {
        pathFile = pathFile.replace("C:\\Users\\" + System.getProperty("user.name") + "\\Saved Games\\CD Projekt Red\\Cyberpunk 2077\\", "");
        String content = Files.readString(Path.of("C:\\Users\\" + System.getProperty("user.name") + "\\Saved Games\\CD Projekt Red\\Cyberpunk 2077\\user.gls"), StandardCharsets.ISO_8859_1);
        if(content.contains(pathFile)) {
            if (!pathFile.equals(latest)) {
                System.out.println("[INFO] Loaded Savestate " + pathFile);
            }
            latest = pathFile;
            return true;
        }else {
            return false;
        }
    }

}
