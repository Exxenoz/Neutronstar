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
        m_Animations.put(Animations.SmallRocket, new Animation(3, R.drawable.gv_weapon_small_rocket, "WeaponSmallRocket", 0, 0, 64, 64, 64, 64, 0f));
        m_Animations.put(Animations.BigRocket, new Animation(4, R.drawable.gv_weapon_big_rocket, "WeaponBigRocket", 0, 0, 80, 80, 80, 80, 0f));
        m_Animations.put(Animations.Asteroid1, new Animation(5, R.drawable.gv_enemy_asteroid1, "EnemyAsteroid1", 0, 0, 256, 256, 256, 256, 0f));
        m_Animations.put(Animations.Asteroid2, new Animation(6, R.drawable.gv_enemy_asteroid2, "EnemyAsteroid2", 0, 0, 256, 256, 256, 256, 0f));
        m_Animations.put(Animations.Asteroid3, new Animation(7, R.drawable.gv_enemy_asteroid3, "EnemyAsteroid3", 0, 0, 256, 256, 256, 256, 0f));
        m_Animations.put(Animations.Explosion1, new Animation(8, R.drawable.gv_explosion_sheet1, "Explosion1", 0, 0, 512, 512, 64, 64, 0.032f));

        m_AnimationSets.put(AnimationSets.BackgroundGame, new AnimationSet(0, "BackgroundGame", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.BackgroundGameDefault));
        }}));
        m_AnimationSets.put(AnimationSets.Planet, new AnimationSet(1, "Planet", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.PlanetSheet));
        }}));
        m_AnimationSets.put(AnimationSets.Clouds, new AnimationSet(2, "Clouds", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.CloudSheet));
        }}));
        m_AnimationSets.put(AnimationSets.SmallRocket, new AnimationSet(3, "SmallRocket", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.SmallRocket));
        }}));
        m_AnimationSets.put(AnimationSets.BigRocket, new AnimationSet(4, "BigRocket", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.BigRocket));
        }}));
        m_AnimationSets.put(AnimationSets.Asteroid1, new AnimationSet(5, "Asteroid1", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.Asteroid1));
        }}));
        m_AnimationSets.put(AnimationSets.Asteroid2, new AnimationSet(6, "Asteroid2", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.Asteroid2));
        }}));
        m_AnimationSets.put(AnimationSets.Asteroid3, new AnimationSet(7, "Asteroid3", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.Asteroid3));
        }}));
        m_AnimationSets.put(AnimationSets.Explosion1, new AnimationSet(8, "Explosion1", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.Explosion1));
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
