package at.autrage.projects.zeta.opengl;

import java.util.LinkedList;

public class GlyphLine {
    public LinkedList<Glyph> Glyphs;
    public float WidthNorm;
    public float HeightNorm;
    public int WordCount;

    public GlyphLine() {
        Glyphs = new LinkedList<>();
        WidthNorm = 0f;
        HeightNorm = 0f;
        WordCount = 0;
    }
}
