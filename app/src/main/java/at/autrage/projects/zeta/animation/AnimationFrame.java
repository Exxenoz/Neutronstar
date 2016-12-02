package at.autrage.projects.zeta.animation;


import android.graphics.Bitmap;
import android.graphics.Point;

public class AnimationFrame {
    private Bitmap m_SequenceImage;
    private Point m_TexCoords;
    private Point m_Size;
    private float m_Duration;
    private AnimationFrame m_NextFrame;

    public AnimationFrame(Bitmap sequenceImage, Point texCoords, Point size, float duration) {
        this.m_SequenceImage = sequenceImage;
        this.m_TexCoords = texCoords;
        this.m_Size = size;
        this.m_Duration = duration;
    }

    public Bitmap getSequenceImage() {
        return m_SequenceImage;
    }

    public Point getTexCoords() {
        return m_TexCoords;
    }

    public Point getSize() {
        return m_Size;
    }

    public float getDuration() {
        return m_Duration;
    }

    public AnimationFrame getNextFrame() {
        return m_NextFrame;
    }

    public void setNextFrame(AnimationFrame nextFrame) {
        this.m_NextFrame = nextFrame;
    }
}
