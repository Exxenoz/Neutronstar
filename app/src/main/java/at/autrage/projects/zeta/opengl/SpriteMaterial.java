package at.autrage.projects.zeta.opengl;

import java.nio.FloatBuffer;

import at.autrage.projects.zeta.module.AssetManager;

public class SpriteMaterial extends Material {
    /** Color attribute used by the update thread. */
    private Color m_Color;
    /** Texture attribute used by the update thread. */
    private Texture m_Texture;
    /** Texture coordinates attribute used by the update thread. */
    private FloatBuffer m_TextureCoordinates;

    public SpriteMaterial() {
        super(AssetManager.getInstance().getSpriteShader());

        m_Color = new Color();
    }

    @Override
    public void shift(ShaderParams shaderParams) {
        shaderParams.Color.setColor(m_Color);
        shaderParams.TextureDataHandle = m_Texture != null ? m_Texture.getTextureDataHandle() : 0;
        shaderParams.TextureCoordinates = m_TextureCoordinates;
    }

    public void setColor(Color color) {
        m_Color = color;
    }

    public Color getColor() {
        return m_Color;
    }

    public void setTexture(Texture texture) {
        m_Texture = texture;
    }

    public void setTextureCoordinates(FloatBuffer textureCoordinates) {
        m_TextureCoordinates = textureCoordinates;
    }
}
