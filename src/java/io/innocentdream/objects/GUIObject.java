package io.innocentdream.objects;

import io.innocentdream.InnocentDream;
import io.innocentdream.rendering.Model;
import io.innocentdream.utils.Identifier;
import io.innocentdream.utils.Utils;

public class GUIObject extends TexturedObject {
    private Model m = null;
    private boolean loaded;

    public GUIObject(int x, int y, int w, int h, Identifier texture) {
        super(x, y, w, h, texture);
        this.loaded = false;
    }

    @Override
    public void draw() {
        float[] closeCorner = Utils.convertPixelToScreenCoordinates(new float[] { aABB.x, aABB.y });
        float[] farCorner = Utils.convertPixelToScreenCoordinates(new float[] { aABB.x + aABB.w, aABB.y + aABB.h });
        if (!loaded) {
            this.m = InnocentDream.display.loader.loadToVAO(new float[]{
                    closeCorner[0], closeCorner[1],
                    closeCorner[0], farCorner[1],
                    farCorner[0], farCorner[1],
                    farCorner[0], closeCorner[1]
            });
            this.loaded = true;
        }
        InnocentDream.display.renderer.renderGuiObject(m, prepareTexture());
    }

    @Override
    public void update() {}

}
