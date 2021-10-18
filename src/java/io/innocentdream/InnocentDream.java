package io.innocentdream;

import io.innocentdream.utils.DiscordManager;
import io.innocentdream.utils.LibraryManager;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class InnocentDream {

    public static boolean online = true;
    public static final String version = "v0.0.1";

    public static final Logger logger = LogManager.getLogger("InnocentDream");
    public static DiscordManager discord;

    public static void main(String[] args) {
        BasicConfigurator.configure();
        logger.setLevel(Level.ALL);
        logger.info("Starting Innocent Dream version " + version + "...");
        online = testConnection();
        LibraryManager.lwjgl();
        discord = new DiscordManager();
    }

    private static boolean testConnection() {
        try {
            URL url = new URL("https://www.example.com/");
            URLConnection connection = url.openConnection();
            connection.connect();
        } catch (IOException e) {
            logger.warn("Could not ping servers, assuming no connection");
            return false;
        }
        return true;
    }

}
