package at.autrage.projects.zeta.model;

import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.List;

import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.view.GameView;

/**
 * This class represents an object in the game.
 */
public abstract class GameObject {
    private GameView m_GameView;

    private float m_DirectionX;
    private float m_DirectionY;
    private float m_SpeedX;
    private float m_SpeedY;
    private float m_Speed;

    /**
     * The model matrix transforms a position in a model to the position in the world.
     */
    private float[] modelMatrix;
    /**
     * The translation/rotation matrix cache.
     */
    private float[] TRMatrix;
    /**
     * The translation matrix translates a position.
     */
    private float[] translationMatrix;
    /**
     * The rotation matrix rotates a position.
     */
    private float[] rotationMatrix;
    /**
     * The scale matrix scales a position.
     */
    private float[] scaleMatrix;

    private float positionX;
    private float positionY;
    private float positionZ;

    private float localPositionX;
    private float localPositionY;
    private float localPositionZ;

    private float rotationX;
    private float rotationY;
    private float rotationZ;

    private float scaleX;
    private float scaleY;
    private float scaleZ;

    private float halfScaleX;
    private float halfScaleY;
    private float halfScaleZ;

    private GameObject parent;
    private List<GameObject> children;

    private List<Component> components;

    public GameObject(GameView gameView, float positionX, float positionY) {
        m_GameView = gameView;

        modelMatrix = new float[16];
        TRMatrix = new float[16];
        translationMatrix = new float[16];
        rotationMatrix = new float[16];
        scaleMatrix = new float[16];

        parent = null;
        children = new ArrayList<>();

        m_DirectionX = 0f;
        m_DirectionY = 0f;
        setSpeed(0f);

        components = new ArrayList<>();

        if (m_GameView != null) {
            m_GameView.addGameObjectToInsertQueue(this);
        }
    }

    public boolean addComponent(Component component) {
        if (component == null) {
            return false;
        }

        components.add(component);

        return true;
    }

    public boolean removeComponent(Component component) {
        if (components.remove(component)) {
            component.destroy();
            return true;
        }

        return false;
    }

