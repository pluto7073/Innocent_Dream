package io.innocentdream.utils;

import io.innocentdream.InnocentDream;
import net.lingala.zip4j.ZipFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

public class LibraryManager {

    private static final Stack<String> libsToDownload;
    private static final OS os;
    private static final String[] libsWindows = {
            "assimp.dll", "bgfx.dll", "glfw.dll", "lwjgl.dll", "lwjgl_nanovg.dll",
            "lwjgl_nuklear.dll", "lwjgl_opengl.dll", "lwjgl_par.dll", "lwjgl_stb.dll",
            "OpenAL.dll"
    };
    private static final String[] libsLinux = {
            "libassimp.so", "libbgfx.so", "libglfw.so", "liblwjgl.so", "liblwjgl_nanovg.so",
            "liblwjgl_nuklear.so", "liblwjgl_opengl.so", "liblwjgl_par.so", "liblwjgl_stb.so",
            "libopenal.so"
    };
    private static final String[] libsMac = {
            "libassimp.dylib", "libbgfx.dylib", "libglfw.dylib", "liblwjgl.dylib", "liblwjgl_nanovg.dylib",
            "liblwjgl_nuklear.dylib", "liblwjgl_opengl.dylib", "liblwjgl_par.dylib", "liblwjgl_stb.dylib",
            "libopenal.dylib"
    };

    static {
        libsToDownload = new Stack<>();
        if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("windows")) {
            os = OS.WINDOWS;
            libsWindows();
        } else if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("linux")) {
            os = OS.LINUX;
            libsLinux();
        } else if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("mac")) {
            os = OS.MAC;
            libsMac();
        } else os = null;
    }

    private static void libsWindows() {
        libsToDownload.addAll(List.of(libsWindows));
    }
    private static void libsLinux() {
        libsToDownload.addAll(List.of(libsLinux));
    }
    private static void libsMac() {
        libsToDownload.addAll(List.of(libsMac));
    }

    public static void lwjgl() {
        switch (os) {
            case WINDOWS -> lwjglWindows();
            case LINUX -> lwjglLinux();
            case MAC -> lwjglMac();
        }
    }

    public static void lwjglLinux() {
        File libDir = Utils.newFile("libs\\natives");
        System.setProperty("org.lwjgl.librarypath", libDir.getAbsolutePath());
        if (!NetworkManager.online) {
            InnocentDream.logger.warn("No internet connection, assuming all libraries are good");
            return;
        }
        File[] existing = libDir.listFiles();
        assert existing != null;
        for (File f : existing) {
            libsToDownload.remove(f.getName());
        }
        for (String s : libsToDownload) {
            try {
                InputStream is = new URL("https://innocent-dream.web.app/cdn/libs/natives/linux/" + s).openStream();
                File sd = Utils.newFile("libs\\natives\\" + s);
                Files.copy(is, sd.toPath(), StandardCopyOption.REPLACE_EXISTING);
                InnocentDream.logger.info("Downloaded " + s + " to " + sd.getAbsolutePath());
            } catch (IOException e) {
                InnocentDream.logger.error("An error occurred in downloading " + s, e);
            }
        }
    }

    public static void lwjglMac() {
        File libDir = Utils.newFile("libs\\natives");
        System.setProperty("org.lwjgl.librarypath", libDir.getAbsolutePath());
        if (!NetworkManager.online) {
            InnocentDream.logger.warn("No internet connection, assuming all libraries are good");
            return;
        }
        File[] existing = libDir.listFiles();
        assert existing != null;
        for (File f : existing) {
            libsToDownload.remove(f.getName());
        }
        for (String s : libsToDownload) {
            try {
                InputStream is = new URL("https://innocent-dream.web.app/cdn/libs/natives/mac/" + s).openStream();
                File sd = Utils.newFile("libs\\natives\\" + s);
                Files.copy(is, sd.toPath(), StandardCopyOption.REPLACE_EXISTING);
                InnocentDream.logger.info("Downloaded " + s + " to " + sd.getAbsolutePath());
            } catch (IOException e) {
                InnocentDream.logger.error("An error occurred in downloading " + s, e);
            }
        }
    }

    public static void lwjglWindows() {
        File libDir = Utils.newFile("libs\\natives");
        libDir.mkdirs();
        System.setProperty("org.lwjgl.librarypath", libDir.getAbsolutePath());
        if (!NetworkManager.online) {
            InnocentDream.logger.warn("No internet connection, assuming all libraries are good");
            return;
        }
        File[] existing = libDir.listFiles();
        assert existing != null;
        for (File f : existing) {
            libsToDownload.remove(f.getName());
        }
        for (String s : libsToDownload) {
            try {
                InputStream is = new URL("https://innocent-dream.web.app/cdn/libs/natives/windows/" + s).openStream();
                File sd = Utils.newFile("libs\\natives\\" + s);
                Files.copy(is, sd.toPath(), StandardCopyOption.REPLACE_EXISTING);
                InnocentDream.logger.info("Downloaded " + s + " to " + sd.getAbsolutePath());
            } catch (IOException e) {
                InnocentDream.logger.error("An error occurred in downloading " + s, e);
            }
        }
    }

    public static File discord() {
        File discordFolder = Utils.newFile("libs\\natives\\discord");
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
            case WINDOWS -> Utils.newFile(base + ".dll");
            case LINUX -> Utils.newFile(base + ".so");
            case MAC -> Utils.newFile(base + ".dylib");
        };
    }

    private enum OS {
        WINDOWS,
        LINUX,
        MAC
    }

}
