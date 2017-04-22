package at.autrage.projects.zeta.opengl;

import at.autrage.projects.zeta.module.AssetManager;

public class SpriteMaterial extends ColorMaterial {
    /** Texture attribute used by the update thread. */
    private Texture m_Texture;

    public SpriteMaterial() {
        super(AssetManager.getInstance().getSpriteShader());
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
