package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.opengl.Color;
import at.autrage.projects.zeta.opengl.ColorMaterial;
import at.autrage.projects.zeta.opengl.MeshRenderer;
import at.autrage.projects.zeta.opengl.SpriteMesh;
import at.autrage.projects.zeta.view.GameView;

public class HealthBar extends Component {
    private ColorMaterial m_ColorMaterial;
    private float m_FullWidth;
    private float m_FullHeight;
    private float m_HealthPercent;
    private MeshRenderer meshRenderer;

    public HealthBar(GameObject gameObject, float width, float height) {
        super(gameObject);

        m_ColorMaterial = new ColorMaterial();
        m_FullWidth = width;
        m_FullHeight = height;
        m_HealthPercent = 1f;

        updateScale();

        meshRenderer = new MeshRenderer(gameObject);
        meshRenderer.setMaterial(m_ColorMaterial);
        meshRenderer.setMesh(new SpriteMesh());
        meshRenderer.setEnabled(true);
    }

    private void updateScale() {
        gameObject.setScale(m_FullWidth * m_HealthPercent, m_FullHeight);
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
    }
}
