package at.autrage.projects.zeta.opengl;

import at.autrage.projects.zeta.module.AssetManager;

public class SpriteMaterial extends Material {
    private Color m_Color;

    public SpriteMaterial() {
        super(AssetManager.getInstance().getSpriteShader());

        m_Color = new Color();
    }

    public void draw(Mesh mesh, float[] mvpMatrix) {
        m_Shader.draw(mesh, m_Color.getColor(), mvpMatrix);
    }
}
