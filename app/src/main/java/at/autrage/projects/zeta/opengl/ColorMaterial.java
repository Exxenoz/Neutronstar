package at.autrage.projects.zeta.opengl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import at.autrage.projects.zeta.module.AssetManager;

public class ColorMaterial extends Material {
    /** Color attribute used by the main thread. */
    private Color m_Color;
    /** Color attribute used by the render thread. */
    private Color _color;

    public ColorMaterial() {
        super(AssetManager.getInstance().getColorShader());

        m_Color = new Color();
        _color = new Color();
    }

    @Override
    public void shift() {
        _color.setColor(m_Color);
    }

    @Override
    public void draw(FloatBuffer vertices, ShortBuffer indices, int indexCount, float[] modelMatrix, float[] vpMatrix) {
        _shader.draw(vertices, indices, indexCount, _color.getColor(), modelMatrix, vpMatrix);
    }

    public Color getColor() {
        return m_Color;
    }
}
