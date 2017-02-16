package at.autrage.projects.zeta.opengl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public abstract class Material {
    protected Shader _shader;

    public Material(Shader shader) {
        _shader = shader;
    }

    public abstract void shift();
    public abstract void draw(FloatBuffer vertices, ShortBuffer indices, int indexCount, float[] mvpMatrix);

    public Shader getShader() {
        return _shader;
    }
}
