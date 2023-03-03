package io.innocentdream.utils;

import com.google.gson.JsonObject;
import io.innocentdream.rendering.ResourceManager;

import java.io.InputStream;

public final class LanguageManager {

    private static JsonObject LANG_DATA;

    private LanguageManager() {}

    public static String getStringTranslation(String key) {
        return JsonHelper.getString(LANG_DATA, key);
    }

    public static void load() {
        InputStream stream = ResourceManager.get(new Identifier("assets", "lang/" + GamePropertyManager.getLanguageProperty() + ".json")).get();
        LANG_DATA = JsonHelper.streamToObject(stream);
    }

}
