package io.innocentdream.rendering;

import io.innocentdream.InnocentDream;

import java.io.IOException;
import java.io.InputStream;

public class Resource {

    public enum Type {
        IMAGE,
        AUDIO,
        TEXT,
        FONT
    }

    private final InputStream stream;
    private final Type type;

    public Resource(Type type, String path) {
        stream = this.getClass().getClassLoader().getResourceAsStream(path);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public InputStream get() {
        return stream;
    }

}
