package io.innocentdream.utils;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;
import io.innocentdream.InnocentDream;

import java.io.File;
import java.time.Instant;

public class DiscordManager implements Runnable {

    private final Core core;
    public Activity activity;
    private Thread thread;

    public DiscordManager() {
        if (!InnocentDream.online) {
            core = null;
            activity = null;
            return;
        }
        File discordLibrary = new File("libs\\natives\\discord\\lib\\x86_64\\discord_game_sdk.dll");
        Core.init(discordLibrary);
        try (CreateParams params = new CreateParams()) {
            params.setClientID(837415091812171806L);
            params.setFlags(CreateParams.getDefaultFlags());
            core = new Core(params);
            activity = new Activity();
            activity.setState("Loading Game...");
            activity.timestamps().setStart(Instant.now());

            activity.assets().setLargeImage("icon-512");
            core.activityManager().updateActivity(activity);
        }
        thread = new Thread(this);
        thread.setName("DiscordActivityThread");
        thread.start();
    }

    @Override
    public void run() {
        InnocentDream.logger.info("Discord Activity Manager Initialized");
        while (true) {
            core.activityManager().updateActivity(activity);
            core.runCallbacks();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                InnocentDream.logger.error("Something went wrong when delaying thread", e);
            }
        }
    }

}
