package at.autrage.projects.zeta.model;

import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.List;

import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.exception.ArgumentNullException;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.ui.TouchDown;
import at.autrage.projects.zeta.ui.TouchEvent;
import at.autrage.projects.zeta.ui.TouchMove;
import at.autrage.projects.zeta.ui.TouchUp;
import at.autrage.projects.zeta.view.GameView;

/**
 * This class represents an object in the game.
 */
public final class GameObject {
    public enum Layer {
        None,
        UI,
        GameView
    }

    private GameView m_GameView;

    /**
     * The model matrix transforms a position in a model to the position in the world.
     */
    private float[] modelMatrix;
    /**
     * The rotation/translation matrix cache.
     */
    private float[] RTMatrix;
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

    private float localRotationX;
    private float localRotationY;
    private float localRotationZ;

    private float scaleX;
    private float scaleY;
    private float scaleZ;

    private float halfScaleX;
    private float halfScaleY;
    private float halfScaleZ;

    private Layer layer;

    private GameObject parent;
    private List<GameObject> children;

    private boolean ignoreParentPosition;
    private boolean ignoreParentRotation;

    protected List<Component> components;
    private int currComponentIdx;

    public GameObject(GameView gameView, float positionX, float positionY) {
        this(gameView, positionX, positionY, Layer.GameView);
    }

    public GameObject(GameView gameView, float positionX, float positionY, Layer layer) {
        m_GameView = gameView;

        modelMatrix = new float[16];
        RTMatrix = new float[16];
        TRMatrix = new float[16];
        translationMatrix = new float[16];
        rotationMatrix = new float[16];
        scaleMatrix = new float[16];

        this.layer = layer;

        parent = null;
        children = new ArrayList<>();

        setPosition(positionX, positionY);

        components = new ArrayList<>();
        currComponentIdx = -1;

        if (m_GameView != null) {
            m_GameView.addGameObject(this);
        }
    }

    public Layer getLayer() {
        return layer;
    }

    public boolean addComponent(Component component) {
        if (component == null) {
            throw new ArgumentNullException();
        }

        return components.add(component);
    }

