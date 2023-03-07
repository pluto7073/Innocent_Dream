package io.innocentdream;

import io.innocentdream.crash.CrashReport;
import io.innocentdream.crash.CrashReportPopulator;
import io.innocentdream.mods.ModLoader;
import io.innocentdream.objects.texts.CharacterManager;
import io.innocentdream.rendering.DisplayManager;
import io.innocentdream.rendering.ResourceManager;
import io.innocentdream.rendering.TextureHelper;
import io.innocentdream.screens.LoadScreen;
import io.innocentdream.utils.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import static org.lwjgl.glfw.GLFW.glfwInit;

public class InnocentDream implements Runnable, CrashReportPopulator {

    public static final String version = "a2023.0.1";
    public static volatile boolean isRunning = false;

    public static final Logger logger = LogManager.getLogger("InnocentDream");
    public static DiscordManager discord;
    public static DisplayManager display;
    public static Timer timer;

    public long currentRenderTick;

    @Override
    public void run() {
        isRunning = true;
        display = DisplayManager.create();
        currentRenderTick = 0L;
        while (isRunning) {
            display.activeScreens.lastElement().tick();
            currentRenderTick++;
        }
        cleanUp();
        logger.info("Stopping Game...");
        System.exit(0);
    }

    @Override
    public void populateCrashReport(CrashReport report) {

    }

    public void cleanUp() {
        logger.debug("Saving Game Properties");
        GamePropertyManager.stop();
        logger.debug("Done!");
        logger.debug("Cleaning up temp files");
        Utils.cleanUp();
        logger.debug("Done!");
    }

    public static void main(String[] args) {
        logger.info("Starting Innocent Dream version " + version + "...");
        Utils.init(args);
        NetworkManager.init();
        LibraryManager.lwjgl();
        GamePropertyManager.start();
        LoadScreen.addTask(new ModLoader());
        LoadScreen.addTask(new ResourceManager());
        LoadScreen.addTask(new CharacterManager());
        discord = new DiscordManager();
        timer = new Timer();
        Thread thread = new Thread(new InnocentDream());
        thread.setName("main");
        thread.start();
    }

}
