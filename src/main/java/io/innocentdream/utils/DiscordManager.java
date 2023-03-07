package io.innocentdream.utils;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.LogLevel;
import de.jcm.discordgamesdk.activity.Activity;
import io.innocentdream.InnocentDream;
import io.innocentdream.crash.CrashReport;
import io.innocentdream.crash.CrashReportPopulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;

public class DiscordManager implements Runnable, CrashReportPopulator {

    private final Core core;
    public Activity activity;
    private Thread thread;

    public DiscordManager() {
        if (!NetworkManager.online) {
            core = null;
            activity = null;
            return;
        }
        if (!isProgramRunning()) {
            core = null;
            activity = null;
            return;
        }
        File discordLibrary = LibraryManager.discord();
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

    private boolean isProgramRunning() {
        if (LibraryManager.os != LibraryManager.OS.WINDOWS) {
            return true;
        }
        String line;
        String pidInfo = "";

        try {
            Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\tasklist.exe");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                pidInfo += line;
            }

            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return pidInfo.contains("Discord");
    }

    @Override
    public void run() {
        InnocentDream.logger.info("Discord Activity Manager Initialized");
        while (true) {
            core.activityManager().updateActivity(activity);
            core.runCallbacks();
        }
    }

    @Override
    public void populateCrashReport(CrashReport report) {

    }
}
