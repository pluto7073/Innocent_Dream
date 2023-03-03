package io.innocentdream.objects.texts;

import io.innocentdream.objects.GUIObject;
import io.innocentdream.utils.LanguageManager;
import io.innocentdream.utils.Utils;

public abstract class Text {

    private float charSize;

    private Text(float scale) {
        this.charSize = scale;
    }

    public abstract char[] getCharArray();

    public Text setScale(float scale) {
        this.charSize = scale;
        return this;
    }

    public int getTotalLines() {
        return Utils.countArrayOccurrences(getCharArray(), '\n') + 1;
    }

    public float getWidth() {
        if (getTotalLines() == 1) {
            return getSingleLineWidth(getCharArray(), charSize);
        } else {
            String[] lines = new String(getCharArray()).split("\n");
            float maxLength = 0;
            for (String line : lines) {
                float lineLength = getSingleLineWidth(line.toCharArray(), charSize);
                if (lineLength > maxLength) {
                    maxLength = lineLength;
                }
            }
            return maxLength;
        }
    }

    public float getHeight() {
        return getTotalLines() * charSize;
    }

    public void draw(float x, float y) {
        float baseX = x;
        for (char c : getCharArray()) {
            if (c == '\n') {
                x = baseX;
                y -= charSize;
                continue;
            }
            GUIObject character = new GUIObject((int) x, (int) y, (int) charSize, (int) charSize, CharacterManager.getIdentifierForChar(c));
            character.draw();
            x += charSize;
        }
    }

    private static float getSingleLineWidth(char[] line, float charSize) {
        int charLength = 0;
        for (char c : line) {
            if (c == '\n') {
                break;
            }
            charLength++;
        }
        return charSize * charLength;
    }

    public static Text of(String text) {
        return of(text, 1.0F);
    }

    public static Text of(String text, float scale) {
        return new Text(scale) {
            @Override
            public char[] getCharArray() {
                return text.toCharArray();
            }
        };
    }

    public static Text translatable(String text) {
        return translatable(text, 1.0F);
    }

    public static Text translatable(String text, float scale) {
        return new Text(scale) {
            @Override
            public char[] getCharArray() {
                return LanguageManager.getStringTranslation(text).toCharArray();
            }
        };
    }

}
