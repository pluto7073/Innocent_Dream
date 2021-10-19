package io.innocentdream.rendering;

import java.util.HashMap;

public class ResourceManager {

    public static final HashMap<String, Resource> RESOURCES;

    static {
        RESOURCES = new HashMap<>();
        RESOURCES.put("icon-16.png", new Resource(Resource.Type.IMAGE, "assets/gui/icon/icon-16.png"));
        RESOURCES.put("icon-32.png", new Resource(Resource.Type.IMAGE, "assets/gui/icon/icon-32.png"));
    }

    public static void init() {}

}
