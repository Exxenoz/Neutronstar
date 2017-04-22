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

    public void setColor(float r, float g, float b) {
        setColor(r, g, b, m_Color[3]);
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
        if (alpha < 0f) {
            alpha = 0f;
        }
        else if (alpha > 1f) {
            alpha = 1f;
        }

        m_Color[3] = alpha;
    }

    public float getAlpha() {
        return m_Color[3];
    }

    public static final Color Red   = new Color(1f, 0f, 0f, 1f);
    public static final Color Green = new Color(0f, 1f, 0f, 1f);
    public static final Color Blue  = new Color(0f, 0f, 1f, 1f);
    public static final Color Black = new Color(0f, 0f, 0f, 1f);
    public static final Color White = new Color(1f, 1f, 1f, 1f);
}
