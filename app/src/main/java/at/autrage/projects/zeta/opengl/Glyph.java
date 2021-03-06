package at.autrage.projects.zeta.opengl;

import at.autrage.projects.zeta.util.CoordinateTranslator;

public class Glyph {
    public final char Character;
    public final int X;
    public final int Y;
    public final int W;
    public final int H;
    public final int XOffset;
    public final int YOffset;
    public final int XAdvance;
    public final float WNorm;
    public final float HNorm;
    public final float XOffsetNorm;
    public final float YOffsetNorm;
    public final float XAdvanceNorm;
    public final float[] TextureCoordinates;

    public Glyph(char character, int x, int y, int w, int h, int xOffset, int yOffset, int xAdvance,
                 float wNorm, float hNorm, float xOffsetNorm, float yOffsetNorm, float xAdvanceNorm, int textureAtlasSizeX, int textureAtlasSizeY) {
        Character = character;
        X = x;
        Y = y;
        W = w;
        H = h;
        XOffset = xOffset;
        YOffset = yOffset;
        XAdvance = xAdvance;
        WNorm = wNorm;
        HNorm = hNorm;
        XOffsetNorm = xOffsetNorm;
        YOffsetNorm = yOffsetNorm;
        XAdvanceNorm = xAdvanceNorm;
        TextureCoordinates = CoordinateTranslator.CalculateNormalisedTextureCoordinates(X, Y, W, H, textureAtlasSizeX, textureAtlasSizeY);
    }
}
