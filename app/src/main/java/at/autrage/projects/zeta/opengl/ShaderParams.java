package at.autrage.projects.zeta.opengl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class ShaderParams {
    public boolean Enabled;
    public Shader Shader;

    public FloatBuffer VertexBuffer;
    public ShortBuffer IndexBuffer;
    public int IndexBufferSize;

    public Color Color = new Color();

    public int TextureDataHandle;
    public FloatBuffer TextureCoordinates;

    public float[] VPMatrix;
    public float[] ModelMatrix = new float[16];
}
