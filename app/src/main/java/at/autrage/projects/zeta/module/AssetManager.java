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
import at.autrage.projects.zeta.animation.AnimationInfo;
import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.animation.AnimationSets;
import at.autrage.projects.zeta.animation.AnimationType;
import at.autrage.projects.zeta.animation.Animations;
import at.autrage.projects.zeta.module.texturepacker.PackedTexture;
import at.autrage.projects.zeta.module.texturepacker.TexturePackerAtlas;
import at.autrage.projects.zeta.module.texturepacker.TexturePackerInterpreter;
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

    public void initialize(Context context) {
        if (m_Initialized) {
            return;
        }

        m_Initialized = true;

        loadTextureData();
        loadAnimationData(context);
        loadShaderData();
    }

    private void loadTextureData() {
        m_Textures.clear();

        int[] textureResIds = new int[] {
                R.drawable.background_game,
                R.drawable.gv_explosion_sheet1,
                R.drawable.gv_engine_fire,
                R.drawable.gv_explosion2,
                R.drawable.gv_explosion3,
                R.drawable.debug,
                R.drawable.gv_planet,
                R.drawable.gv_texture_atlas
        };

        for (int textureResId : textureResIds) {
            m_Textures.put(textureResId, new Texture(textureResId));
        }
    }

    private void loadAnimationFromTexturePacker(AnimationInfo animationInfo, Context context) {
        TexturePackerAtlas atlas = TexturePackerInterpreter.getInstance().parseAtlas(animationInfo.TextureAtlasJSONFile, context.getAssets());
        if (atlas == null) {
            Logger.E("Could not load animation " + animationInfo.PackedTextureName + " from texture packer, because texture packer atlas could not be loaded!");
            return;
        }

        PackedTexture packedTexture = atlas.getTextureByFileName(animationInfo.PackedTextureName);
        if (packedTexture == null) {
            Logger.E("Could not load animation " + animationInfo.PackedTextureName + " from texture packer, because packed texture " + animationInfo.PackedTextureName + " could not be found!");
            return;
        }

        int frameSizeX = packedTexture.w;
        int frameSizeY = packedTexture.h;

        if (animationInfo.Duration != 0f &&
            animationInfo.FrameSizeX > -1 &&
            animationInfo.FrameSizeY > -1) {
            frameSizeX = animationInfo.FrameSizeX;
            frameSizeY = animationInfo.FrameSizeY;
        }

        m_Animations.put(animationInfo.AnimationID, new Animation(animationInfo.TextureResourceID, animationInfo.PackedTextureName,
                atlas.w, atlas.h,
                frameSizeX, frameSizeY,
                packedTexture.x, packedTexture.y,
                packedTexture.x + packedTexture.w, packedTexture.y + packedTexture.h,
                animationInfo.Duration));
    }

    private void loadAnimationFromAnimationInfo(AnimationInfo animationInfo) {
        m_Animations.put(animationInfo.AnimationID, new Animation(animationInfo.TextureResourceID, animationInfo.PackedTextureName,
                animationInfo.TextureSizeX, animationInfo.TextureSizeY,
                animationInfo.FrameSizeX, animationInfo.FrameSizeY,
                animationInfo.StartTexCoordX, animationInfo.StartTexCoordY,
                animationInfo.EndTexCoordX, animationInfo.EndTexCoordY,
                animationInfo.Duration));
    }

    private void loadAnimationData(Context context) {
        m_Animations.clear();

        AnimationInfo[] animationInfos = new AnimationInfo[] {
                new AnimationInfo(R.drawable.background_game, Animations.BackgroundGameDefault, "background_game", 1920, 1080),
                new AnimationInfo(R.drawable.gv_texture_atlas, Animations.SmallRocket, "gv_weapon_small_rocket", "gv_texture_atlas.json"),
                new AnimationInfo(R.drawable.gv_texture_atlas, Animations.BigRocket, "gv_weapon_big_rocket", "gv_texture_atlas.json"),
                new AnimationInfo(R.drawable.gv_texture_atlas, Animations.SmallNuke, "gv_weapon_small_nuke", "gv_texture_atlas.json"),
                new AnimationInfo(R.drawable.gv_texture_atlas, Animations.BigNuke, "gv_weapon_big_nuke", "gv_texture_atlas.json"),
                new AnimationInfo(R.drawable.gv_texture_atlas, Animations.Asteroid1, "gv_enemy_asteroid1", "gv_texture_atlas.json"),
                new AnimationInfo(R.drawable.gv_texture_atlas, Animations.Asteroid2, "gv_enemy_asteroid2", "gv_texture_atlas.json"),
                new AnimationInfo(R.drawable.gv_texture_atlas, Animations.Asteroid3, "gv_enemy_asteroid3", "gv_texture_atlas.json"),
                new AnimationInfo(R.drawable.gv_explosion_sheet1, Animations.Explosion1, "gv_explosion_sheet1", 512, 512, 64, 64, 0.032f),
                new AnimationInfo(R.drawable.gv_explosion2, Animations.Explosion2, "gv_explosion2", 768, 768, 128, 128, 0.032f),
                new AnimationInfo(R.drawable.gv_explosion3, Animations.Explosion3, "gv_explosion3", 768, 768, 128, 128, 0.032f),
                new AnimationInfo(R.drawable.gv_engine_fire, Animations.EngineFire, "gv_engine_fire", 512, 512, 64, 64, 0.032f),
                new AnimationInfo(R.drawable.gv_planet, Animations.Planet, "gv_planet", 1024, 512),
                new AnimationInfo(R.drawable.debug, Animations.Debug, "debug", 300, 192, 64, 64, 0, 0, 192, 192, 1f),
                new AnimationInfo(R.drawable.debug, Animations.DebugCircle, "debug_circle", 300, 192, 100, 100, 200, 0, 300, 100, 0f),
        };

        for (AnimationInfo animationInfo : animationInfos) {
            if (animationInfo.TextureAtlasJSONFile != null) {
                loadAnimationFromTexturePacker(animationInfo, context);
            }
            else {
                loadAnimationFromAnimationInfo(animationInfo);
            }
        }


        m_AnimationSets.clear();

        m_AnimationSets.put(AnimationSets.BackgroundGame, new AnimationSet("BackgroundGame", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.BackgroundGameDefault));
        }}));
        m_AnimationSets.put(AnimationSets.SmallRocket, new AnimationSet("SmallRocket", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.SmallRocket));
        }}));
        m_AnimationSets.put(AnimationSets.BigRocket, new AnimationSet("BigRocket", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.BigRocket));
        }}));
        m_AnimationSets.put(AnimationSets.Asteroid1, new AnimationSet("Asteroid1", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.Asteroid1));
        }}));
        m_AnimationSets.put(AnimationSets.Asteroid2, new AnimationSet("Asteroid2", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.Asteroid2));
        }}));
        m_AnimationSets.put(AnimationSets.Asteroid3, new AnimationSet("Asteroid3", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.Asteroid3));
        }}));
        m_AnimationSets.put(AnimationSets.Explosion1, new AnimationSet("Explosion1", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.Explosion1));
        }}));
        m_AnimationSets.put(AnimationSets.EngineFire, new AnimationSet("EngineFire", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.EngineFire));
        }}));
        m_AnimationSets.put(AnimationSets.SmallNuke, new AnimationSet("SmallNuke", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.SmallNuke));
        }}));
        m_AnimationSets.put(AnimationSets.BigNuke, new AnimationSet("BigNuke", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.BigNuke));
        }}));
        m_AnimationSets.put(AnimationSets.Explosion2, new AnimationSet("Explosion2", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.Explosion2));
        }}));
        m_AnimationSets.put(AnimationSets.Explosion3, new AnimationSet("Explosion3", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.Explosion3));
        }}));
        m_AnimationSets.put(AnimationSets.Debug, new AnimationSet("Debug", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.Debug));
        }}));
        m_AnimationSets.put(AnimationSets.DebugCircle, new AnimationSet("DebugCircle", new HashMap<AnimationType, Animation>() {{
            put(AnimationType.Default, m_Animations.get(Animations.DebugCircle));
        }}));
        m_AnimationSets.put(AnimationSets.Planet, new AnimationSet("Planet", new HashMap<AnimationType, Animation>() {{
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
        }

        if (m_SpriteShader != null) {
            // ToDo: Is it legit to "just" reset shader ids or do we
            // have to free resources by calling the delete methods?
            m_SpriteShader.reset();

            m_SpriteShader.createVertexShader("sprite_vertex_shader.glsl", context);
            m_SpriteShader.createFragmentShader("sprite_fragment_shader.glsl", context);
            m_SpriteShader.createProgram();
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
