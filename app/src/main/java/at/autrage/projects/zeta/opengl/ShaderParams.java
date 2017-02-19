package at.autrage.projects.zeta.opengl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class ShaderParams {
    public boolean Enabled;

    public Material Material;
    public Mesh Mesh;

    public FloatBuffer Vertices;
    public ShortBuffer Indices;
    public int IndexCount;

    public Color Color = new Color();

    public int TextureDataHandle;
    public FloatBuffer TextureCoordinates;

    public float[] VPMatrix;
    public float[] ModelMatrix = new float[16];
}
