package at.autrage.projects.zeta.activity;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.util.Util;

/**
 * This class is inherited by most project activities to enable immersive mode automatically.
 *
 * <p>The back button method {@link SuperActivity#onBackPressed()} was overwritten by using an empty implementation.</p>
 */
public class SuperActivity extends AppCompatActivity {

    public enum Activities {
        MainActivity,
        IntroActivity,
        SplashScreenActivity,
        MainMenuActivity,
        GameActivity,
        HighscoreActivity,
        ShopActivity,
        CreditsActivity
    }

    /** The currently active activity */
    protected static Activities m_CurrentActivity = Activities.MainActivity;

    /**
     * Returns the value of {@link SuperActivity#m_CurrentActivity}
     *
     * @return the value of {@link SuperActivity#m_CurrentActivity}
     */
    public static Activities getCurrentActivity() {
        return m_CurrentActivity;
    }

    private static int m_CurrentResolutionX;
    private static int m_CurrentResolutionY;

    /**
     * Returns the value of {@link SuperActivity#m_CurrentResolutionX}
     *
     * @return the value of {@link SuperActivity#m_CurrentResolutionX}
     */
    public static int getCurrentResolutionX() {
        return m_CurrentResolutionX;
    }

    /**
     * Returns the value of {@link SuperActivity#m_CurrentResolutionY}
     *
     * @return the value of {@link SuperActivity#m_CurrentResolutionY}
     */
    public static int getCurrentResolutionY() {
        return m_CurrentResolutionY;
    }

    private static float m_ScaleFactor;
    private static float scaleFactorX;
    private static float scaleFactorY;

    /**
     * Returns the value of {@link SuperActivity#m_ScaleFactor}
     *
     * @return the value of {@link SuperActivity#m_ScaleFactor}
     */
    public static float getScaleFactor() {
        return m_ScaleFactor;
    }

    public static float getScaleFactorX() {
        return scaleFactorX;
    }
    public static float getScaleFactorY() {
        return scaleFactorY;
    }

    /**
     * Used as globally accessible immersive mode flags container.
     */
    public final int WishedSystemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enableImmersiveMode(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        enableImmersiveMode(false);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        enableImmersiveMode(false);
    }

    @Override
    public void onBackPressed() {
        // Default: Do nothing
    }

    protected void scaleChildViewsToCurrentResolution(ViewGroup rootViewGroup) {
        Logger.D("Activity Layout Name: " + getResources().getResourceEntryName(rootViewGroup.getId()));
        Logger.D("Activity Layout Childs: " + rootViewGroup.getChildCount());

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point(); display.getRealSize(size);

        m_CurrentResolutionX = size.x;
        m_CurrentResolutionY = size.y;

        scaleFactorX = m_CurrentResolutionX / (float)Pustafin.ReferenceResolutionX;
        scaleFactorY = m_CurrentResolutionY / (float)Pustafin.ReferenceResolutionY;

        m_ScaleFactor = Math.min(scaleFactorX, scaleFactorY);

        Logger.D("Reference Resolution: " + Pustafin.ReferenceResolutionX + "x" + Pustafin.ReferenceResolutionY);
        Logger.D("Current Resolution: " + m_CurrentResolutionX + "x" + m_CurrentResolutionY);
        Logger.D("ScaleFactor (to current Resolution): " + m_ScaleFactor);

        int counter = 0;

        if (m_ScaleFactor == 1.0f) {
            Logger.D("No scaling required, because scale factor is 1!");
            return;
        }

        for (int i = 0, count = rootViewGroup.getChildCount(); i < count; ++i) {
            View currView = rootViewGroup.getChildAt(i);
            if (!(currView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
                continue;
            }

            if (!(currView instanceof ImageView ||
                currView instanceof TextView ||
                currView instanceof Button)) {
                continue;
            }

            if (currView instanceof TextView) {
                TextView textView = (TextView)currView;
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getTextSize() * m_ScaleFactor);
            }

            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)currView.getLayoutParams();
            Util.setViewSize(currView, layoutParams.width, layoutParams.height);
            Util.setViewMargin(currView, layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);
            Util.setViewPadding(currView, currView.getPaddingLeft(), currView.getPaddingTop(), currView.getPaddingRight(), currView.getPaddingBottom());

            ++counter;
        }

        Logger.D("Scaled " + counter + " views to current resolution!");
    }

    /**
     * Enables immersive mode using the globally accessible immersive mode flags.
     *
     * @param setChangeListener Sets a {@link SystemUiVisibilityChangeListener}
     * to re-enable immersive mode after possible system ui visibility changes:
     * true to set it, false to not.
     */
    protected void enableImmersiveMode(boolean setChangeListener) {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(WishedSystemUiVisibility);
        if (setChangeListener) {
            decorView.setOnSystemUiVisibilityChangeListener(new SystemUiVisibilityChangeListener(decorView));
        }
    }

    /**
     * This class reacts to possible system ui visibility
     * changes by re-enabling immersive mode if necessary.
     */
    private class SystemUiVisibilityChangeListener implements View.OnSystemUiVisibilityChangeListener {

        View m_DecorView;

        public SystemUiVisibilityChangeListener(View decorView) {
            m_DecorView = decorView;
        }

        @Override
        public void onSystemUiVisibilityChange(int visibility) {
            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                m_DecorView.setSystemUiVisibility(WishedSystemUiVisibility);
            }
        }
    }
}
