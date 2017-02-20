package at.autrage.projects.zeta.module;


import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.view.SurfaceHolder;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.animation.Animation;
import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.animation.AnimationSets;
import at.autrage.projects.zeta.animation.AnimationType;
import at.autrage.projects.zeta.animation.Animations;
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

    private boolean m_Initialized;

    private AssetManager() {
        m_Animations = new HashMap<>();
        m_AnimationSets = new HashMap<>();
        m_ColorShader = null;
        m_SpriteShader = null;
        m_Textures = new HashMap<>();

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
                R.drawable.gv_explosion3,
                R.drawable.debug,
                R.drawable.gv_planet
        };

        for (int textureResId : textureResIds) {
            m_Textures.put(textureResId, new Texture(textureResId));
        }
    }

    private void loadAnimationData() {
        m_Animations.clear();

        m_Animations.put(Animations.BackgroundGameDefault, new Animation(0, R.drawable.background_game, "BackgroundGameDefault", 1920, 1080, 1920, 1080, 0, 0, 1920, 1080, 0f));
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
        m_Animations.put(Animations.Debug, new Animation(14, R.drawable.debug, "Debug", 300, 192, 64, 64, 0, 0, 192, 192, 1f));
        m_Animations.put(Animations.DebugCircle, new Animation(15, R.drawable.debug, "DebugCircle", 300, 192, 100, 100, 200, 0, 300, 100, 0f));
        m_Animations.put(Animations.Planet, new Animation(16, R.drawable.gv_planet, "Planet", 1024, 512, 1024, 512, 0, 0, 1024, 512, 0f));


        m_AnimationSets.clear();

        m_AnimationSets.put(AnimationSets.BackgroundGame, new AnimationSet(0, "BackgroundGame", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.BackgroundGameDefault));
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
        m_AnimationSets.put(AnimationSets.Debug, new AnimationSet(14, "Debug", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.Debug));
        }}));
        m_AnimationSets.put(AnimationSets.DebugCircle, new AnimationSet(15, "DebugCircle", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.DebugCircle));
        }}));
        m_AnimationSets.put(AnimationSets.Planet, new AnimationSet(16, "Planet", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.Planet));
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

            m_SpriteShader.createVertexShader("sprite_vertex_shader.glsl", context);
            m_SpriteShader.createFragmentShader("sprite_fragment_shader.glsl", context);
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
}
