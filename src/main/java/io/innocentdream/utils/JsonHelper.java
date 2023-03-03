package io.innocentdream.utils;

import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;

public final class JsonHelper {

    public static String getString(JsonObject object, String tag) {
        return object.get(tag).getAsString();
    }

    public static JsonObject streamToObject(InputStream stream) {
        return Streams.parse(new JsonReader(new InputStreamReader(stream))).getAsJsonObject();
    }

    public static JsonArray streamToArray(InputStream stream) {
        return Streams.parse(new JsonReader(new InputStreamReader(stream))).getAsJsonArray();
    }

    public static JsonArray getArray(JsonObject object, String tag) {
        return object.get(tag).getAsJsonArray();
    }

    public static JsonObject getObject(JsonObject object, String tag) {
        return object.get(tag).getAsJsonObject();
    }

    public static long getLong(JsonObject object, String tag) {
        return object.get(tag).getAsLong();
    }

}
