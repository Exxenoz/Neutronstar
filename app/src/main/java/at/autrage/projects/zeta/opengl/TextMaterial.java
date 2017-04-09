package at.autrage.projects.zeta.opengl;

import at.autrage.projects.zeta.module.AssetManager;

public class TextMaterial extends Material {
    /** Color attribute used by the update thread. */
    private Color m_Color;
    /** Texture attribute used by the update thread. */
    private Texture m_Texture;

    public TextMaterial() {
        super(AssetManager.getInstance().getTextShader());

        m_Color = new Color();
    }

    @Override
    public void shift(ShaderParams shaderParams) {
        shaderParams.Color.setColor(m_Color);
        shaderParams.TextureDataHandle = m_Texture != null ? m_Texture.getTextureDataHandle() : 0;
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

    public void setTexture(Texture texture) {
        m_Texture = texture;
    }
}
