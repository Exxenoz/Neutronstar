package at.autrage.projects.zeta.opengl;

import at.autrage.projects.zeta.model.Component;
import at.autrage.projects.zeta.model.GameObject;
import at.autrage.projects.zeta.model.Transform;
import at.autrage.projects.zeta.module.Logger;

public class MeshRenderer extends Component {
    /** The transform object used by the mesh renderer. */
    private Transform m_Transform;
    /** The reference to the {@link Material} that is used. */
    private Material m_Material;
    /** The reference to the {@link Mesh} that is used. */
    private Mesh m_Mesh;

    /** The reference to the {@link ShaderParams} object. */
    private ShaderParams _shaderParams;

    public MeshRenderer(GameObject gameObject) {
        super(gameObject);

        m_Transform = gameObject.getTransform();
        m_Material = null;
        m_Mesh = null;

        _shaderParams = new ShaderParams();
    }

    @Override
    protected void onEnable() {
        m_Transform.getOwner().getGameView().addMeshRendererToInsertQueue(this);
    }

    @Override
    protected void onDisable() {
        m_Transform.getOwner().getGameView().addMeshRendererToDeleteQueue(this);
    }

    public void shift() {
        _shaderParams.Enabled = isEnabled();

        _shaderParams.Material = m_Material;
        if (_shaderParams.Material != null) {
            _shaderParams.Material.shift(_shaderParams);
        }

        _shaderParams.Mesh = m_Mesh;
        if (_shaderParams.Mesh != null) {
            _shaderParams.Mesh.shift(_shaderParams);
        }

        System.arraycopy(m_Transform.getModelMatrix(), 0, _shaderParams.ModelMatrix, 0, 16);
    }

    public void draw(float[] vpMatrix) {
        if (!_shaderParams.Enabled) {
            return;
        }

        if (_shaderParams.Material == null) {
            Logger.W("Could not draw mesh, because material member is null.");
            return;
        }

        if (_shaderParams.Mesh == null) {
            Logger.W("Could not draw mesh, because mesh member is null.");
            return;
        }

        // Copy of reference is OK, because
        // the matrix is only accessible by
        // the render thread.
        _shaderParams.VPMatrix = vpMatrix;

        _shaderParams.Material._shader.draw(_shaderParams);
    }

    public void setMaterial(Material material) {
        m_Material = material;
    }

    public void setMesh(Mesh mesh) {
        m_Mesh = mesh;
    }

    public Transform getTransform() {
        return m_Transform;
    }

    public Material getMaterial() {
        return m_Material;
    }

    public Mesh getMesh() {
        return m_Mesh;
    }
}
