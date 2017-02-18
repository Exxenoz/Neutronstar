package at.autrage.projects.zeta.module;


import android.util.Log;

/**
 * This class helps maintaining total time independence of the running program.
 *
 * <p>One can use this class to run an application with a constant speed which is independent
 * of the device processor clock rate.</p>
 *
 */
public class Time {
    /** Real passed time in seconds */
    private static float realDeltaTime;
    /** Passed time in seconds */
    private static float deltaTime;
    /** Passed time in seconds multiplied by the factor {@link Time#timeScale} */
    private static float scaledDeltaTime;
    /** Factor which gets multiplied to {@link Time#deltaTime} to form {@link Time#scaledDeltaTime} */
    private static float timeScale = 1f;
    /** Passed time in milli seconds */
    private static long deltaTimeInMs;
    /** Passed time in seconds */
    private static float deltaTimeGL;

    /**
     * Sets the {@link Time#realDeltaTime} variable to a new value.
     *
     * @param realDeltaTime must be positive
     */
    public static void setRealDeltaTime(float realDeltaTime) {
        if (realDeltaTime < 0) {
            Logger.E("Couldn't set real delta time, because value must not be negative.");
            return;
        }

        Time.realDeltaTime = realDeltaTime;
    }

    /**
     * Sets the {@link Time#deltaTime} variable to a new value.
     *
     * @param deltaTime must be positive
     */
    public static void setDeltaTime(float deltaTime) {
        if (deltaTime < 0) {
            Logger.E("Couldn't set delta time, because value must not be negative.");
            return;
        }

        Time.deltaTime = deltaTime;
        Time.scaledDeltaTime = deltaTime * Time.timeScale;
    }

    /**
     * Sets the {@link Time#timeScale} variable
     *
     * @param timeScale must be positive
     */
    public static void setTimeScale(float timeScale) {
        if (timeScale < 0) {
            Logger.E("Couldn't set time scale, because value must not be negative.");
            return;
        }

        Time.timeScale = timeScale;
        Time.scaledDeltaTime = timeScale * Time.deltaTime;
    }

    /**
     * Sets the {@link Time#deltaTimeInMs} variable
     *
     * @param deltaTimeInMs must be positive
     */
    public static void setDeltaTimeInMs(long deltaTimeInMs) {
        if (deltaTimeInMs < 0) {
            Logger.E("Couldn't set delta time in ms, because value must not be negative.");
            return;
        }

        Time.deltaTimeInMs = deltaTimeInMs;
    }

    /**
     * Sets the {@link Time#deltaTimeGL} variable to a new value.
     *
     * @param deltaTimeGL must be positive
     */
    public static void setDeltaTimeGL(float deltaTimeGL) {
        if (deltaTimeGL < 0) {
            Logger.E("Couldn't set delta time GL, because value must not be negative.");
            return;
        }

        Time.deltaTimeGL = deltaTimeGL;
    }

    /**
     * Returns the value of {@link Time#realDeltaTime}
     *
     * @return the value of {@link Time#realDeltaTime}
     */
    public static float getRealDeltaTime() {
        return Time.realDeltaTime;
    }

    /**
     * Returns the value of {@link Time#deltaTime}
     *
     * @return the value of {@link Time#deltaTime}
     */
    public static float getDeltaTime() {
        return Time.deltaTime;
    }

    /**
     * Returns the value of {@link Time#scaledDeltaTime}
     *
     * @return the value of {@link Time#scaledDeltaTime}
     */
    public static float getScaledDeltaTime() {
        return Time.scaledDeltaTime;
    }

    /**
     * Returns the value of {@link Time#timeScale}
     *
     * @return the value of {@link Time#timeScale}
     */
    public static float getTimeScale() {
        return Time.timeScale;
    }

    /**
     * Returns the value of {@link Time#deltaTimeInMs}
     *
     * @return the value of {@link Time#deltaTimeInMs}
     */
    public static long getDeltaTimeInMs() {
        return Time.deltaTimeInMs;
    }

    /**
     * Returns the value of {@link Time#deltaTimeGL}
     *
     * @return the value of {@link Time#deltaTimeGL}
     */
    public static float getDeltaTimeGL() {
        return Time.deltaTimeGL;
    }

    /**
     * Returns the estimated amount of frames per second
     *
     * @return the value of estimated frames per second
     */
    public static long getFPS() {
        return (long)(1000f / Math.max(Time.deltaTimeGL, 1f /* 1000 FPS display limit */));
    }
}
