package at.autrage.projects.zeta.animation;


import android.graphics.Bitmap;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

public class Animation {
    private int m_ID;
    private Bitmap m_SequenceImage;
    private String m_Name;
    private Point m_StartTexCoords;
    private Point m_EndTexCoords;
    private Point m_Size;
    private float m_Duration;
    private List<AnimationFrame> m_AnimationFrames;

    public Animation(int ID, Bitmap sequenceImage, String name,
                     Point startTexCoords, Point endTexCoords,
                     Point size, float duration) {
        this.m_ID = ID;
        this.m_SequenceImage = sequenceImage;
        this.m_Name = name;
        this.m_StartTexCoords = startTexCoords;
        this.m_EndTexCoords = endTexCoords;
        this.m_Size = size;
        this.m_Duration = duration;
        this.m_AnimationFrames = new ArrayList<AnimationFrame>();

        GenerateAnimationFrames();
    }

    public AnimationFrame GetFirstAnimationFrame() {
        return m_AnimationFrames != null && m_AnimationFrames.size() > 0 ? m_AnimationFrames.get(0) : null;
    }

    private void GenerateAnimationFrames() {
        for (int texCoordY = m_StartTexCoords.y; texCoordY < m_EndTexCoords.y; texCoordY += m_Size.y)
        {
            for (int texCoordX = m_StartTexCoords.x; texCoordX < m_EndTexCoords.x; texCoordX += m_Size.x)
            {
                m_AnimationFrames.add(new AnimationFrame(m_SequenceImage, new Point(texCoordX, texCoordY), m_Size, m_Duration));
            }
        }

        for (int i = 0, next = i + 1; i < m_AnimationFrames.size(); i++, next++)
        {
            if (next == m_AnimationFrames.size())
            {
                next = 0;
            }

            m_AnimationFrames.get(i).setNextFrame(m_AnimationFrames.get(next));
        }
    }
}
