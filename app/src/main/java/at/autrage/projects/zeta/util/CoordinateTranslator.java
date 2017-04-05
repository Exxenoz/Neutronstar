package at.autrage.projects.zeta.util;

import at.autrage.projects.zeta.activity.SuperActivity;
import at.autrage.projects.zeta.module.Pustafin;

public final class CoordinateTranslator {
    public static float ScreenCoordXToWorldCoordX(float screenCoordX) {
        return screenCoordX * SuperActivity.getScaleFactorX() - Pustafin.HalfReferenceResolutionX;
    }

    public static float ScreenCoordYToWorldCoordY(float screenCoordY) {
        return screenCoordY * SuperActivity.getScaleFactorY() - Pustafin.HalfReferenceResolutionY;
    }
}
