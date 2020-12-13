package Core;

import Lib.Game;
import Lib.Savestate;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Main {

    static String role, level;

    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("[INFO] Waiting for Cyberpunk 2077 to start.");
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
                level = "Level " + Savestate.getLevel();
                presence.state = level;
            }else {
                role = "in Night City";
            }

            presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
            presence.details = role;
            presence.largeImageKey = "cp";
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
                            main(args);
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
