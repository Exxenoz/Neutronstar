package at.autrage.projects.zeta.animation;

import at.autrage.projects.zeta.opengl.Texture;

public class AnimationFrame {
    private Animation m_Owner;
    private int m_TexCoordX;
    private int m_TexCoordY;
    private int m_FrameSizeX;
    private int m_FrameSizeY;
    private float m_Duration;
    private AnimationFrame m_LastFrame;
    private AnimationFrame m_NextFrame;

    public AnimationFrame(Animation owner, int texCoordX, int texCoordY, int frameSizeX, int frameSizeY, float duration) {
        this.m_Owner = owner;
        this.m_TexCoordX = texCoordX;
        this.m_TexCoordY = texCoordY;
        this.m_FrameSizeX = frameSizeX;
        this.m_FrameSizeY = frameSizeY;
        this.m_Duration = duration;
        this.m_LastFrame = null;
        this.m_NextFrame = null;
    }

    public Texture getTexture() {
        return m_Owner.getTexture();
    }

    public int getTexCoordX() {
        return m_TexCoordX;
    }

    public int getTexCoordY() {
        return m_TexCoordY;
    }

    public int getFrameSizeX() {
        return m_FrameSizeX;
    }

    public int getFrameSizeY() {
        return m_FrameSizeY;
    }

    public float getDuration() {
        return m_Duration;
    }

    public AnimationFrame getLastFrame() {
        return m_LastFrame;
    }

    public void setLastFrame(AnimationFrame m_LastFrame) {
        this.m_LastFrame = m_LastFrame;
    }

    public AnimationFrame getNextFrame() {
        return m_NextFrame;
    }

    public void setNextFrame(AnimationFrame nextFrame) {
        this.m_NextFrame = nextFrame;
    }
}
