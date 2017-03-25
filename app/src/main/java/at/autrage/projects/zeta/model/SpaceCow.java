package at.autrage.projects.zeta.model;


import at.autrage.projects.zeta.animation.AnimationSet;
import at.autrage.projects.zeta.animation.AnimationSets;
import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.view.GameView;

public class SpaceCow extends Enemy {
    public SpaceCow(EnemySpawner spawner, float health, float hitDamage, int bounty, int points) {
        super(spawner, health, hitDamage, bounty, points);
    }
}
