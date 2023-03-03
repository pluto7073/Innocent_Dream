package io.innocentdream.math;

public class BiContainer<T, S> extends Container<T> {

    public S y;

    public BiContainer(T x, S y) {
        this.x = x;
        this.y = y;
    }

    public BiContainer() {}

}
