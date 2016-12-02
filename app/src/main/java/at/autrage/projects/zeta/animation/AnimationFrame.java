package at.autrage.projects.zeta.animation;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.module.Logger;

public class AnimationFrame {
    private int m_SequenceImageResID;
    private Bitmap m_SequenceImage;
    private Rect m_TexCoordRect;
    private int m_TexCoordX;
    private int m_TexCoordY;
    private int m_SizeX;
    private int m_SizeY;
    private float m_Duration;
    private AnimationFrame m_NextFrame;

    public AnimationFrame(int sequenceImageResID, int texCoordX, int texCoordY, int sizeX, int sizeY, float duration) {
        this.m_SequenceImageResID = sequenceImageResID;
        this.m_SequenceImage = null;
        this.m_TexCoordX = texCoordX;
        this.m_TexCoordY = texCoordY;
        this.m_SizeX = sizeX;
        this.m_SizeY = sizeY;
        this.m_Duration = duration;

        m_TexCoordRect = new Rect(texCoordX, texCoordY, texCoordX + sizeX, texCoordY + sizeY);
    }

    public void load(Resources resources) {
        if (m_SequenceImage != null) {
            Logger.E("Could not load animation frame, because it is already loaded!");
            return;
        }

        m_SequenceImage = BitmapFactory.decodeResource(resources, m_SequenceImageResID);
    }

    public void unLoad() {
        if (m_SequenceImage != null) {
            m_SequenceImage.recycle();
            m_SequenceImage = null;
        }
    }

    public Bitmap getSequenceImage() {
        return m_SequenceImage;
    }

    public Rect getTexCoordRect() {
        return m_TexCoordRect;
    }

    public int getTexCoordX() {
        return m_TexCoordX;
    }

    public int getTexCoordY() {
        return m_TexCoordY;
    }

    public int getSizeX() {
        return m_SizeX;
    }

    public int getSizeY() {
        return m_SizeY;
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
