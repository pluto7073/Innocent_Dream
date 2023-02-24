package io.innocentdream.objects;

import io.innocentdream.InnocentDream;
import io.innocentdream.rendering.TextureHelper;
import io.innocentdream.utils.Identifier;

import java.awt.*;

public abstract class TexturedObject extends GameObject {

    protected Identifier texture;

    public TexturedObject(int x, int y, int w, int h, Identifier texture) {
        super(x, y, w, h);
        this.texture = texture;
    }

    protected int prepareTexture() {
        return TextureHelper.loadTexture(texture);
    }

    @Override
    public void draw() {
        if (model == null) {
            model = InnocentDream.display.loader.loadToVAO(new float[]{
                    aABB.x, aABB.y, 0,
                    aABB.x, aABB.y + aABB.h, 0,
                    aABB.x + aABB.w, aABB.y + aABB.h, 0,
                    aABB.x + aABB.w, aABB.y, 0}, texCoords);
        }
        InnocentDream.display.renderer.renderTexturedModel(this.model, prepareTexture());
    }
}
