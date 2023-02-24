package io.innocentdream.objects;

import io.innocentdream.InnocentDream;
import io.innocentdream.rendering.Model;
import io.innocentdream.rendering.TextureHelper;
import io.innocentdream.utils.Identifier;
import io.innocentdream.utils.Utils;

public class GUIObject extends TexturedObject {
    public GUIObject(int x, int y, int w, int h, Identifier texture) {
        super(x, y, w, h, texture);
    }

    @Override
    public void draw() {
        float[] closeCorner = Utils.convertPixelToScreenCoordinates(new float[] { aABB.x, aABB.y });
        float[] farCorner = Utils.convertPixelToScreenCoordinates(new float[] { aABB.x + aABB.w, aABB.y + aABB.h });
        Model model = InnocentDream.display.loader.loadToVAO(new float[] {
                closeCorner[0], closeCorner[1],
                closeCorner[0], farCorner[1],
                farCorner[0], farCorner[1],
                farCorner[0], closeCorner[1]
        });
        InnocentDream.display.renderer.renderGuiObject(model, TextureHelper.loadTexture(texture));
    }

    @Override
    public void update() {}

}
