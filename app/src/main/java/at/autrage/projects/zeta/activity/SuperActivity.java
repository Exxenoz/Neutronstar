package at.autrage.projects.zeta.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SuperActivity extends AppCompatActivity {

    public final int WishedSystemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @Override
    protected void onResume() {
        super.onResume();

        enableImmersiveMode();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        enableImmersiveMode();
    }

    @Override
    public void onBackPressed() {
        // Default: Do nothing
    }

    protected void enableImmersiveMode() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(WishedSystemUiVisibility);
        decorView.setOnSystemUiVisibilityChangeListener(new SystemUiVisibilityChangeListener(decorView));
    }

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
