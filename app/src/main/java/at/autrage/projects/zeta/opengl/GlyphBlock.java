package at.autrage.projects.zeta.opengl;

import java.util.LinkedList;

public class GlyphBlock {
    public LinkedList<GlyphLine> glyphLines;
    public float WidthNorm;
    public float HeightNorm;

    public GlyphBlock() {
        glyphLines = new LinkedList<>();
        WidthNorm = 0f;
        HeightNorm = 0f;
    }

    public void addGlyphLine(GlyphLine glyphLine) {
        glyphLines.add(glyphLine);

        if (glyphLine.WidthNorm > WidthNorm) {
            WidthNorm = glyphLine.WidthNorm;
        }

        HeightNorm += glyphLine.HeightNorm;
    }
}
