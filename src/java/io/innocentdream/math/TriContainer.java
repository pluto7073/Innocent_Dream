package io.innocentdream.math;

public class TriContainer<T, S, N> extends Container<T> {

    public S y;
    public N z;

    public TriContainer(T t, S s, N n) {
        this.x = t;
        this.y = s;
        this.z = n;
    }

    public TriContainer() {}

}
