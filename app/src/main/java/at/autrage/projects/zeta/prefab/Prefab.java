package at.autrage.projects.zeta.prefab;

import at.autrage.projects.zeta.view.GameView;

public abstract class Prefab {
    public final String Name;
    public final GameView GameView;

    public Prefab(String name, GameView gameView) {
        this.Name = name;
        this.GameView = gameView;
    }

    public abstract void destroy();
}
