package at.autrage.projects.zeta.module;


import android.util.Log;

public class Time {
    private static float deltaTime;
    private static float scaledDeltaTime;
    private static float timeScale;
    private static long deltaTimeInMs;

    public static void setDeltaTime(float deltaTime) {
        if (deltaTime < 0) {
            Log.e("PNE::Error", "Couldn't set delta time, because value must not be negative.");
            return;
        }

        Time.deltaTime = deltaTime;
        Time.scaledDeltaTime = deltaTime * Time.timeScale;
    }

    public static void setTimeScale(float timeScale) {
        if (timeScale < 0) {
            Log.e("PNE::Error", "Couldn't set time scale, because value must not be negative.");
            return;
        }

        Time.timeScale = timeScale;
        Time.scaledDeltaTime = timeScale * Time.deltaTime;
    }

    public static void setDeltaTimeInMs(long deltaTimeInMs) {
        if (deltaTimeInMs < 0) {
            Log.e("PNE::Error", "Couldn't set delta time in ms, because value must not be negative.");
            return;
        }

        Time.deltaTimeInMs = deltaTimeInMs;
    }

    public static float getDeltaTime() {
        return Time.deltaTime;
    }

    public static float getScaledDeltaTime() {
        return Time.scaledDeltaTime;
    }

    public static float getTimeScale() {
        return Time.timeScale;
    }

    public static long getDeltaTimeInMs() {
        return Time.deltaTimeInMs;
    }
}
