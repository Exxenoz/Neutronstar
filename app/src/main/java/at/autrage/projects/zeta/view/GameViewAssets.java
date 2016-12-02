package at.autrage.projects.zeta.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.activity.SuperActivity;
import at.autrage.projects.zeta.animation.Animation;
import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.animation.AnimationType;
import at.autrage.projects.zeta.model.GameObject;

public class GameViewAssets {
    private GameView m_GameView;
    private Animation[] m_Animations;
    private AnimationSet[] m_AnimationSets;

    public GameViewAssets(GameView gameView) {
        m_GameView = gameView;
        loadAnimationData();
        loadGameObjects();
    }

    private void loadAnimationData() {
        m_Animations = new Animation[] {
            new Animation(0, R.drawable.background_game, "BackgroundGameDefault", 0, 0, 1920, 1080, 1920, 1080, 0)
        };
        m_AnimationSets = new AnimationSet[] {
            new AnimationSet(0, "BackgroundGame", new HashMap<AnimationType, Animation>() {
                {
                    put(AnimationType.Default, m_Animations[0]);
                }
            })
        };
    }

    private void loadGameObjects() {
        new GameObject(m_GameView, 0, 0, m_AnimationSets[0]);
    }

    public void load(Resources resources) {
        for (Animation a : m_Animations) {
            a.load(resources);
        }
    }

    public void unLoad() {
        for (Animation a : m_Animations) {
            a.unLoad();
        }
    }
}
