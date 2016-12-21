package at.autrage.projects.zeta.model;


import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.view.GameView;

public class EnemySpawner extends GameObject {
    private Player m_Player;

    public EnemySpawner(GameView gameView, float positionX, float positionY, AnimationSet animationSet) {
        super(gameView, positionX, positionY, animationSet);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();


    }

    public Player getPlayer() {
        return m_Player;
    }

    public void setPlayer(Player player) {
        this.m_Player = player;
    }
}
