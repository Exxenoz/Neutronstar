package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.opengl.Color;
import at.autrage.projects.zeta.opengl.ColorMaterial;
import at.autrage.projects.zeta.opengl.MeshRenderer;
import at.autrage.projects.zeta.opengl.SpriteMesh;

public class HealthBar extends Component {
    private static SpriteMesh spriteMesh = new SpriteMesh();
    private ColorMaterial m_ColorMaterial;
    private float m_FullWidth;
    private float m_FullHeight;
    private float m_HealthPercent;
    private MeshRenderer meshRenderer;

    public HealthBar(GameObject gameObject) {
        super(gameObject);

        m_ColorMaterial = new ColorMaterial();
        m_FullWidth = 0f;
        m_FullHeight = 0f;
        setHealthPercent(0f);
    }

    @Override
    protected void onStart() {
        meshRenderer = gameObject.addComponent(MeshRenderer.class);
        meshRenderer.setMaterial(m_ColorMaterial);
        meshRenderer.setMesh(spriteMesh);
    }

    private void updateScale() {
        gameObject.setScale(m_FullWidth * m_HealthPercent, m_FullHeight);
    }

    public void setFullHeight(float fullHeight) {
        this.m_FullHeight = fullHeight;
        updateScale();
    }

    public void setFullWidth(float fullWidth) {
        this.m_FullWidth = fullWidth;
        updateScale();
    }

    public void setHealthPercent(float healthPercent) {
        if (healthPercent < 0f) {
            healthPercent = 0f;
        }

        if (healthPercent > 1f) {
            healthPercent = 1f;
        }

        if (healthPercent < Pustafin.EnemyHealthBarMinPercentageColorOrange) {
            m_ColorMaterial.setColor(Color.Red);
        }
        else if (healthPercent < Pustafin.EnemyHealthBarMinPercentageColorGreen) {
            m_ColorMaterial.setColor(255f / 255f, 200f / 255f, 0f);
        }
        else {
            m_ColorMaterial.setColor(Color.Green);
        }

        m_HealthPercent = healthPercent;

        updateScale();
    }

    @Override
    protected void onDestroy() {
        if (meshRenderer != null) {
            meshRenderer.destroy();
        }
    }
}
