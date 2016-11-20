package at.autrage.projects.zeta.activity;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class is inherited by most project activities to enable immersive mode automatically.
 *
 * <p>The back button method {@link SuperActivity#onBackPressed()} was overwritten by using an empty implementation.</p>
 */
public class SuperActivity extends AppCompatActivity {

    public final int ReferenceResolutionX = 1920;
    public final int ReferenceResolutionY = 1080;

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
        Log.d("PNE::Debug", "Activity Layout Name: " + getResources().getResourceEntryName(rootViewGroup.getId()));
        Log.d("PNE::Debug", "Activity Layout Childs: " + rootViewGroup.getChildCount());

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point(); display.getRealSize(size);

        int currResolutionX = size.x;
        int currResolutionY = size.y;

        float scaleFactorX = currResolutionX / (float)ReferenceResolutionX;
        float scaleFactorY = currResolutionY / (float)ReferenceResolutionY;

        float scaleFactor = Math.min(scaleFactorX, scaleFactorY);

        Log.d("PNE::Debug", "Reference Resolution: " + ReferenceResolutionX + "x" + ReferenceResolutionY);
        Log.d("PNE::Debug", "Current Resolution: " + currResolutionX + "x" + currResolutionY);
        Log.d("PNE::Debug", "ScaleFactor (to current Resolution): " + scaleFactor);

        int counter = 0;

        if (scaleFactor == 1.0f) {
            Log.d("PNE::Debug", "No scaling required, because scale factor is 1!");
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
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getTextSize() * scaleFactor);
            }

            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)currView.getLayoutParams();

            if (layoutParams.width > 0) {
                layoutParams.width *= scaleFactor;
            }

            if (layoutParams.height > 0) {
                layoutParams.height *= scaleFactor;
            }

            int leftMargin = layoutParams.leftMargin;
            int rightMargin = layoutParams.rightMargin;
            int topMargin = layoutParams.topMargin;
            int bottomMargin = layoutParams.bottomMargin;

            if (leftMargin > 0) {
                leftMargin *= scaleFactor;
            }

            if (rightMargin > 0) {
                rightMargin *= scaleFactor;
            }

            if (topMargin > 0) {
                topMargin *= scaleFactor;
            }

            if (bottomMargin > 0) {
                bottomMargin *= scaleFactor;
            }

            layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

            currView.setLayoutParams(layoutParams);

            ++counter;
        }

        Log.d("PNE::Debug", "Scaled " + counter + " views to current resolution!");
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
