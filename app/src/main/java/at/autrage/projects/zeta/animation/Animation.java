package at.autrage.projects.zeta.animation;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

import at.autrage.projects.zeta.module.Logger;

public class Animation {
    private int m_ID;
    private String m_Name;

    private int m_SequenceImageResID;
    private Bitmap m_SequenceImage;

    private int m_SizeX;
    private int m_SizeY;

    private int m_FrameSizeX;
    private int m_FrameSizeY;

    private float m_ScaleFactorX;
    private float m_ScaleFactorY;

    private int m_StartTexCoordX;
    private int m_StartTexCoordY;

    private int m_EndTexCoordX;
    private int m_EndTexCoordY;

    private float m_Duration;
    private List<AnimationFrame> m_AnimationFrames;

    public Animation(int ID, int sequenceImageResID, String name,
                     int sizeX, int sizeY, int frameSizeX, int frameSizeY,
                     int startTexCoordX, int startTexCoordY, int endTexCoordX, int endTexCoordY, float duration) {
        this.m_ID = ID;
        this.m_Name = name;

        this.m_SequenceImageResID = sequenceImageResID;
        this.m_SequenceImage = null;

        this.m_SizeX = sizeX;
        this.m_SizeY = sizeY;

        this.m_FrameSizeX = frameSizeX;
        this.m_FrameSizeY = frameSizeY;

        this.m_ScaleFactorX = 0f;
        this.m_ScaleFactorY = 0f;

        this.m_StartTexCoordX = startTexCoordX;
        this.m_StartTexCoordY = startTexCoordY;

        this.m_EndTexCoordX = endTexCoordX;
        this.m_EndTexCoordY = endTexCoordY;

        this.m_Duration = duration;
        this.m_AnimationFrames = new ArrayList<AnimationFrame>();

        generateAnimationFrames();
    }

    private void generateAnimationFrames() {
        for (int texCoordY = m_StartTexCoordY; texCoordY < m_EndTexCoordY; texCoordY += m_FrameSizeY)
        {
            for (int texCoordX = m_StartTexCoordX; texCoordX < m_EndTexCoordX; texCoordX += m_FrameSizeX)
            {
                m_AnimationFrames.add(new AnimationFrame(this, texCoordX, texCoordY, m_FrameSizeX, m_FrameSizeY, m_Duration));
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

    public void load(Resources resources) {
        if (m_SequenceImage != null) {
            Logger.E("Could not load animation frame, because it is already loaded!");
            return;
        }

        m_SequenceImage = BitmapFactory.decodeResource(resources, m_SequenceImageResID);

        m_ScaleFactorX = m_SequenceImage.getWidth() / (float)m_SizeX;
        m_ScaleFactorY = m_SequenceImage.getHeight() / (float)m_SizeY;

        for (AnimationFrame animationFrame : m_AnimationFrames) {
            animationFrame.initializeScaledTexCoordRect(m_ScaleFactorX, m_ScaleFactorY);
        }

        Logger.D("Loaded Sequence Image for Animation %s with size (%d, %d), original size (%d, %d) and scale factors (%f, %f)",
                m_Name, m_SequenceImage.getWidth(), m_SequenceImage.getHeight(), m_SizeX, m_SizeY, m_ScaleFactorX, m_ScaleFactorY);
    }

    public void unLoad() {
        if (m_SequenceImage != null) {
            m_SequenceImage.recycle();
            m_SequenceImage = null;
        }
    }

    public int getID() {
        return m_ID;
    }

    public String getName() {
        return m_Name;
    }

    public Bitmap getSequenceImage() {
        return m_SequenceImage;
    }

    public int getSequenceImageResID() {
        return m_SequenceImageResID;
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
