package at.autrage.projects.zeta.model;

import android.opengl.Matrix;

public class Transform {
    /** The owner of the transform object. */
    private GameObject m_Owner;

    /** The model matrix transforms a position in a model to the position in the world. */
    private float[] m_ModelMatrix;
    /** The translation/rotation matrix cache. */
    private float[] m_TRMatrix;
    /** The translation matrix translates a position. */
    private float[] m_TranslationMatrix;
    /** The rotation matrix rotates a position. */
    private float[] m_RotationMatrix;
    /** The scale matrix scales a position. */
    private float[] m_ScaleMatrix;

    private float m_PositionX;
    private float m_PositionY;
    private float m_PositionZ;

    private float m_RotationX;
    private float m_RotationY;
    private float m_RotationZ;

    private float m_ScaleX;
    private float m_ScaleY;
    private float m_ScaleZ;

    private float m_HalfScaleX;
    private float m_HalfScaleY;
    private float m_HalfScaleZ;

    public Transform(GameObject owner) {
        m_Owner = owner;

        m_ModelMatrix = new float[16];
        m_TRMatrix = new float[16];
        m_TranslationMatrix = new float[16];
        m_RotationMatrix = new float[16];
        m_ScaleMatrix = new float[16];
    }

    public void update() {
        // Update translation matrix
        Matrix.setIdentityM(m_TranslationMatrix, 0);
        Matrix.translateM(m_TranslationMatrix, 0, m_PositionX, m_PositionY, m_PositionZ);
        // Update rotation matrix
        Matrix.setIdentityM(m_RotationMatrix, 0);

        if (m_RotationX != 0f) {
            Matrix.rotateM(m_RotationMatrix, 0, m_RotationX, 1f, 0f, 0f);
        }

        if (m_RotationY != 0f) {
            Matrix.rotateM(m_RotationMatrix, 0, m_RotationY, 0f, 1f, 0f);
        }

        if (m_RotationZ != 0f) {
            Matrix.rotateM(m_RotationMatrix, 0, m_RotationZ, 0f, 0f, 1f);
        }
        // Update scale matrix
        Matrix.setIdentityM(m_ScaleMatrix, 0);
        Matrix.scaleM(m_ScaleMatrix, 0, m_ScaleX, m_ScaleY, m_ScaleZ);
        // Update model matrix
        Matrix.multiplyMM(m_TRMatrix, 0, m_TranslationMatrix, 0, m_RotationMatrix, 0);
        Matrix.multiplyMM(m_ModelMatrix, 0, m_TRMatrix, 0, m_ScaleMatrix, 0);
    }

    public GameObject getOwner() {
        return m_Owner;
    }

    public float[] getModelMatrix() {
        return m_ModelMatrix;
    }

    public float[] getTranslationMatrix() {
        return m_TranslationMatrix;
    }

    public float[] getRotationMatrix() {
        return m_RotationMatrix;
    }

    public float[] getScaleMatrix() {
        return m_ScaleMatrix;
    }

    public float getPositionX() {
        return m_PositionX;
    }

    public float getPositionY() {
        return m_PositionY;
    }

    public float getPositionZ() {
        return m_PositionZ;
    }

    public void setPositionX(float positionX) {
        m_PositionX = positionX;
    }

    public void setPositionY(float positionY) {
        m_PositionY = positionY;
    }

    public void setPositionZ(float positionZ) {
        m_PositionZ = positionZ;
    }

    public void setPosition(float positionX, float positionY) {
        m_PositionX = positionX;
        m_PositionY = positionY;
    }

    public void setPosition(float positionX, float positionY, float positionZ) {
        m_PositionX = positionX;
        m_PositionY = positionY;
        m_PositionZ = positionZ;
    }

    public float getRotationX() {
        return m_RotationX;
    }

    public float getRotationY() {
        return m_RotationY;
    }

    public float getRotationZ() {
        return m_RotationZ;
    }

    public void setRotationX(float rotationX) {
        m_RotationX = rotationX;
    }

    public void setRotationY(float rotationY) {
        m_RotationY = rotationY;
    }

    public void setRotationZ(float rotationZ) {
        m_RotationZ = rotationZ;
    }

    public void setRotation(float rotationX, float rotationY) {
        m_RotationX = rotationX;
        m_RotationY = rotationY;
    }

    public void setRotation(float rotationX, float rotationY, float rotationZ) {
        m_RotationX = rotationX;
        m_RotationY = rotationY;
        m_RotationZ = rotationZ;
    }

    public float getScaleX() {
        return m_ScaleX;
    }

    public float getScaleY() {
        return m_ScaleY;
    }

    public float getScaleZ() {
        return m_ScaleZ;
    }

    public float getHalfScaleX() {
        return m_HalfScaleX;
    }

    public float getHalfScaleY() {
        return m_HalfScaleY;
    }

    public float getHalfScaleZ() {
        return m_HalfScaleZ;
    }

    public void setScaleX(float scaleX) {
        m_ScaleX = scaleX;
        m_HalfScaleX = scaleX / 2f;
    }

    public void setScaleY(float scaleY) {
        m_ScaleY = scaleY;
        m_HalfScaleY = scaleY / 2f;
    }

    public void setScaleZ(float scaleZ) {
        m_ScaleZ = scaleZ;
        m_HalfScaleZ = scaleZ / 2f;
    }

    public void setScale(float scaleX, float scaleY) {
        m_ScaleX = scaleX;
        m_ScaleY = scaleY;
        m_HalfScaleX = scaleX / 2f;
        m_HalfScaleY = scaleY / 2f;
    }

    public void setScale(float scaleX, float scaleY, float scaleZ) {
        m_ScaleX = scaleX;
        m_ScaleY = scaleY;
        m_ScaleZ = scaleZ;
        m_HalfScaleX = scaleX / 2f;
        m_HalfScaleY = scaleY / 2f;
        m_HalfScaleZ = scaleZ / 2f;
    }
}
