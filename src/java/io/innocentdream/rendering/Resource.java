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
    private final String path;
    private final String namespace;

    public Resource(String path, String namespace) {
        stream = this.getClass().getClassLoader().getResourceAsStream(path);
        this.path = path;
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }

    public InputStream get() {
        return this.getClass().getClassLoader().getResourceAsStream(path);
    }

}
