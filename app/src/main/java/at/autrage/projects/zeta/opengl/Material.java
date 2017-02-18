package at.autrage.projects.zeta.opengl;

public abstract class Material {
    protected Shader _shader;

    public Material(Shader shader) {
        _shader = shader;
    }

    public abstract void shift();
    public abstract void draw(ShaderParams shaderParams);
}
