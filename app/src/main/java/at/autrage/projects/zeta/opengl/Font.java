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
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].startsWith("lineHeight=")) {
                lineHeightValue = getValueFrom(attributes[i]);
                break;
            }
        }

        try {
            lineHeight = Integer.parseInt(lineHeightValue);
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
            glyphMap.put(character, new Glyph(character, x, y, w, h, xO, yO, xA, wNorm, hNorm, xONorm, yONorm, xANorm));
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

    public float getLineHeight() {
        return lineHeight;
    }

    public Glyph getGlyphForCharacter(char c) {
        return glyphMap.get(c);
    }
}
