package io.innocentdream.rendering;

import io.innocentdream.utils.Identifier;
import io.innocentdream.utils.Utils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

public final class TextureHelper {

    private static final Map<Identifier, Integer> texIds = new HashMap<>();

    public static final Logger LOGGER = LogManager.getLogger("TextureManager");

    public static boolean isTextureLoaded(Identifier i) {
        return texIds.containsKey(i);
    }

    public static boolean isTextureLoaded(int i) {
        return texIds.containsValue(i);
    }

    public static int loadTexture(Identifier id) {
        if (texIds.containsKey(id)) {
            return texIds.get(id);
        }
        return loadTexture(ResourceManager.get(id).get(), id);
    }

    public static int loadPreLoadedTexture(Identifier id) {
        return texIds.get(id);
    }

    public static int loadTexture(InputStream stream, Identifier id) {
        if (texIds.containsKey(id)) {
            return texIds.get(id);
        }
        int texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);

        float[] borderColour = { 0.0f, 0.0f, 0.0f, 0.0f };
        glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColour);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        int[] width = {0}, height = {0}, nrChannels = {0};
        ByteBuffer data;
        try {
            File tempImg = Utils.createTempFile(id.getPath().replace("/", File.separator) + (!id.getPath().endsWith(".png") ? ".png" : ""));
            BufferedImage img = ImageIO.read(stream);
            ImageIO.write(img, "PNG", tempImg);
            data = stbi_load(tempImg.getPath(), width, height, nrChannels, 3);
        } catch (Exception e) {
            LOGGER.fatal("Failed to load texture " + id, e);
            return 0;
        }
        if (data != null) {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width[0], height[0], 0, GL_RGB, GL_UNSIGNED_BYTE, data);
            glGenerateMipmap(GL_TEXTURE_2D);
        }
        stbi_image_free(data);
        texIds.put(id, texID);
        return texID;
    }

    public static void cleanUp() {
        for (Identifier id : texIds.keySet()) {
            int i = texIds.get(id);
            GLFW.glfwMakeContextCurrent(DisplayManager.win);
            glDeleteTextures(i);
        }
    }

    static {
        LOGGER.setLevel(Level.ALL);
    }

}
