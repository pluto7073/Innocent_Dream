package io.innocentdream.utils;

import io.innocentdream.InnocentDream;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

public class NetworkManager {

    public static boolean online = true;

    public static void init() {
        online = testConnection();
    }

    public static boolean testConnection() {
        try {
            URL url = new URL("https://www.example.com/");
            URLConnection connection = url.openConnection();
            connection.connect();
        } catch (IOException e) {
            InnocentDream.logger.warn("Could not ping servers, assuming no connection");
            return false;
        }
        return true;
    }

}
