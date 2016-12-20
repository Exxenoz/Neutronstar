package at.autrage.projects.zeta.module;


import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.animation.Animation;
import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.animation.AnimationType;

public class AssetManager {
    private static AssetManager m_Instance;

    public static AssetManager getInstance() {
        if (m_Instance == null) {
            m_Instance = new AssetManager();
        }

        return m_Instance;
    }

    private Map<Animations, Animation> m_Animations;
    private Map<AnimationSets, AnimationSet> m_AnimationSets;

    private boolean m_Initialized;

    private AssetManager() {
        m_Animations = new HashMap<Animations, Animation>();
        m_AnimationSets = new HashMap<AnimationSets, AnimationSet>();

        m_Initialized = false;
    }

    public void initialize() {
        if (!m_Initialized) {
            m_Initialized = true;
            loadAnimationData();
        }
    }

    private void loadAnimationData() {
        m_Animations.put(Animations.BackgroundGameDefault, new Animation(0, R.drawable.background_game, "BackgroundGameDefault", 0, 0, 1920, 1080, 1920, 1080, 0));
        m_Animations.put(Animations.PlanetSheet, new Animation(1, R.drawable.gv_planet_sheet_100p, "PlanetSheet", 0, 0, 2000, 1800, 100, 100, 0.032f));
        m_Animations.put(Animations.CloudSheet, new Animation(2, R.drawable.gv_planet_cloud_sheet_100p, "CloudSheet", 0, 0 , 2000, 1800, 100, 100, 0.04f));

        m_AnimationSets.put(AnimationSets.BackgroundGame, new AnimationSet(0, "BackgroundGame", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.BackgroundGameDefault));
        }}));
        m_AnimationSets.put(AnimationSets.Planet, new AnimationSet(1, "Planet", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.PlanetSheet));
        }}));
        m_AnimationSets.put(AnimationSets.Clouds, new AnimationSet(2, "Clouds", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.CloudSheet));
        }}));
    }

    public void load(Resources resources) {
        for (Animation a : m_Animations.values()) {
            a.load(resources);
        }
    }

    public void unLoad() {
        for (Animation a : m_Animations.values()) {
            a.unLoad();
        }
    }

    public Animation getAnimation(Animations animation) {
        return m_Animations.get(animation);
    }

    public AnimationSet getAnimationSet(AnimationSets animationSet) {
        return m_AnimationSets.get(animationSet);
    }
}