    public boolean removeComponent(Component component) {
        if (component == null) {
            throw new ArgumentNullException();
        }

        if (currComponentIdx > -1) {
            int componentIndex = components.indexOf(component);
            if (componentIndex > -1 && componentIndex <= currComponentIdx) {
                currComponentIdx--;
                return components.remove(componentIndex) != null;
            }
        }
        else {
            return components.remove(component);
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
        for (currComponentIdx = 0; currComponentIdx < components.size(); currComponentIdx++) {
            components.get(currComponentIdx).update();
        }

        currComponentIdx = -1;

        // Update translation matrix
        Matrix.setIdentityM(translationMatrix, 0);
        Matrix.setIdentityM(rotationMatrix, 0);
        Matrix.setIdentityM(scaleMatrix, 0);

        if (rotationX != 0f) {
            Matrix.rotateM(rotationMatrix, 0, rotationX, 1f, 0f, 0f);
        }
        if (rotationY != 0f) {
            Matrix.rotateM(rotationMatrix, 0, rotationY, 0f, 1f, 0f);
        }
        if (rotationZ != 0f) {
            Matrix.rotateM(rotationMatrix, 0, rotationZ, 0f, 0f, 1f);
        }

        if (parent != null) {
            Matrix.translateM(translationMatrix, 0, localPositionX, localPositionY, localPositionZ);
            Matrix.multiplyMM(RTMatrix, 0, rotationMatrix, 0, translationMatrix, 0);
            Matrix.setIdentityM(translationMatrix, 0);
            Matrix.translateM(translationMatrix, 0, parent.getPositionX(), parent.getPositionY(), parent.getPositionZ());
            Matrix.multiplyMM(TRMatrix, 0, translationMatrix, 0, RTMatrix, 0);

        } else {
            Matrix.translateM(translationMatrix, 0, positionX, positionY, positionZ);
            Matrix.multiplyMM(TRMatrix, 0, translationMatrix, 0, rotationMatrix, 0);
        }

        Matrix.scaleM(scaleMatrix, 0, scaleX, scaleY, scaleZ);
        Matrix.multiplyMM(modelMatrix, 0, TRMatrix, 0, scaleMatrix, 0);

        // Check for lost objects and destroy them
        if (Math.abs(positionX) >= Pustafin.GameObjectAutoDestroyDistance ||
                Math.abs(positionY) >= Pustafin.GameObjectAutoDestroyDistance) {
            Logger.D("Auto destroyed game object due to distance from planet.");
            destroy();
        }
    }

    public void onCollide(Collider other) {
        for (currComponentIdx = 0; currComponentIdx < components.size(); currComponentIdx++) {
            Component component = components.get(currComponentIdx);
            component.collide(other);
        }

        currComponentIdx = -1;
    }

    public void touchDown(Collider collider, TouchEvent e) {
        for (currComponentIdx = 0; currComponentIdx < components.size(); currComponentIdx++) {
            Component component = components.get(currComponentIdx);
            if (component instanceof TouchDown){
                ((TouchDown)component).touchDown(collider, e);
            }
        }

        currComponentIdx = -1;
    }

    public void touchUp(Collider collider, TouchEvent e) {
        for (currComponentIdx = 0; currComponentIdx < components.size(); currComponentIdx++) {
            Component component = components.get(currComponentIdx);
            if (component instanceof TouchUp){
                ((TouchUp)component).touchUp(collider, e);
            }
        }

        currComponentIdx = -1;
    }

    public void touchMove(Collider collider, TouchEvent e) {
        for (currComponentIdx = 0; currComponentIdx < components.size(); currComponentIdx++) {
            Component component = components.get(currComponentIdx);
            if (component instanceof TouchMove){
                ((TouchMove)component).touchMove(collider, e);
            }
        }

        currComponentIdx = -1;
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

        if (parent != null && !ignoreParentPosition) {
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

        if (parent != null && !ignoreParentPosition) {
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

        if (parent != null && !ignoreParentPosition) {
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

        if (parent != null && !ignoreParentPosition) {
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

        if (parent != null && !ignoreParentPosition) {
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

        if (parent != null && !ignoreParentPosition) {
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

        if (parent != null && !ignoreParentRotation) {
            localRotationX = this.rotationX - parent.getRotationX();
        } else {
            localRotationX = this.rotationX;
        }

        GameObject child = null;
        for (int i = 0, size = children.size(); i < size; i++) {
            child = children.get(i);
            child.setLocalRotationX(child.getLocalRotationX());
        }
    }

    public void setRotationY(float rotationY) {
        this.rotationY = rotationY;

        if (parent != null && !ignoreParentRotation) {
            localRotationY = this.rotationY - parent.getRotationY();
        } else {
            localRotationY = this.rotationY;
        }

        GameObject child = null;
        for (int i = 0, size = children.size(); i < size; i++) {
            child = children.get(i);
            child.setLocalRotationY(child.getLocalRotationY());
        }
    }

    public void setRotationZ(float rotationZ) {
        this.rotationZ = rotationZ;

        if (parent != null && !ignoreParentRotation) {
            localRotationZ = this.rotationZ - parent.getRotationZ();
        } else {
            localRotationZ = this.rotationZ;
        }

        GameObject child = null;
        for (int i = 0, size = children.size(); i < size; i++) {
            child = children.get(i);
            child.setLocalRotationZ(child.getLocalRotationZ());
        }
    }

    public void setRotation(float rotationX, float rotationY, float rotationZ) {
        setRotationX(rotationX);
        setRotationY(rotationY);
        setRotationZ(rotationZ);
    }

    public float getLocalRotationX() {
        return localRotationX;
    }

    public float getLocalRotationY() {
        return localRotationY;
    }

    public float getLocalRotationZ() {
        return localRotationZ;
    }

    public void setLocalRotationX(float localRotationX) {
        this.localRotationX = localRotationX;

        if (parent != null && !ignoreParentRotation) {
            rotationX = parent.getRotationX() + this.localRotationX;
        } else {
            rotationX = this.localRotationX;
        }

        GameObject child = null;
        for (int i = 0, size = children.size(); i < size; i++) {
            child = children.get(i);
            child.setLocalRotationX(child.getLocalRotationX());
        }
    }

    public void setLocalRotationY(float localRotationY) {
        this.localRotationY = localRotationY;

        if (parent != null && !ignoreParentRotation) {
            rotationY = parent.getRotationY() + this.localRotationY;
        } else {
            rotationY = this.localRotationY;
        }

        GameObject child = null;
        for (int i = 0, size = children.size(); i < size; i++) {
            child = children.get(i);
            child.setLocalRotationY(child.getLocalRotationY());
        }
    }

    public void setLocalRotationZ(float localRotationZ) {
        this.localRotationZ = localRotationZ;

        if (parent != null && !ignoreParentRotation) {
            rotationZ = parent.getRotationZ() + this.localRotationZ;
        } else {
            rotationZ = this.localRotationZ;
        }

        GameObject child = null;
        for (int i = 0, size = children.size(); i < size; i++) {
            child = children.get(i);
            child.setLocalRotationZ(child.getLocalRotationZ());
        }
    }

    public void setLocalRotation(float localRotationX, float localRotationY, float localRotationZ) {
        setLocalRotationX(localRotationX);
        setLocalRotationY(localRotationY);
        setLocalRotationZ(localRotationZ);
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

        // Update local rotation
        gameObject.setLocalRotation(
                rotationX - gameObject.getRotationX(),
                rotationY - gameObject.getRotationY(),
                rotationZ - gameObject.getRotationZ()
        );
    }

    public void removeChild(GameObject gameObject) {
        if (!children.remove(gameObject)) {
            return;
        }

        gameObject.parent = null;

        // Update position
        gameObject.setPosition(gameObject.getPositionX(), gameObject.getPositionY(), gameObject.getPositionZ());

        // Update rotation
        gameObject.setRotation(gameObject.getPositionX(), gameObject.getRotationY(), gameObject.getRotationZ());
    }

    public GameObject getChild(int index) {
        return children.get(index);
    }

    public int getChildCount() {
        return children.size();
    }

    public boolean isIgnoreParentPosition() {
        return ignoreParentPosition;
    }

    public void setIgnoreParentPosition(boolean ignoreParentPosition) {
        this.ignoreParentPosition = ignoreParentPosition;
    }

    public boolean isIgnoreParentRotation() {
        return ignoreParentRotation;
    }

    public void setIgnoreParentRotation(boolean ignoreParentRotation) {
        this.ignoreParentRotation = ignoreParentRotation;
    }

    public void destroy() {
        for (int i = 0, size = children.size(); i < size; i++) {
            children.get(i).destroy();
        }

        for (int i = 0; i < components.size(); i++) {
            components.get(i).destroy();
        }

        if (m_GameView != null) {
            m_GameView.removeGameObject(this);
        }
    }
}