    public <T> T getComponent(Class<T> componentClass) {
        for (Component component : components) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                return (T) component;
            }
        }

        return null;
    }

    public <T> List<T> getComponents(Class<T> componentClass) {
        ArrayList<T> components = new ArrayList<>();

        for (Component component : this.components) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                components.add((T) component);
            }
        }

        return components;
    }

    public void onUpdate() {
        if (m_Speed != 0f) {
            setPosition(
                    getPositionX() + m_SpeedX * Time.getScaledDeltaTime(),
                    getPositionY() + m_SpeedY * Time.getScaledDeltaTime()
            );
        }

        for (int i = 0; i < components.size(); i++) {
            components.get(i).update();
        }

        // Update translation matrix
        Matrix.setIdentityM(translationMatrix, 0);
        Matrix.translateM(translationMatrix, 0, positionX, positionY, positionZ);
        // Update rotation matrix
        Matrix.setIdentityM(rotationMatrix, 0);

        if (rotationX != 0f) {
            Matrix.rotateM(rotationMatrix, 0, rotationX, 1f, 0f, 0f);
        }

        if (rotationY != 0f) {
            Matrix.rotateM(rotationMatrix, 0, rotationY, 0f, 1f, 0f);
        }

        if (rotationZ != 0f) {
            Matrix.rotateM(rotationMatrix, 0, rotationZ, 0f, 0f, 1f);
        }
        // Update scale matrix
        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.scaleM(scaleMatrix, 0, scaleX, scaleY, scaleZ);
        // Update model matrix
        Matrix.multiplyMM(TRMatrix, 0, translationMatrix, 0, rotationMatrix, 0);
        Matrix.multiplyMM(modelMatrix, 0, TRMatrix, 0, scaleMatrix, 0);

        // Check for lost objects and destroy them
        if (Math.abs(positionX) >= Pustafin.GameObjectAutoDestroyDistance ||
                Math.abs(positionY) >= Pustafin.GameObjectAutoDestroyDistance) {
            Logger.D("Auto destroyed game object due to distance from planet.");
            destroy();
        }
    }

    public void onCollide(Collider other) {
    }

    public GameView getGameView() {
        return m_GameView;
    }

    public float[] getModelMatrix() {
        return modelMatrix;
    }

    public float[] getTranslationMatrix() {
        return translationMatrix;
    }

    public float[] getRotationMatrix() {
        return rotationMatrix;
    }

    public float[] getScaleMatrix() {
        return scaleMatrix;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public float getPositionZ() {
        return positionZ;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;

        if (parent != null) {
            localPositionX = this.positionX - parent.getPositionX();
        } else {
            localPositionX = this.positionX;
        }

        GameObject child = null;
        for (int i = 0, size = children.size(); i < size; i++) {
            child = children.get(i);
            child.setLocalPositionX(child.getLocalPositionX());
        }
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;

        if (parent != null) {
            localPositionY = this.positionY - parent.getPositionY();
        } else {
            localPositionY = this.positionY;
        }

        GameObject child = null;
        for (int i = 0, size = children.size(); i < size; i++) {
            child = children.get(i);
            child.setLocalPositionY(child.getLocalPositionY());
        }
    }

    public void setPositionZ(float positionZ) {
        this.positionZ = positionZ;

        if (parent != null) {
            localPositionZ = this.positionZ - parent.getPositionZ();
        } else {
            localPositionZ = this.positionZ;
        }

        GameObject child = null;
        for (int i = 0, size = children.size(); i < size; i++) {
            child = children.get(i);
            child.setLocalPositionZ(child.getLocalPositionZ());
        }
    }

    public void setPosition(float positionX, float positionY) {
        setPositionX(positionX);
        setPositionY(positionY);
    }

    public void setPosition(float positionX, float positionY, float positionZ) {
        setPositionX(positionX);
        setPositionY(positionY);
        setPositionZ(positionZ);
    }

    public float getLocalPositionX() {
        return localPositionX;
    }

    public float getLocalPositionY() {
        return localPositionY;
    }

    public float getLocalPositionZ() {
        return localPositionZ;
    }

    public void setLocalPositionX(float localPositionX) {
        this.localPositionX = localPositionX;

        if (parent != null) {
            positionX = parent.getPositionX() + this.localPositionX;
        } else {
            positionX = this.localPositionX;
        }

        GameObject child = null;
        for (int i = 0, size = children.size(); i < size; i++) {
            child = children.get(i);
            child.setLocalPositionX(child.getLocalPositionX());
        }
    }

    public void setLocalPositionY(float localPositionY) {
        this.localPositionY = localPositionY;

        if (parent != null) {
            positionY = parent.getPositionY() + this.localPositionY;
        } else {
            positionY = this.localPositionY;
        }

        GameObject child = null;
        for (int i = 0, size = children.size(); i < size; i++) {
            child = children.get(i);
            child.setLocalPositionY(child.getLocalPositionY());
        }
    }

    public void setLocalPositionZ(float localPositionZ) {
        this.localPositionZ = localPositionZ;

        if (parent != null) {
            positionZ = parent.getPositionZ() + this.localPositionZ;
        } else {
            positionZ = this.localPositionZ;
        }

        GameObject child = null;
        for (int i = 0, size = children.size(); i < size; i++) {
            child = children.get(i);
            child.setLocalPositionZ(child.getLocalPositionZ());
        }
    }

    public void setLocalPosition(float localPositionX, float localPositionY) {
        setLocalPositionX(localPositionX);
        setLocalPositionY(localPositionY);
    }

    public void setLocalPosition(float localPositionX, float localPositionY, float localPositionZ) {
        setLocalPositionX(localPositionX);
        setLocalPositionY(localPositionY);
        setLocalPositionZ(localPositionZ);
    }

    public float getRotationX() {
        return rotationX;
    }

    public float getRotationY() {
        return rotationY;
    }

    public float getRotationZ() {
        return rotationZ;
    }

    public void setRotationX(float rotationX) {
        this.rotationX = rotationX;
    }

    public void setRotationY(float rotationY) {
        this.rotationY = rotationY;
    }

    public void setRotationZ(float rotationZ) {
        this.rotationZ = rotationZ;
    }

    public void setRotation(float rotationX, float rotationY) {
        this.rotationX = rotationX;
        this.rotationY = rotationY;
    }

    public void setRotation(float rotationX, float rotationY, float rotationZ) {
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public float getScaleZ() {
        return scaleZ;
    }

    public float getHalfScaleX() {
        return halfScaleX;
    }

    public float getHalfScaleY() {
        return halfScaleY;
    }

    public float getHalfScaleZ() {
        return halfScaleZ;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
        halfScaleX = scaleX / 2f;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
        halfScaleY = scaleY / 2f;
    }

    public void setScaleZ(float scaleZ) {
        this.scaleZ = scaleZ;
        halfScaleZ = scaleZ / 2f;
    }

    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        halfScaleX = scaleX / 2f;
        halfScaleY = scaleY / 2f;
    }

    public void setScale(float scaleX, float scaleY, float scaleZ) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
        halfScaleX = scaleX / 2f;
        halfScaleY = scaleY / 2f;
        halfScaleZ = scaleZ / 2f;
    }

    public void setParent(GameObject gameObject) {
        if (gameObject == this) {
            return;
        }

        if (parent != null) {
            parent.removeChild(this);
        }

        parent = gameObject;

        if (parent != null) {
            parent.addChild(this);
        }
    }

    public GameObject getParent() {
        return parent;
    }

    public void addChild(GameObject gameObject) {
        if (gameObject == null) {
            return;
        }

        if (gameObject == this) {
            return;
        }

        if (gameObject.parent != null) {
            gameObject.parent.removeChild(gameObject);
        }

        gameObject.parent = this;

        children.add(gameObject);

        // Update local position
        gameObject.setLocalPosition(
                positionX - gameObject.getPositionX(),
                positionY - gameObject.getPositionY(),
                positionZ - gameObject.getPositionZ()
        );
    }

    public void removeChild(GameObject gameObject) {
        if (!children.remove(gameObject)) {
            return;
        }

        gameObject.parent = null;

        // Update position
        gameObject.setPosition(gameObject.getPositionX(), gameObject.getPositionY(), gameObject.getPositionZ());
    }

    public GameObject getChild(int index) {
        return children.get(index);
    }

    public int getChildCount() {
        return children.size();
    }

    public float getDirectionX() {
        return m_DirectionX;
    }

    public float getDirectionY() {
        return m_DirectionY;
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

    public float getSpeed() {
        return m_Speed;
    }

    public void setSpeed(float speed) {
        m_Speed = speed;
        m_SpeedX = m_DirectionX * speed;
        m_SpeedY = m_DirectionY * speed;
    }

    public void destroy() {
        GameObject child = null;
        for (int i = 0, size = children.size(); i < size; i++) {
            child = children.get(i);
            child.destroy();
        }

        while (components.size() > 0) {
            components.get(0).destroy();
        }

        if (m_GameView != null) {
            m_GameView.addGameObjectToDeleteQueue(this);
        }
    }
}
