package at.autrage.projects.zeta.opengl;

import java.util.concurrent.ConcurrentLinkedQueue;

import at.autrage.projects.zeta.model.Component;
import at.autrage.projects.zeta.model.GameObject;

public abstract class Renderer extends Component {
    private int drawOrderID;
    public ConcurrentLinkedQueue<Renderer> Holder;

    public Renderer(GameObject gameObject) {
        super(gameObject);

        Holder = null;
    }

    @Override
    protected void onEnable() {
        gameObject.getGameView().addRenderer(this, drawOrderID);
    }

    @Override
    protected void onDisable() {
        gameObject.getGameView().removeRenderer(this);
    }

    @Override
    protected void onDestroy() {
        gameObject.getGameView().removeRenderer(this);
    }

    public int getDrawOrderID() {
        return drawOrderID;
    }

    public void setDrawOrderID(int drawOrderID) {
        this.drawOrderID = drawOrderID;

        if (isEnabled()) {
            gameObject.getGameView().removeRenderer(this);
            gameObject.getGameView().addRenderer(this, drawOrderID);
        }
    }

    public abstract void lateUpdate();
    public abstract void draw(float[] vpMatrix);
}
