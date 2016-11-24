package at.autrage.projects.zeta.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.activity.SuperActivity;

public class GameViewAssets {
    public Bitmap BackgroundGame;
    public Rect BackgroundGameRectSrc;
    public Rect BackgroundGameRectDst;

    public GameViewAssets() {
    }

    public void load(Resources resources) {
        int currentResolutionX = SuperActivity.getCurrentResolutionX();
        int currentResolutionY = SuperActivity.getCurrentResolutionY();
        //float scaleFactor = SuperActivity.getScaleFactor();

        BackgroundGame = BitmapFactory.decodeResource(resources, R.drawable.background_game);
        BackgroundGameRectSrc = new Rect(0, 0, BackgroundGame.getWidth(), BackgroundGame.getHeight());
        BackgroundGameRectDst = new Rect(0, 0, currentResolutionX, currentResolutionY);
    }

    public void unLoad() {
        if (BackgroundGame != null) {
            BackgroundGame.recycle();
            BackgroundGame = null;
        }
    }
}
