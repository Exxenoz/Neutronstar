package at.autrage.projects.zeta.util;

import at.autrage.projects.zeta.activity.SuperActivity;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;

public final class CoordinateTranslator {
    public static float ScreenCoordXToWorldCoordX(float screenCoordX) {
        return screenCoordX * SuperActivity.getScaleFactorX() - Pustafin.HalfReferenceResolutionX;
    }

    public static float ScreenCoordYToWorldCoordY(float screenCoordY) {
        return screenCoordY * SuperActivity.getScaleFactorY() - Pustafin.HalfReferenceResolutionY;
    }

    public static float[] CalculateNormalisedTextureCoordinates(int x, int y, int w, int h, int textureWidth, int textureHeight) {
        if (textureWidth == 0 || textureHeight == 0) {
            Logger.E("Could not calculate normalised texture coordinates, because texture width and height may not be zero!");
            return new float[8];
        }

        float texCoordTopLeftX = x;
        float texCoordTopLeftY = y;

        float texCoordTopRightX = texCoordTopLeftX + w;
        float texCoordTopRightY = texCoordTopLeftY;

        float texCoordBottomLeftX = x;
        float texCoordBottomLeftY = texCoordTopLeftY + h;

        float texCoordBottomRightX = texCoordBottomLeftX + w;
        float texCoordBottomRightY = texCoordBottomLeftY;

        return new float[] {
                texCoordBottomLeftX / textureWidth, texCoordBottomLeftY / textureHeight,     // Bottom Left
                texCoordBottomRightX / textureWidth, texCoordBottomRightY / textureHeight,   // Bottom Right
                texCoordTopLeftX / textureWidth, texCoordTopLeftY / textureHeight,           // Top Left
                texCoordTopRightX / textureWidth, texCoordTopRightY / textureHeight          // Top Right
        };
    }
}
