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
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
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

    private float m_RotationAngle;

    private int m_SizeX;
    private int m_SizeY;

    private float m_HalfSizeX;
    private float m_HalfSizeY;

    private float m_ScaledSizeX;
    private float m_ScaledSizeY;

    private float m_ScaledHalfSizeX;
    private float m_ScaledHalfSizeY;

    private float m_ScaleFactor;

    private Rect m_DstRect;

    private Animation m_Animation;
    private AnimationSet m_AnimationSet;
    private AnimationFrame m_CurrentAnimationFrame;
    private float m_AnimationTimer;
    private boolean m_AnimationReversed;

    private float m_DirectionX;
    private float m_DirectionY;
    private float m_SpeedX;
    private float m_SpeedY;
    private float m_Speed;

    private Collider m_Collider;

    public GameObject(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        m_GameView = gameView;

        m_PositionX = positionX;
        m_PositionY = positionY;

        m_ScaledPositionX = 0;
        m_ScaledPositionY = 0;

        m_RotationAngle = 0f;

        m_SizeX = m_SizeY = 0;
        m_HalfSizeX = m_HalfSizeY = 0;
        m_ScaledSizeX = m_ScaledSizeY = 0;
        m_ScaledHalfSizeX = m_ScaledHalfSizeY = 0;

        m_ScaleFactor = 1f;

        m_DstRect = new Rect();

        m_Animation = null;
        m_AnimationSet = animationSet;
        m_CurrentAnimationFrame = null;
        m_AnimationTimer = 0;
        m_AnimationReversed = false;

        m_DirectionX = 0f;
        m_DirectionY = 0f;
        setSpeed(0f);

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

        if (m_Speed != 0f) {
            m_PositionX += m_SpeedX * Time.getScaledDeltaTime();
            m_PositionY += m_SpeedY * Time.getScaledDeltaTime();
        }

        float scaleFactor = SuperActivity.getScaleFactor();
        m_ScaledPositionX = m_PositionX * scaleFactor;
        m_ScaledPositionY = m_PositionY * scaleFactor;

        float scaledPivotPositionX = m_ScaledPositionX - m_ScaledHalfSizeX;
        float scaledPivotPositionY = m_ScaledPositionY - m_ScaledHalfSizeY;

        m_DstRect.set((int)(scaledPivotPositionX), (int)(scaledPivotPositionY),
                (int)(scaledPivotPositionX + m_ScaledSizeX), (int)(scaledPivotPositionY + m_ScaledSizeY));
    }

    public void onCollide(Collider collider) {
    }

    public void onDraw(Canvas canvas) {
        if (m_CurrentAnimationFrame != null) {
            canvas.save(Canvas.MATRIX_SAVE_FLAG);
            canvas.rotate(m_RotationAngle, m_ScaledPositionX, m_ScaledPositionY);
            canvas.drawBitmap(m_CurrentAnimationFrame.getSequenceImage(), m_CurrentAnimationFrame.getTexCoordRect(), m_DstRect, null);
            canvas.restore();

            if (Pustafin.DebugMode && m_Collider != null) {
                m_Collider.onDraw(canvas);
            }
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

        m_HalfSizeX = m_SizeX / 2f;
        m_HalfSizeY = m_SizeY / 2f;

        float scaleFactor = SuperActivity.getScaleFactor();
        m_ScaledSizeX = m_SizeX * scaleFactor;
        m_ScaledSizeY = m_SizeY * scaleFactor;

        m_ScaledHalfSizeX = m_HalfSizeX * scaleFactor;
        m_ScaledHalfSizeY = m_HalfSizeY * scaleFactor;
    }

    public void setAnimationReversed(boolean animationReversed) {
        this.m_AnimationReversed = animationReversed;
    }

    public void setRotationAngle(float rotationAngle) {
        m_RotationAngle = rotationAngle;
    }

    public void setScaleFactor(float scaleFactor) {
        m_ScaleFactor = scaleFactor;
        setCurrentAnimationFrame(m_CurrentAnimationFrame);
    }

    public void setHalfSizeX(float halfSizeX) {
        this.m_HalfSizeX = halfSizeX;
    }

    public void setHalfSizeY(float halfSizeY) {
        this.m_HalfSizeY = halfSizeY;
    }

    public void setScaledHalfSizeX(float scaledHalfSizeX) {
        this.m_ScaledHalfSizeX = scaledHalfSizeX;
    }

    public void setScaledHalfSizeY(float scaledHalfSizeY) {
        this.m_ScaledHalfSizeY = scaledHalfSizeY;
    }

    public void setDirectionX(float directionX) {
        this.m_DirectionX = directionX;
        setSpeed(m_Speed);
    }

    public void setDirectionY(float directionY) {
        this.m_DirectionY = directionY;
        setSpeed(m_Speed);
    }

    public void setDirection(float directionX, float directionY) {
        this.m_DirectionX = directionX;
        this.m_DirectionY = directionY;
        setSpeed(m_Speed);
    }

    public void setSpeed(float speed) {
        m_Speed = speed;
        m_SpeedX = m_DirectionX * speed;
        m_SpeedY = m_DirectionY * speed;
    }

    public void setCollider(Collider collider) {
        m_Collider = collider;
    }

    public GameView getGameView() {
        return m_GameView;
    }

    public float getPositionX() {
        return m_PositionX;
    }

    public float getPositionY() {
        return m_PositionY;
    }

    public float getScaledPositionX() {
        return m_ScaledPositionX;
    }

    public float getScaledPositionY() {
        return m_ScaledPositionY;
    }

    public float getRotationAngle() {
        return m_RotationAngle;
    }

    public float getHalfSizeX() {
        return m_HalfSizeX;
    }

    public float getHalfSizeY() {
        return m_HalfSizeY;
    }

    public float getScaledHalfSizeX() {
        return m_ScaledHalfSizeX;
    }

    public float getScaledHalfSizeY() {
        return m_ScaledHalfSizeY;
    }

    public float getScaleFactor() {
        return m_ScaleFactor;
    }

    public Collider getCollider() {
        return m_Collider;
    }

    public void destroy() {
        if (m_GameView != null) {
            m_GameView.addGameObjectToDeleteQueue(this);
        }
    }
}
