package at.autrage.projects.zeta.opengl;

public class ShaderParams {
    public boolean Enabled;
    public Shader Shader;

    public Mesh Mesh;

    public Color Color = new Color();
    public int TextureDataHandle;

    public float[] VPMatrix;
    public float[] ModelMatrix = new float[16];
}
