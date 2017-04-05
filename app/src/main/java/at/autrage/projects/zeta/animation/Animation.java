package at.autrage.projects.zeta.animation;

import java.util.ArrayList;
import java.util.List;

import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.opengl.Texture;

public class Animation {
    private String m_Name;

    private int m_TextureResId;
    private Texture m_Texture;

    private int m_TextureSizeX;
    private int m_TextureSizeY;

    private int m_FrameSizeX;
    private int m_FrameSizeY;

    private int m_StartTexCoordX;
    private int m_StartTexCoordY;

    private int m_EndTexCoordX;
    private int m_EndTexCoordY;

    private float m_Duration;
    private List<AnimationFrame> m_AnimationFrames;

    public Animation(int textureResId, String name,
                     int textureSizeX, int textureSizeY, int frameSizeX, int frameSizeY,
                     int startTexCoordX, int startTexCoordY, int endTexCoordX, int endTexCoordY, float duration) {
        this.m_Name = name;

        this.m_TextureResId = textureResId;
        this.m_Texture = AssetManager.getInstance().getTexture(textureResId);

        if (this.m_Texture == null) {
            Logger.E("Could not load texture with resource ID " + textureResId + " for animation " + m_Name + "!");
        }

        this.m_TextureSizeX = textureSizeX;
        this.m_TextureSizeY = textureSizeY;

        this.m_FrameSizeX = frameSizeX;
        this.m_FrameSizeY = frameSizeY;

        this.m_StartTexCoordX = startTexCoordX;
        this.m_StartTexCoordY = startTexCoordY;

        this.m_EndTexCoordX = endTexCoordX;
        this.m_EndTexCoordY = endTexCoordY;

        this.m_Duration = duration;
        this.m_AnimationFrames = new ArrayList<>();

        generateAnimationFrames();
    }

    private void generateAnimationFrames() {
        for (int texCoordY = m_StartTexCoordY; texCoordY < m_EndTexCoordY; texCoordY += m_FrameSizeY)
        {
            for (int texCoordX = m_StartTexCoordX; texCoordX < m_EndTexCoordX; texCoordX += m_FrameSizeX)
            {
                m_AnimationFrames.add(new AnimationFrame(this, texCoordX, texCoordY, m_FrameSizeX, m_FrameSizeY, m_TextureSizeX, m_TextureSizeY, m_Duration));
            }
        }

        for (int i = 0, next = i + 1, last = i - 1; i < m_AnimationFrames.size(); i++, next++)
        {
            if (last < 0) {
                last = m_AnimationFrames.size() - 1;
            }

            if (next == m_AnimationFrames.size()) {
                next = 0;
            }

            AnimationFrame lastAnimationFrame = m_AnimationFrames.get(last);
            AnimationFrame currentAnimationFrame = m_AnimationFrames.get(i);
            AnimationFrame nextAnimationFrame = m_AnimationFrames.get(next);

            currentAnimationFrame.setLastFrame(lastAnimationFrame);
            currentAnimationFrame.setNextFrame(nextAnimationFrame);

            last = i;
        }
    }

    public String getName() {
        return m_Name;
    }

    public int getTextureResID() {
        return m_TextureResId;
    }

    public Texture getTexture() {
        return m_Texture;
    }

    public int getFrameSizeX() {
        return m_FrameSizeX;
    }

    public int getFrameSizeY() {
        return m_FrameSizeY;
    }

    public int getStartTexCoordX() {
        return m_StartTexCoordX;
    }

    public int getStartTexCoordY() {
        return m_StartTexCoordY;
    }

    public int getEndTexCoordX() {
        return m_EndTexCoordX;
    }

    public int getEndTexCoordY() {
        return m_EndTexCoordY;
    }

    public AnimationFrame getFirstAnimationFrame() {
        return m_AnimationFrames != null && m_AnimationFrames.size() > 0 ? m_AnimationFrames.get(0) : null;
    }
}
