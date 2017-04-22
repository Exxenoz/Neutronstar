package at.autrage.projects.zeta.opengl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.Logger;

public class Font {
    public final String Name;
    public final String FontDataFile;
    public final Texture FontAtlasTexture;

    private float size;
    private float lineHeight;
    private float lineHeightNorm;
    private int scaleW;
    private int scaleH;
    private Map<Character, Glyph> glyphMap;

    public Font(String name, String fontDataFile, int fontAtlasTextureResId) {
        this.Name = name;
        this.FontDataFile = fontDataFile;

        this.FontAtlasTexture = AssetManager.getInstance().getTexture(fontAtlasTextureResId);
        if (this.FontAtlasTexture == null) {
            Logger.E("Could not load font atlas texture with resource ID " + fontAtlasTextureResId + " for font " + name + "!");
        }

        this.size = 0f;
        this.lineHeight = 0f;
        this.lineHeightNorm = 0f;
        this.scaleW = 0;
        this.scaleH = 0;
        this.glyphMap = new HashMap<>();
    }

    public void load(android.content.res.AssetManager am) {
        glyphMap.clear();

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(am.open(FontDataFile, android.content.res.AssetManager.ACCESS_BUFFER)));

            String line = reader.readLine();
            for (; line != null; line = reader.readLine()) {
                if (line.startsWith("char id=")) {
                    loadGlyphLine(line);
                }
                else if (line.startsWith("info ")) {
                    loadInfoLine(line);
                }
                else if (line.startsWith("common ")) {
                    loadCommonLine(line);
                }
            }
        } catch (Exception e) {
            Logger.E("Could not read font data file \"" + FontDataFile + "\": " + e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    Logger.E("Could not close reader for font data file \"" + FontDataFile + "\": " + e);
                }
            }
        }

        if (size > 0f) {
            lineHeightNorm = lineHeight / size;
        }
        else {
            Logger.E("Could not calculate normalized line height for font " + Name + ", because font size is invalid!");
        }

        Logger.D("Loaded " + glyphMap.size() + " glyphs for font " + Name + "!");
    }

    private void loadInfoLine(String line) {
        // Remove multiple whitespaces with a single one
        line = line.replaceAll("\\s+", " ");

        String[] attributes = line.split(" ");

        String sizeValue = null;
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].startsWith("size=")) {
                sizeValue = getValueFrom(attributes[i]);
                break;
            }
        }

        try {
            size = Integer.parseInt(sizeValue);
        }
        catch (Exception e) {
            Logger.E("Could not load info line for font " + Name + ", because the following line is invalid: " + line);
        }
    }

    private void loadCommonLine(String line) {
        // Remove multiple whitespaces with a single one
        line = line.replaceAll("\\s+", " ");

        String[] attributes = line.split(" ");

        String lineHeightValue = null;
        String scaleWValue = null;
        String scaleHValue = null;
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].startsWith("lineHeight=")) {
                lineHeightValue = getValueFrom(attributes[i]);
            }
            else if (attributes[i].startsWith("scaleW=")) {
                scaleWValue = getValueFrom(attributes[i]);
            }
            else if (attributes[i].startsWith("scaleH=")) {
                scaleHValue = getValueFrom(attributes[i]);
            }
        }

        try {
            lineHeight = Integer.parseInt(lineHeightValue);
            scaleW = Integer.parseInt(scaleWValue);
            scaleH = Integer.parseInt(scaleHValue);
        }
        catch (Exception e) {
            Logger.E("Could not load common line for font " + Name + ", because the following line is invalid: " + line);
        }
    }

    private void loadGlyphLine(String line) {
        // Remove multiple whitespaces with a single one
        line = line.replaceAll("\\s+", " ");

        String[] attributes = line.split(" ");
        if (attributes.length < 11) {
            return;
        }

        String cValue = getValueFrom(attributes[1]);
        String xValue = getValueFrom(attributes[2]);
        String yValue = getValueFrom(attributes[3]);
        String wValue = getValueFrom(attributes[4]);
        String hValue = getValueFrom(attributes[5]);
        String xOValue = getValueFrom(attributes[6]);
        String yOValue = getValueFrom(attributes[7]);
        String xAValue = getValueFrom(attributes[8]);

        if (cValue == null || xValue == null || yValue == null ||
            wValue == null || hValue == null || xOValue == null || yOValue == null || xAValue == null) {
            Logger.W("Could not load glyph for font " + Name + ", because the following line is invalid: " + line);
            return;
        }

        try {
            char character = (char) Integer.parseInt(cValue);
            int x = Integer.parseInt(xValue);
            int y = Integer.parseInt(yValue);
            int w = Integer.parseInt(wValue);
            int h = Integer.parseInt(hValue);
            int xO = Integer.parseInt(xOValue);
            int yO = Integer.parseInt(yOValue);
            int xA = Integer.parseInt(xAValue);
            float wNorm = 0f;
            float hNorm = 0f;
            float xONorm = 0f;
            float yONorm = 0f;
            float xANorm = 0f;
            if (size > 0f) {
                wNorm = w / size;
                hNorm = h / size;
                xONorm = xO / size;
                yONorm = yO / size;
                xANorm = xA / size;
            }
            else {
                Logger.E("Could not calculate normalized width, height, xOffset, yOffset and xAdvance for font " + Name + ", because font size is invalid!");
            }
            glyphMap.put(character, new Glyph(character, x, y, w, h, xO, yO, xA, wNorm, hNorm, xONorm, yONorm, xANorm, scaleW, scaleH));
        }
        catch (Exception e) {
            Logger.W("Could not load glyph for font " + Name + ", because the following line could not be parsed: " + line);
        }
    }

    private String getValueFrom(String s) {
        if (s == null) {
            return null;
        }

        if (s == "") {
            return null;
        }

        String[] explode = s.split("=");

        if (explode.length != 2) {
            return null;
        }

        return explode[1];
    }

    private void addEllipsis(GlyphLine glyphLine, TextOverflowModes textOverflowMode, float maxWidthNorm) {
        if (glyphLine == null) {
            return;
        }

        Glyph glyph = getGlyphForCharacter('.');

        if (glyph == null) {
            Logger.W("Could not add ellipsis using character '.', because glyph could not be found in font " + Name + "!");
            return;
        }

        float ellipsisWidthNorm = 3f * glyph.XAdvanceNorm;

        if (textOverflowMode != TextOverflowModes.Overflow &&
            textOverflowMode != TextOverflowModes.OverflowH) {
            Glyph lastRemovedGlyph = null;

            while ((maxWidthNorm - glyphLine.WidthNorm) < ellipsisWidthNorm) {
                if (glyphLine.Glyphs.size() == 0) {
                    break;
                }

                lastRemovedGlyph = glyphLine.Glyphs.removeLast();
                glyphLine.WidthNorm -= lastRemovedGlyph.XAdvanceNorm;
            }
        }

        for (int i = 0; i < 3; i++) {
            glyphLine.Glyphs.add(glyph);
        }

        glyphLine.WidthNorm += ellipsisWidthNorm;
        glyphLine.HalfWidthNorm = glyphLine.WidthNorm / 2f;
    }

    private boolean isWordSeparator(char c) {
        return c == ' ' || c == '-';
    }

    public GlyphBlock measureText(String text, float fontSize, TextOverflowModes textOverflowMode, float maxWidth, float maxHeight, boolean wordWrappingEnabled) {
        if (text == null) {
            Logger.E("Could not measure text, because text parameter is null!");
            return null;
        }

        if (fontSize <= 0f) {
            Logger.E("Could not measure text \"" + text + "\", because font size " + fontSize + " is invalid!");
            return null;
        }

        if (textOverflowMode != TextOverflowModes.Overflow) {
            if (textOverflowMode != TextOverflowModes.OverflowH && maxWidth <= 0f) {
                Logger.E("Could not measure text \"" + text + "\", because max width " + maxWidth + " for overflow mode " + textOverflowMode + " is invalid!");
                return null;
            }

            if (textOverflowMode != TextOverflowModes.OverflowV && maxHeight <= 0f) {
                Logger.E("Could not measure text \"" + text + "\", because max height " + maxHeight + " for overflow mode " + textOverflowMode + " is invalid!");
                return null;
            }
        }

        float maxWidthNorm = maxWidth / fontSize;
        float maxHeightNorm = maxHeight / fontSize;

        GlyphBlock glyphBlock = new GlyphBlock(text, maxWidthNorm, maxHeightNorm);
        GlyphLine glyphLine = null;

        for (int j = 0, length = text.length(); j < length; j++) {
            if (textOverflowMode != TextOverflowModes.Overflow &&
                textOverflowMode != TextOverflowModes.OverflowV) {
                // No space for next line
                if ((maxHeightNorm - glyphBlock.HeightNorm) < lineHeightNorm) {
                    switch (textOverflowMode) {
                        case Ellipsis:
                            addEllipsis(glyphLine, textOverflowMode, maxWidthNorm);
                            break;
                        case Truncate:
                            // Do nothing
                            break;
                    }
                    break;
                }
            }

            glyphLine = new GlyphLine();
            glyphLine.HeightNorm = lineHeightNorm;

            for (char currChar, lastChar = 0; j < length; j++) {
                currChar = text.charAt(j);

                if (currChar == '\n') {
                    break;
                }

                Glyph glyph = glyphMap.get(currChar);

                if (glyph == null) {
                    Logger.W("Could not measure character '" + currChar + "', because glyph could not be found in font " + Name + "!");
                    continue;
                }

                glyphLine.Glyphs.add(glyph);
                glyphLine.WidthNorm += glyph.XAdvanceNorm;

                if ((isWordSeparator(lastChar) || lastChar == 0) && !isWordSeparator(currChar)) {
                    glyphLine.WordCount++;
                }

                if (textOverflowMode == TextOverflowModes.Overflow ||
                    textOverflowMode == TextOverflowModes.OverflowH ||
                    glyphLine.WidthNorm <= maxWidthNorm) {
                    lastChar = currChar;
                    continue;
                }

                // Each line must at least contain one glyph
                if (glyphLine.Glyphs.size() == 1) {
                    break;
                }

                if (wordWrappingEnabled) {
                    if (isWordSeparator(currChar)) {
                        // Remove the word separator
                        glyph = glyphLine.Glyphs.removeLast();
                        glyphLine.WidthNorm -= glyph.XAdvanceNorm;
                        // Move the word separator to the
                        // next line (except whitespace).
                        if (currChar != ' ') {
                            j--;
                        }
                    }
                    else if (glyphLine.WordCount >= 2) {
                        // Remove the last word
                        do
                        {
                            glyph = glyphLine.Glyphs.removeLast();
                            glyphLine.WidthNorm -= glyph.XAdvanceNorm;
                            j--;
                        }
                        while (!isWordSeparator(glyph.Character));

                        // Re-add word separator (except whitespace)
                        if (glyph.Character != ' ') {
                            glyphLine.Glyphs.add(glyph);
                            glyphLine.WidthNorm += glyph.XAdvanceNorm;
                        }

                        // Skip word separator
                        j++;

                        // Decrease word count
                        glyphLine.WordCount--;
                    }
                    else { // Word count == 1
                        // Either the line begins with multiple word separators
                        // and takes all the space, or the word is bigger than
                        // the available line width.

                        // Remove the last character
                        glyph = glyphLine.Glyphs.removeLast();
                        glyphLine.WidthNorm -= glyph.XAdvanceNorm;
                        // Decrease character counter, because the last
                        // character should be placed in the next line.
                        j--;
                    }
                }
                else {
                    // Remove the last character
                    glyph = glyphLine.Glyphs.removeLast();
                    glyphLine.WidthNorm -= glyph.XAdvanceNorm;
                    // Decrease character counter, because the last
                    // character should be placed in the next line.
                    j--;
                }

                break;
            }

            // Remove EOL whitespaces
            while (glyphLine.Glyphs.size() > 0) {
                Glyph glyph = glyphLine.Glyphs.getLast();

                if (glyph.Character != ' ') {
                    break;
                }

                glyphLine.WidthNorm -= glyph.XAdvanceNorm;
                glyphLine.Glyphs.removeLast();
            }

            glyphLine.HalfWidthNorm = glyphLine.WidthNorm / 2f;
            glyphLine.HalfHeightNorm = glyphLine.HeightNorm / 2f;

            if (glyphLine.Glyphs.size() > 0) {
                glyphBlock.addGlyphLine(glyphLine);
            }
        }

        glyphBlock.HalfWidthNorm = glyphBlock.WidthNorm / 2f;
        glyphBlock.HalfHeightNorm = glyphBlock.HeightNorm / 2f;

        return glyphBlock;
    }

    public float getLineHeight() {
        return lineHeight;
    }

    public float getLineHeightNorm() {
        return lineHeightNorm;
    }

    public Glyph getGlyphForCharacter(char c) {
        return glyphMap.get(c);
    }
}
