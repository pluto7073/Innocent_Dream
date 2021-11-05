package io.innocentdream.objects;

public class AABB {

    public float x, y, w, h;

    public AABB(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public boolean test(AABB object) {
        return this.x < object.x + object.w &&
                this.x + this.w > object.x &&
                this.y < object.y + object.h &&
                this.y + this.h > object.y;
    }

}
