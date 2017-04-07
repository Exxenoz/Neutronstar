package at.autrage.projects.zeta.opengl;

public class Glyph {
    public final char Character;
    public final int X;
    public final int Y;
    public final int W;
    public final int H;

    public Glyph(char character, int x, int y, int w, int h) {
        Character = character;
        X = x;
        Y = y;
        W = w;
        H = h;
    }
}
