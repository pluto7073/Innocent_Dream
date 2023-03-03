package io.innocentdream.utils;

public class Identifier implements Comparable<Identifier> {

    private final String namespace;
    private final String path;

    public Identifier(String id) {
        if (id.contains(":")) {
            String[] parts = id.split(":");
            if (parts.length < 2) {
                throw new IllegalStateException("%s is not a valid Identifier".formatted(id));
            } else if (parts.length > 2) {
                throw new IllegalStateException("%s has too many ':' characters".formatted(id));
            }
            namespace = parts[0];
            path = parts[1];
        } else {
            namespace = "innocent-dream";
            path = id;
        }
    }

    public String getNamespace() {
        return namespace;
    }

    public String getPath() {
        return path;
    }

    public Identifier(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    public String toString() {
        return namespace + ":" + path;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Identifier id)) {
            return false;
        } else {
            return this.namespace.equals(id.namespace) && this.path.equals(id.path);
        }
    }

    @Override
    public int hashCode() {
        return 31 * this.namespace.hashCode() + this.path.hashCode();
    }

    @Override
    public int compareTo(Identifier o) {
        int i = this.path.compareTo(o.path);
        if (i == 0) {
            i = this.namespace.compareTo(o.namespace);
        }
        return i;
    }
}
