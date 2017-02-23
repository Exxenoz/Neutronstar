package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.opengl.Color;
import at.autrage.projects.zeta.opengl.ColorMaterial;
import at.autrage.projects.zeta.opengl.MeshRenderer;
import at.autrage.projects.zeta.opengl.SpriteMesh;
import at.autrage.projects.zeta.view.GameView;

public class HealthBar extends GameObject {
    private GameObject owner;
    private ColorMaterial m_ColorMaterial;
    private float m_FullWidth;
    private float m_FullHeight;
    private float m_HealthPercent;

    public HealthBar(GameView gameView, GameObject owner, float width, float height) {
        super(gameView, 0f, 0f);

        this.owner = owner;
        m_ColorMaterial = new ColorMaterial();
        m_FullWidth = width;
        m_FullHeight = height;
        m_HealthPercent = 1f;

        updateScale();
        updatePosition();

        MeshRenderer meshRenderer = new MeshRenderer(this);
        meshRenderer.setMaterial(m_ColorMaterial);
        meshRenderer.setMesh(new SpriteMesh());
        meshRenderer.setEnabled(true);
        addComponent(meshRenderer);
    }

    private void updatePosition() {
        setPosition(
                owner.getPositionX() + Pustafin.EnemyHealthBarOffsetX - (m_FullWidth - getScaleX()) / 2f,
                owner.getPositionY() + owner.getHalfScaleY() + Pustafin.EnemyHealthBarHalfHeight + Pustafin.EnemyHealthBarOffsetY
        );
    }

    private void updateScale() {
        setScale(m_FullWidth * m_HealthPercent, m_FullHeight);
    }

    @Override
    public void onUpdate() {
        updatePosition();

        super.onUpdate();
    }

    public void setHealthPercent(float healthPercent) {
        if (healthPercent < 0f) {
            healthPercent = 0f;
        }

        if (healthPercent > 1f) {
            healthPercent = 1f;
        }

        if (healthPercent < Pustafin.EnemyHealthBarMinPercentageColorOrange) {
            m_ColorMaterial.getColor().setColor(Color.Red);
        }
        else if (healthPercent < Pustafin.EnemyHealthBarMinPercentageColorGreen) {
            m_ColorMaterial.getColor().setColor(255f / 255f, 200f / 255f, 0f, 1f);
        }
        else {
            m_ColorMaterial.getColor().setColor(Color.Green);
        }

        m_HealthPercent = healthPercent;

        updateScale();
        updatePosition();
    }
}
