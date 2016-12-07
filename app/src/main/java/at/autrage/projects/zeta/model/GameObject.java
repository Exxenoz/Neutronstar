package at.autrage.projects.zeta.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import at.autrage.projects.zeta.activity.SuperActivity;
import at.autrage.projects.zeta.animation.Animation;
import at.autrage.projects.zeta.animation.AnimationFrame;
import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.animation.AnimationType;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.view.GameView;

/**
 * WIP
 */
public class GameObject {
    private GameView m_GameView;

    private float m_PositionX;
    private float m_PositionY;

    private float m_ScaledPositionX;
    private float m_ScaledPositionY;

    private int m_SizeX;
    private int m_SizeY;

    private float m_ScaledSizeX;
    private float m_ScaledSizeY;

    private float m_ScaleFactor;

    public Rect m_DstRect;

    private Animation m_Animation;
    private AnimationSet m_AnimationSet;
    private AnimationFrame m_CurrentAnimationFrame;
    private float m_AnimationTimer;
    private boolean m_AnimationReversed;

    public GameObject(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        m_GameView = gameView;

        m_PositionX = positionX;
        m_PositionY = positionY;

        m_ScaledPositionX = 0;
        m_ScaledPositionY = 0;

        m_SizeX = m_SizeY = 0;
        m_ScaledSizeX = 0;
        m_ScaledSizeY = 0;

        m_ScaleFactor = 1f;

        m_DstRect = new Rect();

        m_Animation = null;
        m_AnimationSet = animationSet;
        m_CurrentAnimationFrame = null;
        m_AnimationTimer = 0;
        m_AnimationReversed = false;

        if (m_AnimationSet != null) {
            playAnimationFromSet(AnimationType.Default);
        }

        if (m_GameView != null) {
            m_GameView.addGameObjectToInsertQueue(this);
        }
    }

    public void onUpdate() {
        if (m_CurrentAnimationFrame != null &&
            m_CurrentAnimationFrame != m_CurrentAnimationFrame.getNextFrame())
        {
            m_AnimationTimer += Time.getScaledDeltaTime();

            if (m_AnimationTimer >= m_CurrentAnimationFrame.getDuration())
            {
                m_AnimationTimer -= m_CurrentAnimationFrame.getDuration();

                if (m_AnimationReversed) {
                    setCurrentAnimationFrame(m_CurrentAnimationFrame.getLastFrame());
                }
                else
                {
                    setCurrentAnimationFrame(m_CurrentAnimationFrame.getNextFrame());
                }
            }
        }

        float scaleFactor = SuperActivity.getScaleFactor();
        m_ScaledPositionX = m_PositionX * scaleFactor;
        m_ScaledPositionY = m_PositionY * scaleFactor;

        m_DstRect.set((int)m_ScaledPositionX, (int)m_ScaledPositionY,
                (int)(m_ScaledPositionX+m_ScaledSizeX), (int)(m_ScaledPositionY+m_ScaledSizeY));
    }

    public void onDraw(Canvas canvas) {
        if (m_CurrentAnimationFrame != null) {
            canvas.drawBitmap(m_CurrentAnimationFrame.getSequenceImage(), m_CurrentAnimationFrame.getTexCoordRect(), m_DstRect, null);
        }
    }

    public void playAnimationFromSet(AnimationType animationType) {
        if (m_AnimationSet == null)
        {
            Logger.W("Could not play animation type " + animationType + ", because animation set reference is null.");
            return;
        }

        Animation animation = m_AnimationSet.getAnimation(animationType);
        if (animation == null)
        {
            Logger.W("Could not play animation type " + animationType + ", because it could not be found in animation set %i.",
                    m_AnimationSet.getID());
            return;
        }

        AnimationFrame animationFrame = animation.getFirstAnimationFrame();
        if (animationFrame == null)
        {
            Logger.W("Could not play animation %i, because it contains no animation frames.",
                    animation.getID());
            return;
        }

        m_Animation = animation;
        setCurrentAnimationFrame(animationFrame);
        m_AnimationTimer = 0;
    }

    private void setCurrentAnimationFrame(AnimationFrame animationFrame) {
        if (animationFrame == null) {
            return;
        }

        m_CurrentAnimationFrame = animationFrame;
        m_SizeX = (int)(m_CurrentAnimationFrame.getSizeX() * m_ScaleFactor);
        m_SizeY = (int)(m_CurrentAnimationFrame.getSizeY() * m_ScaleFactor);

        float scaleFactor = SuperActivity.getScaleFactor();
        m_ScaledSizeX = m_SizeX * scaleFactor;
        m_ScaledSizeY = m_SizeY * scaleFactor;
    }

    public void setAnimationReversed(boolean animationReversed) {
        this.m_AnimationReversed = animationReversed;
    }

    public void setScaleFactor(float scaleFactor) {
        m_ScaleFactor = scaleFactor;
    }

    public float getScaleFactor() {
        return m_ScaleFactor;
    }

    public void destroy() {
        if (m_GameView != null) {
            m_GameView.addGameObjectToDeleteQueue(this);
        }
    }
}
