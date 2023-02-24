package io.innocentdream.objects;

import io.innocentdream.InnocentDream;
import io.innocentdream.rendering.Model;
import io.innocentdream.rendering.TextureHelper;
import io.innocentdream.utils.Identifier;
import io.innocentdream.utils.Utils;

public class GUIObject extends TexturedObject {
    private Model m = null;

    public GUIObject(int x, int y, int w, int h, Identifier texture) {
        super(x, y, w, h, texture);
    }

    @Override
    public void draw() {
        float[] closeCorner = Utils.convertPixelToScreenCoordinates(new float[] { aABB.x, aABB.y });
        float[] farCorner = Utils.convertPixelToScreenCoordinates(new float[] { aABB.x + aABB.w, aABB.y + aABB.h });
        if (m == null) {
            m = InnocentDream.display.loader.loadToVAO(new float[]{
                    closeCorner[0], closeCorner[1],
                    closeCorner[0], farCorner[1],
                    farCorner[0], farCorner[1],
                    farCorner[0], closeCorner[1]
            });
        }
        InnocentDream.display.renderer.renderGuiObject(m, prepareTexture());
    }

    @Override
    public void update() {}

}
