package at.autrage.projects.zeta.module;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLES20;
import android.view.SurfaceHolder;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.animation.Animation;
import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.animation.AnimationType;
import at.autrage.projects.zeta.opengl.ColorShader;
import at.autrage.projects.zeta.opengl.SpriteShader;
import at.autrage.projects.zeta.opengl.Texture;
import at.autrage.projects.zeta.view.GameView;
import at.autrage.projects.zeta.view.GameViewRenderer;

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
    private ColorShader m_ColorShader;
    private SpriteShader m_SpriteShader;
    private Map<Integer /* ResId */, Texture> m_Textures;

    private Paint m_HealthBarFillPaintGreen;
    private Paint m_HealthBarFillPaintOrange;
    private Paint m_HealthBarFillPaintRed;

    private boolean m_Initialized;

    private AssetManager() {
        m_Animations = new HashMap<>();
        m_AnimationSets = new HashMap<>();
        m_ColorShader = null;
        m_SpriteShader = null;
        m_Textures = new HashMap<>();

        m_HealthBarFillPaintGreen = new Paint();
        m_HealthBarFillPaintOrange = new Paint();
        m_HealthBarFillPaintRed = new Paint();

        m_HealthBarFillPaintGreen.setColor(Color.GREEN);
        m_HealthBarFillPaintOrange.setColor(Color.rgb(255, 200, 0));
        m_HealthBarFillPaintRed.setColor(Color.RED);

        m_Initialized = false;
    }

    public void initialize() {
        if (m_Initialized) {
            return;
        }

        m_Initialized = true;

        loadTextureData();
        loadAnimationData();
        loadShaderData();
    }

    private void loadTextureData() {
        m_Textures.clear();

        int[] textureResIds = new int[] {
                R.drawable.background_game,
                R.drawable.gv_planet_sheet_100p,
                R.drawable.gv_planet_cloud_sheet_100p,
                R.drawable.gv_weapon_small_rocket,
                R.drawable.gv_weapon_big_rocket,
                R.drawable.gv_enemy_asteroid1,
                R.drawable.gv_enemy_asteroid2,
                R.drawable.gv_enemy_asteroid3,
                R.drawable.gv_explosion_sheet1,
                R.drawable.gv_engine_fire,
                R.drawable.gv_weapon_small_nuke,
                R.drawable.gv_weapon_big_nuke,
                R.drawable.gv_explosion2,
                R.drawable.gv_explosion3
        };

        for (int textureResId : textureResIds) {
            m_Textures.put(textureResId, new Texture(textureResId));
        }
    }

    private void loadAnimationData() {
        m_Animations.clear();

        m_Animations.put(Animations.BackgroundGameDefault, new Animation(0, R.drawable.background_game, "BackgroundGameDefault", 1920, 1080, 1920, 1080, 0, 0, 1920, 1080, 0f));
        m_Animations.put(Animations.PlanetSheet, new Animation(1, R.drawable.gv_planet_sheet_100p, "PlanetSheet", 2000, 1800, 100, 100, 0, 0, 2000, 1800, 0.032f));
        m_Animations.put(Animations.CloudSheet, new Animation(2, R.drawable.gv_planet_cloud_sheet_100p, "CloudSheet", 2000, 1800, 100, 100, 0, 0, 2000, 1800, 0.04f));
        m_Animations.put(Animations.SmallRocket, new Animation(3, R.drawable.gv_weapon_small_rocket, "WeaponSmallRocket", 64, 64, 64, 64, 0, 0, 64, 64, 0f));
        m_Animations.put(Animations.BigRocket, new Animation(4, R.drawable.gv_weapon_big_rocket, "WeaponBigRocket", 80, 80, 80, 80, 0, 0, 80, 80, 0f));
        m_Animations.put(Animations.Asteroid1, new Animation(5, R.drawable.gv_enemy_asteroid1, "EnemyAsteroid1", 256, 256, 256, 256, 0, 0, 256, 256, 0f));
        m_Animations.put(Animations.Asteroid2, new Animation(6, R.drawable.gv_enemy_asteroid2, "EnemyAsteroid2", 256, 256, 256, 256, 0, 0, 256, 256, 0f));
        m_Animations.put(Animations.Asteroid3, new Animation(7, R.drawable.gv_enemy_asteroid3, "EnemyAsteroid3", 256, 256, 256, 256, 0, 0, 256, 256, 0f));
        m_Animations.put(Animations.Explosion1, new Animation(8, R.drawable.gv_explosion_sheet1, "Explosion1", 512, 512, 64, 64, 0, 0, 512, 512, 0.032f));
        m_Animations.put(Animations.EngineFire, new Animation(9, R.drawable.gv_engine_fire, "EngineFire", 512, 512, 64, 64, 0, 0, 512, 512, 0.032f));
        m_Animations.put(Animations.SmallNuke, new Animation(10, R.drawable.gv_weapon_small_nuke, "WeaponSmallNuke", 64, 64, 64, 64, 0, 0, 64, 64, 0f));
        m_Animations.put(Animations.BigNuke, new Animation(11, R.drawable.gv_weapon_big_nuke, "WeaponBigNuke", 64, 64, 64, 64, 0, 0, 64, 64, 0f));
        m_Animations.put(Animations.Explosion2, new Animation(12, R.drawable.gv_explosion2, "Explosion2", 768, 768, 128, 128, 0, 0, 768, 768, 0.032f));
        m_Animations.put(Animations.Explosion3, new Animation(13, R.drawable.gv_explosion3, "Explosion3", 768, 768, 128, 128, 0, 0, 768, 768, 0.032f));


        m_AnimationSets.clear();

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
        m_AnimationSets.put(AnimationSets.EngineFire, new AnimationSet(9, "EngineFire", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.EngineFire));
        }}));
        m_AnimationSets.put(AnimationSets.SmallNuke, new AnimationSet(10, "SmallNuke", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.SmallNuke));
        }}));
        m_AnimationSets.put(AnimationSets.BigNuke, new AnimationSet(11, "BigNuke", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.BigNuke));
        }}));
        m_AnimationSets.put(AnimationSets.Explosion2, new AnimationSet(12, "Explosion2", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.Explosion2));
        }}));
        m_AnimationSets.put(AnimationSets.Explosion3, new AnimationSet(13, "Explosion3", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.Explosion3));
        }}));
    }

    private void loadShaderData() {
        m_ColorShader = new ColorShader();
        m_SpriteShader = new SpriteShader();
    }

    /**
     * This method is called, when {@link GameView#surfaceCreated(SurfaceHolder)} is called.
     * Do not call OpenGL methods here!
     */
    public void onSurfaceCreated(Resources resources) {
    }

    /**
     * This method is called, when {@link GameViewRenderer#onSurfaceCreated(GL10, EGLConfig)} is called.
     * You can call OpenGL methods here.
     */
    public void onSurfaceCreatedFromOpenGL(Context context) {
        if (m_ColorShader != null) {
            m_ColorShader.reset();

            m_ColorShader.createVertexShader("color_vertex_shader.glsl", context);
            m_ColorShader.createFragmentShader("color_fragment_shader.glsl", context);
            m_ColorShader.createProgram();

            m_ColorShader.init();
        }

        if (m_SpriteShader != null) {
            // ToDo: Is it legit to "just" reset shader ids or do we
            // have to free resources by calling the delete methods?
            m_SpriteShader.reset();

            m_SpriteShader.createVertexShader("color_vertex_shader.glsl", context);
            m_SpriteShader.createFragmentShader("color_fragment_shader.glsl", context);
            m_SpriteShader.createProgram();

            m_SpriteShader.init();
        }

        final int[] textureHandles = new int[m_Textures.size()];

        GLES20.glGenTextures(textureHandles.length, textureHandles, 0);

        int i = 0;
        for (Texture texture : m_Textures.values()) {
            texture.load(context, textureHandles[i]);
            i++;
        }
    }

    /**
     * This method is called, when {@link GameView#surfaceDestroyed(SurfaceHolder)} is called.
     * Do not call OpenGL methods here!
     */
    public void onSurfaceDestroyed() {
        if (m_ColorShader != null) {
            m_ColorShader.reset();
        }

        if (m_SpriteShader != null) {
            m_SpriteShader.reset();
        }
    }

    public Texture getTexture(int resId) {
        return m_Textures.get(resId);
    }

    public Animation getAnimation(Animations animation) {
        return m_Animations.get(animation);
    }

    public AnimationSet getAnimationSet(AnimationSets animationSet) {
        return m_AnimationSets.get(animationSet);
    }

    public ColorShader getColorShader() {
        return m_ColorShader;
    }

    public SpriteShader getSpriteShader() {
        return m_SpriteShader;
    }

    public Paint getHealthBarFillPaintGreen() {
        return m_HealthBarFillPaintGreen;
    }

    public Paint getHealthBarFillPaintOrange() {
        return m_HealthBarFillPaintOrange;
    }

    public Paint getHealthBarFillPaintRed() {
        return m_HealthBarFillPaintRed;
    }
}
