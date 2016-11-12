package at.autrage.projects.zeta.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import at.autrage.projects.zeta.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent redirectIntent = new Intent(this, IntroActivity.class);
        startActivity(redirectIntent);
    }
}
