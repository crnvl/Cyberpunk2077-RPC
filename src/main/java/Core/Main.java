package Core;

import Lib.Game;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        boolean processFound = Game.checkAvailable();

        if(processFound) {

            DiscordRPC lib = DiscordRPC.INSTANCE;
            String applicationId = "787025364677296148";
            String steamId = "1091500";
            DiscordEventHandlers handlers = new DiscordEventHandlers();
            handlers.ready = (user) -> System.out.println("Ready!");
            lib.Discord_Initialize(applicationId, handlers, true, steamId);
            DiscordRichPresence presence = new DiscordRichPresence();
            presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
            presence.details = "in Night City";
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
                            System.out.println("Closed Game. Disconnecting.");
                            System.exit(0);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, "RPC-Callback-Handler").start();

        }else {
            System.out.println("Process not found. Please start Cyberpunk 2077 and try again!");
        }
    }
}
