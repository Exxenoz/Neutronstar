package at.autrage.projects.zeta.opengl;

public class Glyph {
    public final char Character;
    public final int X;
    public final int Y;
    public final int W;
    public final int H;
    public final int XOffset;
    public final int YOffset;
    public final int XAdvance;

    public Glyph(char character, int x, int y, int w, int h, int xOffset, int yOffset, int xAdvance) {
        Character = character;
        X = x;
        Y = y;
        W = w;
        H = h;
        XOffset = xOffset;
        YOffset = yOffset;
        XAdvance = xAdvance;
    }
}
