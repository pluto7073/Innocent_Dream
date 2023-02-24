package io.innocentdream.objects.texts;

import io.innocentdream.objects.GUIObject;
import io.innocentdream.utils.Utils;

public class Text {

    private char[] charArray;
    private float charSize;

    public Text(String s) {
        this(s.toCharArray());
    }

    public Text(char[] charArray) {
        this(charArray, 1f);
    }

    public Text(char[] charArray, float scale) {
        this.charArray = charArray;
        this.charSize = scale;
    }

    public void setText(char[] charArray) {
        this.charArray = charArray;
    }

    public void setText(String s) {
        setText(s.toCharArray());
    }

    public void setScale(float scale) {
        this.charSize = scale;
    }

    public int getTotalLines() {
        return Utils.countArrayOccurrences(charArray, '\n') + 1;
    }

    public float getWidth() {
        if (getTotalLines() == 1) {
            return getSingleLineWidth(charArray, charSize);
        } else {
            String[] lines = new String(charArray).split("\n");
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
        for (char c : charArray) {
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

}
