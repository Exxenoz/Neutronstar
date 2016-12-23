package at.autrage.projects.zeta.activity;

import android.os.Bundle;
import android.view.ViewGroup;

import at.autrage.projects.zeta.R;

public class ShopActivity extends SuperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        scaleChildViewsToCurrentResolution((ViewGroup)findViewById(R.id.activity_shop));
    }

    @Override
    protected void onResume() {
        super.onResume();

        m_CurrentActivity = Activities.ShopActivity;
    }
}
