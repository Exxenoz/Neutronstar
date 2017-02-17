package at.autrage.projects.zeta.opengl;

import android.opengl.Matrix;

import at.autrage.projects.zeta.model.GameObject;
import at.autrage.projects.zeta.module.Logger;

public class MeshRenderer {
    /** The owner of the mesh renderer. */
    private GameObject m_Owner;
    /** Determines whether the mesh is drawn or not. */
    private boolean m_Enabled;
    /** The reference to the {@link Material} that is used. */
    private Material m_Material;
    /** The reference to the {@link Mesh} that is used. */
    private Mesh m_Mesh;

    /** The model matrix transforms a position in a model to the position in the world. */
    private float[] m_ModelMatrix;
    /** The translation matrix translates a position. */
    private float[] m_TranslationMatrix;
    /** The rotation matrix rotates a position. */
    private float[] m_RotationMatrix;
    /** The scale matrix scales a position. */
    private float[] m_ScaleMatrix;

    /** The same as {@link MeshRenderer#m_Enabled}, but safely accessible from the render thread. */
    private boolean _enabled;
    /** The same as {@link MeshRenderer#m_Material}, but safely accessible from the render thread. */
    private Material _material;
    /** The same as {@link MeshRenderer#m_Mesh}, but safely accessible from the render thread. */
    private Mesh _mesh;
    /** The same as {@link MeshRenderer#m_ModelMatrix}, but safely accessible from the render thread. */
    private float[] _modelMatrix;

    public MeshRenderer(GameObject owner) {
        m_Owner = owner;
        m_Enabled = false;
        m_Material = null;
        m_Mesh = null;

        m_ModelMatrix = new float[16];
        m_TranslationMatrix = new float[16];
        m_RotationMatrix = new float[16];
        m_ScaleMatrix = new float[16];

        _enabled = false;
        _material = null;
        _mesh = null;

        _modelMatrix = new float[16];
    }

    public void shift() {
        _enabled = m_Enabled;

        _material = m_Material;
        if (_material != null) {
            _material.shift();
        }

        _mesh = m_Mesh;

        System.arraycopy(m_ModelMatrix, 0, _modelMatrix, 0, 16);
    }

    public void draw(float[] vpMatrix) {
        if (!_enabled) {
            return;
        }

        if (_material == null) {
            Logger.W("Could not draw mesh, because material member is null.");
            return;
        }

        if (_mesh == null) {
            Logger.W("Could not draw mesh, because mesh member is null.");
            return;
        }

        _mesh.draw(_material, _modelMatrix, vpMatrix);
    }

    public void setEnabled(boolean enabled) {
        if (m_Enabled && !enabled)
        {
            m_Owner.getGameView().addMeshRendererToDeleteQueue(this);
        }
        else if (!m_Enabled && enabled)
        {
            m_Owner.getGameView().addMeshRendererToInsertQueue(this);
        }

        m_Enabled = enabled;
    }

    public void setMaterial(Material material) {
        m_Material = material;
    }

    public void setMesh(Mesh mesh) {
        m_Mesh = mesh;
    }

    public GameObject getOwner() {
        return m_Owner;
    }

    public boolean isEnabled() {
        return m_Enabled;
    }

    public Material getMaterial() {
        return m_Material;
    }

    public Mesh getMesh() {
        return m_Mesh;
    }
}
