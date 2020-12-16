package Core;

import Lib.Game;
import Lib.Savestate;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.nio.file.Path;

public class Main {

    static String role, level;
    static boolean start = true;
    static boolean nightbuild = false;

    public static void main(String[] args) throws IOException, ParseException {

        Path path = Path.of(System.getProperty("user.dir"));
        String exe = String.valueOf(path.getParent().getParent());
        System.out.println("[INFO] Waiting for Cyberpunk 2077 to start.");

        if (nightbuild) {
            if(!start) {
                Process process = new ProcessBuilder(exe + "\\bin\\x64\\Cyberpunk2077.exe").start();
                start = true;
            }
        }

        boolean processFound = Game.waitForGame();

        System.out.println("[INFO] Trying to read Savefile.");
        boolean withFile = Savestate.savestateExist();

        if(processFound) {
            DiscordRPC lib = DiscordRPC.INSTANCE;
            String applicationId = "787025364677296148";
            String steamId = "1091500";
            DiscordEventHandlers handlers = new DiscordEventHandlers();
            handlers.ready = (user) -> System.out.println("[INFO] Rich Presence is ready.");
            Savestate.output = false;
            lib.Discord_Initialize(applicationId, handlers, true, steamId);
            DiscordRichPresence presence = new DiscordRichPresence();

            if(withFile) {
                role = "Playing as " + Savestate.getRole();
                switch (Savestate.getRole().toLowerCase()) {
                    case "corporate":
                        presence.smallImageKey = "corpo";
                        presence.smallImageText = role;
                        break;
                    case "streetkid" :
                        presence.smallImageKey = "streetkid";
                        presence.smallImageText = role;
                        break;
                    case "nomad":
                        presence.smallImageKey = "nomad";
                        presence.smallImageText = role;
                        break;
                }
                presence.largeImageKey = "cp";
                presence.largeImageText = "Cyberpunk2077";
                level = "Level " + Savestate.getLevel();
                presence.state = level;
            }else {
                presence.details = "in Night City";
                presence.largeImageKey = "cp";
            }

            presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
            lib.Discord_UpdatePresence(presence);
            // in a worker thread
            new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    lib.Discord_RunCallbacks();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {}

                    try {
                        if(!Game.checkAvailable()) {
                            System.out.println("[INFO] Closed Game. Disconnecting.");
                            lib.Discord_Shutdown();
                            if (!nightbuild) {
                                main(args);
                            }else {
                                System.exit(0);
                            }
                        }else {
                            if(withFile) {
                                role = "Playing as " + Savestate.getRole();
                                level = "Level " + Savestate.getLevel();
                                presence.state = level;
                                lib.Discord_UpdatePresence(presence);
                            }else {
                                role = "in Night City";
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, "RPC-Callback-Handler").start();

        }else {
            System.out.println("[WARNING] Process not found. Please start Cyberpunk 2077 and try again!");
        }
    }
}
