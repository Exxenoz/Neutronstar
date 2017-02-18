package at.autrage.projects.zeta.opengl;

import java.nio.FloatBuffer;

import at.autrage.projects.zeta.module.AssetManager;

public class SpriteMaterial extends Material {
    /** Color attribute used by the update thread. */
    private Color m_Color;
    /** Texture data handle attribute used by the update thread. */
    private int m_TextureDataHandle;
    /** Texture coordinates attribute used by the update thread. */
    private FloatBuffer m_TextureCoordinates;

    /** Color attribute used by the render thread. */
    private Color _color;
    /** Texture data handle attribute used by the render thread. */
    private int _textureDataHandle;
    /** Texture coordinates attribute used by the render thread. */
    private FloatBuffer _textureCoordinates;

    public SpriteMaterial() {
        super(AssetManager.getInstance().getSpriteShader());

        m_Color = new Color();
        _color = new Color();
    }

    @Override
    public void shift() {
        _color.setColor(m_Color);
        _textureDataHandle = m_TextureDataHandle;
        _textureCoordinates = m_TextureCoordinates;
    }

    @Override
    public void draw(ShaderParams shaderParams) {
        shaderParams.Color = _color.getColor();
        shaderParams.TextureDataHandle = _textureDataHandle;
        shaderParams.TextureCoordinates = _textureCoordinates;

        _shader.draw(shaderParams);
    }

    public void setColor(Color color) {
        m_Color = color;
    }

    public Color getColor() {
        return m_Color;
    }

    public void setTextureDataHandle(int textureDataHandle) {
        m_TextureDataHandle = textureDataHandle;
    }

    public void setTextureCoordinates(FloatBuffer textureCoordinates) {
        m_TextureCoordinates = textureCoordinates;
    }
}
