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
import at.autrage.projects.zeta.animation.AnimationSetInfo;
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
                R.drawable.gv_texture_atlas,
                R.drawable.gv_foreground_alarm,
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
                new AnimationInfo(R.drawable.gv_foreground_alarm, Animations.ForegroundAlarm, "gv_foreground_alarm", 1920, 1080),
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

        AnimationSetInfo[] animationSetInfos = new AnimationSetInfo[] {
                new AnimationSetInfo(AnimationSets.BackgroundGame, "BackgroundGame", Animations.BackgroundGameDefault),
                new AnimationSetInfo(AnimationSets.SmallRocket, "SmallRocket", Animations.SmallRocket),
                new AnimationSetInfo(AnimationSets.BigRocket, "BigRocket", Animations.BigRocket),
                new AnimationSetInfo(AnimationSets.Asteroid1, "Asteroid1", Animations.Asteroid1),
                new AnimationSetInfo(AnimationSets.Asteroid2, "Asteroid2", Animations.Asteroid2),
                new AnimationSetInfo(AnimationSets.Asteroid3, "Asteroid3", Animations.Asteroid3),
                new AnimationSetInfo(AnimationSets.Explosion1, "Explosion1", Animations.Explosion1),
                new AnimationSetInfo(AnimationSets.EngineFire, "EngineFire", Animations.EngineFire),
                new AnimationSetInfo(AnimationSets.SmallNuke, "SmallNuke", Animations.SmallNuke),
                new AnimationSetInfo(AnimationSets.BigNuke, "BigNuke", Animations.BigNuke),
                new AnimationSetInfo(AnimationSets.Explosion2, "Explosion2", Animations.Explosion2),
                new AnimationSetInfo(AnimationSets.Explosion3, "Explosion3", Animations.Explosion3),
                new AnimationSetInfo(AnimationSets.Debug, "Debug", Animations.Debug),
                new AnimationSetInfo(AnimationSets.DebugCircle, "DebugCircle", Animations.DebugCircle),
                new AnimationSetInfo(AnimationSets.Planet, "Planet", Animations.Planet),
                new AnimationSetInfo(AnimationSets.ForegroundAlarm, "ForegroundAlarm", Animations.ForegroundAlarm),
        };

        for (AnimationSetInfo animationSetInfo : animationSetInfos) {
            AnimationSet animationSet = new AnimationSet(animationSetInfo.Name);

            for (Map.Entry<AnimationType, Animations> entry : animationSetInfo.Animations.entrySet()) {
                Animation animation = m_Animations.get(entry.getValue());

                if (animation == null) {
                    Logger.W("Could not find animation with ID " + entry.getValue() + " for animation set " + animationSetInfo.Name + "!");
                    continue;
                }

                animationSet.addAnimation(entry.getKey(), animation);
            }

            m_AnimationSets.put(animationSetInfo.AnimationSetID, animationSet);
        }
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
