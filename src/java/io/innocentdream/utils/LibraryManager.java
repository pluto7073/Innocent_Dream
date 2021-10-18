package io.innocentdream.utils;

import java.io.File;

public class LibraryManager {

    public static void lwjgl() {
        System.setProperty("org.lwjgl.librarypath", (new File("libs\\natives").getAbsolutePath()));
    }

    public static void discord() {

    }

}
