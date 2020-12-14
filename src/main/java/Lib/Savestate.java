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

        String savestate;
        for (int k = 0; k < 3; k++) {
            for (int j = 0; j < count; j++) {
                switch (k) {
                    case 0:
                        savestate = "ManualSave";
                        break;
                    case 1:
                        savestate = "AutoSave";
                        break;
                    case 2:
                        savestate = "QuickSave";
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + k);
                }
                Path path = Path.of("C:\\Users\\" + System.getProperty("user.name") + "\\Saved Games\\CD Projekt Red\\Cyberpunk 2077\\" + savestate + "-" + j);

                if(Files.exists(path) && containsUser(String.valueOf(path))) {
                    pathFile = String.valueOf(path);
                    save = true;
                    if (output) {
                        System.out.println("[INFO] " + savestate + " Savestate loaded.");
                    }
                    return save;
                }else if(j == count) {
                    j = 0;
                }
            }
        }
        return false;
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
