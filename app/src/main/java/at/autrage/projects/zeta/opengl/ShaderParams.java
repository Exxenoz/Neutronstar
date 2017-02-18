package at.autrage.projects.zeta.opengl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class ShaderParams {
    public FloatBuffer Vertices;
    public ShortBuffer Indices;
    public int IndexCount;

    public float[] Color;

    public int TextureDataHandle;
    public FloatBuffer TextureCoordinates;

    public float[] VPMatrix;
    public float[] ModelMatrix;
}
