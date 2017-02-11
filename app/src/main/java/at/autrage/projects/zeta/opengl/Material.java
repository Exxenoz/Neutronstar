package at.autrage.projects.zeta.opengl;

public abstract class Material {
    protected Shader m_Shader;

    public Material(Shader shader) {
        m_Shader = shader;
    }

    public Shader getShader() {
        return m_Shader;
    }

    public abstract void draw(Mesh mesh, float[] mvpMatrix);
}
