package io.innocentdream.objects.texts;

import io.innocentdream.InnocentDream;
import io.innocentdream.rendering.DisplayManager;
import io.innocentdream.rendering.ResourceManager;
import io.innocentdream.rendering.TextureHelper;
import io.innocentdream.utils.Identifier;
import io.innocentdream.utils.Task;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CharacterManager implements Task {

    private static final Map<Integer, Identifier> CHARACTERS = new HashMap<>();

    public static void loadCharacters() {
        InputStream asciiStream = ResourceManager.get(new Identifier("assets:font/ascii.png")).get();
        BufferedImage asciiMap;
        try {
            asciiMap = ImageIO.read(asciiStream);
        } catch (IOException e) {
            ResourceManager.LOGGER.fatal("Could not load characters", e);
            return;
        }
        int c = 0;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                BufferedImage character = asciiMap.getSubimage(j * 16, i * 16, 16, 16);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    ImageIO.write(character, "PNG", out);
                } catch (IOException e) {
                    ResourceManager.LOGGER.fatal("Failed to load character: " + (char) c, e);
                    return;
                }
                InputStream in = new ByteArrayInputStream(out.toByteArray());
                Identifier charId = new Identifier("assets:character_ascii_" + c);
                TextureHelper.loadTexture(in, charId);
                CHARACTERS.put(c, charId);
                c++;
            }
        }
    }

    public static int getTexIDForChar(char c) {
        if (CHARACTERS.containsKey((int) c)) {
            return TextureHelper.loadPreLoadedTexture(CHARACTERS.get((int) c));
        } else {
            throw new IllegalArgumentException("Unrecognized character: " + c);
        }
    }

    public static Identifier getIdentifierForChar(char c) {
        return CHARACTERS.get((int) c);
    }

    @Override
    public void preformTask() {
        loadCharacters();
    }

    @Override
    public String getTaskName() {
        return "Deconstructing Fonts";
    }
}
