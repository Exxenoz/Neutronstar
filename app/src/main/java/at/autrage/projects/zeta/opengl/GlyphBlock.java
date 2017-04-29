package at.autrage.projects.zeta.opengl;

import java.util.LinkedList;

public class GlyphBlock {
    public final String OriginalText;
    public LinkedList<GlyphLine> GlyphLines;
    public float WidthNorm;
    public float HeightNorm;
    public float HalfWidthNorm;
    public float HalfHeightNorm;
    public final float MaxWidthNorm;
    public final float MaxHeightNorm;
    public final float HalfMaxWidthNorm;
    public final float HalfMaxHeightNorm;

    public GlyphBlock(String originalText, float maxWidthNorm, float maxHeightNorm) {
        OriginalText = originalText;
        GlyphLines = new LinkedList<>();
        WidthNorm = 0f;
        HeightNorm = 0f;
        HalfWidthNorm = 0f;
        HalfHeightNorm = 0f;
        MaxWidthNorm = maxWidthNorm;
        MaxHeightNorm = maxHeightNorm;
        HalfMaxWidthNorm = maxWidthNorm / 2f;
        HalfMaxHeightNorm = maxHeightNorm / 2f;
    }

    public void addGlyphLine(GlyphLine glyphLine) {
        GlyphLines.add(glyphLine);

        if (glyphLine.WidthNorm > WidthNorm) {
            WidthNorm = glyphLine.WidthNorm;
        }

        HeightNorm += glyphLine.HeightNorm;
    }

    public int getGlyphCount() {
        int glyphCount = 0;

        for (GlyphLine glyphLine : GlyphLines) {
            glyphCount += glyphLine.Glyphs.size();
        }

        return glyphCount;
    }
}
