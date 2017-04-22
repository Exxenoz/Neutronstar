package at.autrage.projects.zeta.opengl;

import at.autrage.projects.zeta.module.AssetManager;

public class TextMaterial extends ColorMaterial {
    /** Texture attribute used by the update thread. */
    private Texture m_Texture;

    public TextMaterial() {
        super(AssetManager.getInstance().getTextShader());
    }

    @Override
    public void shift(ShaderParams shaderParams) {
        super.shift(shaderParams);
        shaderParams.TextureDataHandle = m_Texture != null ? m_Texture.getTextureDataHandle() : 0;
    }

    public void setTexture(Texture texture) {
        m_Texture = texture;
    }
}
