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

    private float lineHeight;
    private Map<Character, Glyph> glyphMap;

    public Font(String name, String fontDataFile, int fontAtlasTextureResId) {
        this.Name = name;
        this.FontDataFile = fontDataFile;

        this.FontAtlasTexture = AssetManager.getInstance().getTexture(fontAtlasTextureResId);
        if (this.FontAtlasTexture == null) {
            Logger.E("Could not load font atlas texture with resource ID " + fontAtlasTextureResId + " for font " + name + "!");
        }

        this.lineHeight = 0f;
        this.glyphMap = new HashMap<>();
    }

    public void loadGlyphMap(android.content.res.AssetManager am) {
        glyphMap.clear();

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(am.open(FontDataFile, android.content.res.AssetManager.ACCESS_BUFFER)));

            String line = reader.readLine();
            for (; line != null; line = reader.readLine()) {
                if (line.startsWith("char id=")) {
                    loadGlyphLine(line);
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

        Logger.D("Loaded " + glyphMap.size() + " glyphs for font " + Name + "!");
    }

    private void loadCommonLine(String line) {
        // Remove multiple whitespaces with a single one
        line = line.replaceAll("\\s+", " ");

        String[] attributes = line.split(" ");
        if (attributes.length < 11) {
            return;
        }

        String lineHeightValue = getValueFrom(attributes[1]);

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

        if (cValue == null || xValue == null || yValue == null ||
            wValue == null || hValue == null) {
            Logger.W("Could not load glyph for font " + Name + ", because the following line is invalid: " + line);
            return;
        }

        try {
            char character = (char) Integer.parseInt(cValue);
            int x = Integer.parseInt(xValue);
            int y = Integer.parseInt(yValue);
            int w = Integer.parseInt(wValue);
            int h = Integer.parseInt(hValue);
            glyphMap.put(character, new Glyph(character, x, y, w, h));
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
