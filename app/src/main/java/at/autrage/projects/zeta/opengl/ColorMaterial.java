package at.autrage.projects.zeta.opengl;

import at.autrage.projects.zeta.module.AssetManager;

public class ColorMaterial extends Material {
    /** Color attribute used by the main thread. */
    private Color m_Color;

    public ColorMaterial() {
        super(AssetManager.getInstance().getColorShader());

        m_Color = new Color();
    }

    @Override
    public void shift(ShaderParams shaderParams) {
        shaderParams.Color.setColor(m_Color);
    }

    public Color getColor() {
        return m_Color;
    }
}
