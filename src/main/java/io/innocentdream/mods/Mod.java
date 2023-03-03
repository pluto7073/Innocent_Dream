package io.innocentdream.mods;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import io.innocentdream.utils.JsonHelper;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Mod {

    private final JsonObject data;
    private final String id;
    private final File sourceFile;

    private Mod(JsonObject data, String id, File sourceFile) {
        this.data = data;
        this.id = id;
        this.sourceFile = sourceFile;
    }

    public JsonObject getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public void initialize() {
        JsonObject entrypoints = JsonHelper.getObject(data, "entrypoints");
        JsonArray mainEntries = JsonHelper.getArray(entrypoints, "main");
        URLClassLoader loader;
        try {
            loader = new URLClassLoader(
                    new URL[]{sourceFile.toURI().toURL()},
                    this.getClass().getClassLoader()
            );
        } catch (MalformedURLException e) {
            throw new ModInitializationException("Error in loading classes from mod %s: %s".formatted(id, e.getMessage()));
        }
        for (JsonElement s : mainEntries) {
            try {
                Class<? extends ModInitializer> modClass = (Class<? extends ModInitializer>) Class.forName(s.getAsString(), true, loader);
                ModInitializer initializer = modClass.getConstructor().newInstance();
                initializer.onInitialize();
            } catch (ClassNotFoundException e) {
                throw new ModInitializationException("Could not find main class \"%s\" for mod %s: %s".formatted(s.toString(), id, e.getMessage()));
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException |
                     InstantiationException e) {
                throw new ModInitializationException("Error occurred in loading class \"%s\" for mod %s: %s".formatted(s.toString(), id, e.getMessage()));
            }
        }
    }

    public static Mod loadModFromJar(File sourceFile) {
        ZipFile zip = new ZipFile(sourceFile);
        InputStream infoFileStream = null;
        try {
            boolean found = false;
            for (FileHeader f : zip.getFileHeaders()) {
                if (f.getFileName().endsWith(".idmod.json")) {
                    infoFileStream = zip.getInputStream(f);
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new ModInitializationException("No idmod.json file was found, make sure it is in the jar!");
            }
        } catch (ZipException e) {
            throw new ModInitializationException("Error in finding files in JAR: " + e.getMessage());
        } catch (IOException e) {
            throw new ModInitializationException("Error in loading mod json: " + e.getMessage());
        }
        JsonObject modObject = Streams.parse(new JsonReader(new InputStreamReader(infoFileStream))).getAsJsonObject();
        String modId = JsonHelper.getString(modObject, "id");
        Mod mod = new Mod(modObject, modId, sourceFile);
        mod.initialize();
        return mod;
    }

}
