package at.autrage.projects.zeta.opengl;

import at.autrage.projects.zeta.module.AssetManager;

public class ColorMaterial extends Material {
    /** Color attribute used by the main thread. */
    private Color m_Color;

    public ColorMaterial() {
        this(AssetManager.getInstance().getColorShader());
    }

    public ColorMaterial(Shader shader) {
        super(shader);

        m_Color = new Color();
    }

    @Override
    public void shift(ShaderParams shaderParams) {
        shaderParams.Color.setColor(m_Color);
    }

    public void setColor(Color color) {
        m_Color.setColor(color);
    }

    public void setColor(float r, float g, float b) {
        m_Color.setColor(r, g, b);
    }

    public Color getColor() {
        return m_Color;
    }

    public void setAlpha(float alpha) {
        m_Color.setAlpha(alpha);
    }

    public float getAlpha() {
        return m_Color.getAlpha();
    }
}
