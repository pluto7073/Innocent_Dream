package io.innocentdream.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.innocentdream.InnocentDream;
import net.lingala.zip4j.ZipFile;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

public class LibraryManager {

    private static final String BASE_VERSION_INFO_LINK = "https://innocent-dream.web.app/cdn/versions/%s/%s.json";
    private static final OS os;
    private static final Logger LOGGER = LogManager.getLogger("Library Manager");

    static {
        LOGGER.setLevel(Level.ALL);
        if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("windows")) {
            os = OS.WINDOWS;
        } else if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("linux")) {
            os = OS.LINUX;
        } else if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("mac")) {
            os = OS.MAC;
        } else os = null;
    }

    public static void lwjgl() {
        if (!NetworkManager.online) {
            InnocentDream.logger.warn("No internet connection, assuming all libraries are good");
            return;
        }
        String version = InnocentDream.version.toLowerCase();
        String link = BASE_VERSION_INFO_LINK.formatted(version, version);
        File infoFile = Utils.newFileInRunDir("versions\\%s\\%s.json".formatted(version, version));
        infoFile.getParentFile().mkdirs();
        try {
            URL url = new URL(link);
            InputStream stream = url.openStream();
            Files.write(infoFile.toPath(), stream.readAllBytes());
            InputStream jsonStream = new FileInputStream(infoFile);
            JsonObject versionData = JsonHelper.streamToObject(jsonStream);
            switch (os) {
                case WINDOWS -> lwjglWindows(versionData);
                case LINUX -> lwjglLinux(versionData);
                case MAC -> lwjglMac(versionData);
            }
        } catch (IOException e) {
            LOGGER.fatal("Error in loading libraries", e);
            System.exit(e.hashCode());
        }

    }

    public static void lwjglLinux(JsonObject versionData) {
        File libDir = Utils.newFileInRunDir("libs\\natives");
        libDir.mkdirs();
        System.setProperty("org.lwjgl.librarypath", libDir.getAbsolutePath());
        JsonObject librariesData = JsonHelper.getObject(versionData, "libraries");
        JsonObject nativesData = JsonHelper.getObject(librariesData, "natives");
        JsonArray linuxNatives = JsonHelper.getArray(nativesData, "linux");
        verifyLibs(linuxNatives, libDir);
    }

    public static void lwjglMac(JsonObject versionData) {
        File libDir = Utils.newFileInRunDir("libs\\natives");
        libDir.mkdirs();
        System.setProperty("org.lwjgl.librarypath", libDir.getAbsolutePath());
        JsonObject librariesData = JsonHelper.getObject(versionData, "libraries");
        JsonObject nativesData = JsonHelper.getObject(librariesData, "natives");
        JsonArray macosNatives = JsonHelper.getArray(nativesData, "macos");
        verifyLibs(macosNatives, libDir);
    }

    public static void lwjglWindows(JsonObject versionData) {
        File libDir = Utils.newFileInRunDir("libs\\natives");
        libDir.mkdirs();
        System.setProperty("org.lwjgl.librarypath", libDir.getAbsolutePath());
        JsonObject librariesData = JsonHelper.getObject(versionData, "libraries");
        JsonObject nativesData = JsonHelper.getObject(librariesData, "natives");
        JsonArray windowsNatives = JsonHelper.getArray(nativesData, "windows");
        verifyLibs(windowsNatives, libDir);
    }

    private static void verifyLibs(JsonArray natives, File libDir) {
        natives.forEach(element -> {
            if (!(element instanceof JsonObject nativeData)) {
                return;
            }
            String downloadLink = JsonHelper.getString(nativeData, "download");
            String name = JsonHelper.getString(nativeData, "name");
            long size = JsonHelper.getLong(nativeData, "size");
            File target = new File(libDir, name);
            if (!target.exists() || target.length() != size) {
                downloadLib(downloadLink, target);
            }
        });
    }

    private static void downloadLib(String downloadLink, File target) {
        try {
            InputStream is = new URL(downloadLink).openStream();
            Files.copy(is, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info("Downloaded %s to %s".formatted(target.getName(), target.getAbsolutePath()));
        } catch (IOException e) {
            LOGGER.error("Failed to download %s from %s".formatted(target.getName(), downloadLink), e);
        }
    }

    public static File discord() {
        File discordFolder = Utils.newFileInRunDir("libs\\natives\\discord");
        if (!discordFolder.exists()) {
            try {
                InputStream is = new URL("https://innocent-dream.web.app/cdn/libs/natives/discord.zip").openStream();
                File dl = Files.createTempFile("discord", ".zip").toFile();
                Files.copy(is, dl.toPath(), StandardCopyOption.REPLACE_EXISTING);
                ZipFile file = new ZipFile(dl);
                file.extractAll(discordFolder.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String base = "libs\\natives\\discord\\lib\\x86_64\\discord_game_sdk";
        return switch (os) {
            case WINDOWS -> Utils.newFileInRunDir(base + ".dll");
            case LINUX -> Utils.newFileInRunDir(base + ".so");
            case MAC -> Utils.newFileInRunDir(base + ".dylib");
        };
    }

    private enum OS {
        WINDOWS,
        LINUX,
        MAC
    }

}
