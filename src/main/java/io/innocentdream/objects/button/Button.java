package io.innocentdream.objects.button;

import io.innocentdream.InnocentDream;
import io.innocentdream.objects.AABB;
import io.innocentdream.objects.GUIObject;
import io.innocentdream.objects.texts.Text;
import io.innocentdream.rendering.DisplayManager;
import io.innocentdream.rendering.ResourceManager;
import io.innocentdream.rendering.TextureHelper;
import io.innocentdream.utils.Identifier;
import org.lwjgl.stb.STBImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Button extends GUIObject {

    private boolean isSelected;
    private boolean isClicked;
    private final List<ButtonListener> clickListeners;
    private final Text text;

    public Button(int x, int y, int w, int h, Text text) {
        super(x, y, w, h, new Identifier("assets", "gui/button.png"));
        this.isSelected = false;
        this.isClicked = false;
        this.clickListeners = new ArrayList<>();
        this.text = text;
        this.text.setScale(12.0F * (w / 64F));
    }

    @Override
    public void draw() {
        super.draw();
        text.draw((this.aABB.x + this.aABB.w) / 2 - (this.text.getWidth() / 2F),
                (this.aABB.y + this.aABB.h) / 2 - (this.text.getHeight() / 2));
    }

    @Override
    protected int prepareTexture() {
        Identifier on = new Identifier("constructed", "button_on");
        Identifier off = new Identifier("constructed", "button_off");
        boolean loaded = TextureHelper.isTextureLoaded(on) && TextureHelper.isTextureLoaded(off);
        if (!loaded) {
            InputStream stream = ResourceManager.get(texture).get();
            try {
                BufferedImage baseButton = ImageIO.read(stream);
                BufferedImage onBtn = baseButton.getSubimage(0, 0, 64, 16);
                BufferedImage offBtn = baseButton.getSubimage(0, 16, 64, 16);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(onBtn, "PNG", out);
                InputStream onStream = new ByteArrayInputStream(out.toByteArray());
                out.close();
                out = new ByteArrayOutputStream();
                ImageIO.write(offBtn, "PNG", out);
                InputStream offStream = new ByteArrayInputStream(out.toByteArray());
                out.close();
                TextureHelper.loadTexture(onStream, on);
                TextureHelper.loadTexture(offStream, off);
            } catch (IOException e) {
                InnocentDream.logger.error("Failed to load button image", e);
                return 0;
            }
        }
        return isSelected ? TextureHelper.loadPreLoadedTexture(on) : TextureHelper.loadPreLoadedTexture(off);
    }

    public Button addOnClickListener(ButtonListener l) {
        clickListeners.add(l);
        return this;
    }

    @Override
    public void update() {
        float[] mousePos = DisplayManager.getWindowMousePosition();
        if (this.aABB.test(new AABB((int) mousePos[0], (int) mousePos[1], 1, 1))) {
            if (DisplayManager.isMouseDown()) {
                isClicked = true;
            } else {
                if (isClicked) {
                    for (ButtonListener l : clickListeners) {
                        l.onClicked(this);
                    }
                }
                isClicked = false;
            }
            isSelected = true;
        } else {
            isSelected = false;
        }
    }
}
