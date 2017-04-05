package at.autrage.projects.zeta.opengl;

public class Color {
    private float[] m_Color;

    public Color() {
        m_Color = new float[] { 1f, 1f, 1f, 1f };
    }

    public Color(float r, float g, float b, float a) {
        m_Color = new float[] { r, g, b, a };
    }

    public void setColor(float r, float g, float b, float a) {
        m_Color[0] = r;
        m_Color[1] = g;
        m_Color[2] = b;
        m_Color[3] = a;
    }

    public void setColor(float[] color) {
        setColor(color[0], color[1], color[2], color[3]);
    }

    public void setColor(Color color) {
        setColor(color.getColor());
    }

    public float[] getColor() {
        return m_Color;
    }

    public void setAlpha(float alpha) {
        m_Color[3] = alpha;
    }

    public float getAlpha() {
        return m_Color[3];
    }

    public static final float[] Red   = new float[] { 1f, 0f, 0f, 1f };
    public static final float[] Green = new float[] { 0f, 1f, 0f, 1f };
    public static final float[] Blue  = new float[] { 0f, 0f, 1f, 1f };
    public static final float[] Black = new float[] { 0f, 0f, 0f, 1f };
    public static final float[] White = new float[] { 1f, 1f, 1f, 1f };
}
