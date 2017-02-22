package at.autrage.projects.zeta.model;

import java.util.ArrayList;
import java.util.List;

import at.autrage.projects.zeta.ability.Behaviour;
import at.autrage.projects.zeta.collision.Collider;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.opengl.MeshRenderer;
import at.autrage.projects.zeta.view.GameView;

/**
 * This class represents an object in the game.
 */
public abstract class GameObject {
    private GameView m_GameView;

    protected Transform m_Transform;

    private float m_DirectionX;
    private float m_DirectionY;
    private float m_SpeedX;
    private float m_SpeedY;
    private float m_Speed;

    private MeshRenderer m_Renderer;

    private boolean m_Visible;

    private List<Component> components;

    public GameObject(GameView gameView, float positionX, float positionY) {
        m_GameView = gameView;

        m_Transform = new Transform(this);
        m_Transform.setPosition(positionX, positionY);

        m_DirectionX = 0f;
        m_DirectionY = 0f;
        setSpeed(0f);

        m_Renderer = null;

        m_Visible = true;

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
            m_Transform.setPosition(
                    m_Transform.getPositionX() + m_SpeedX * Time.getScaledDeltaTime(),
                    m_Transform.getPositionY() + m_SpeedY * Time.getScaledDeltaTime()
            );
        }

        for (int i = 0; i < components.size(); i++) {
            components.get(i).update();
        }

        m_Transform.update();

        // Check for lost objects and destroy them
        if (Math.abs(m_Transform.getPositionX()) >= Pustafin.GameObjectAutoDestroyDistance ||
                Math.abs(m_Transform.getPositionY()) >= Pustafin.GameObjectAutoDestroyDistance) {
            Logger.D("Auto destroyed game object due to distance from planet.");
            destroy();
        }
    }

    public void onCollide(Collider collider) {
    }

    public GameView getGameView() {
        return m_GameView;
    }

    public Transform getTransform() {
        return m_Transform;
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

    public MeshRenderer getRenderer() {
        return m_Renderer;
    }

    public void setRenderer(MeshRenderer renderer) {
        if (m_Renderer != null && m_Renderer != renderer) {
            m_Renderer.setEnabled(false);
        }

        m_Renderer = renderer;
    }

    public boolean isVisible() {
        return m_Visible;
    }

    public void setVisible(boolean visible) {
        this.m_Visible = visible;
    }

    public void destroy() {
        Transform child = null;
        for (int i = 0, size = m_Transform.getChildCount(); i < size; i++) {
            child = m_Transform.getChild(i);
            child.getOwner().destroy();
        }

        while (components.size() > 0) {
            components.get(0).destroy();
        }

        if (m_GameView != null) {
            m_GameView.addGameObjectToDeleteQueue(this);
        }

        setRenderer(null);
    }
}
