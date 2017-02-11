package at.autrage.projects.zeta.opengl;

import at.autrage.projects.zeta.module.Logger;

public class MeshRenderer {
    private boolean m_Enabled;
    private Material m_Material;
    private Mesh m_Mesh;

    public MeshRenderer(Material material, Mesh mesh) {
        m_Enabled = true;
        m_Material = material;
        m_Mesh = mesh;
    }

    public void draw(float[] mvpMatrix) {
        if (!m_Enabled) {
            return;
        }

        if (m_Material == null) {
            Logger.W("Could not draw mesh, because material member is null.");
            return;
        }

        if (m_Mesh == null) {
            Logger.W("Could not draw mesh, because mesh member is null.");
            return;
        }

        m_Material.draw(m_Mesh, mvpMatrix);
    }

    public void setEnabled(boolean enabled) {
        m_Enabled = enabled;
    }

    public void setMaterial(Material material) {
        m_Material = material;
    }

    public void setMesh(Mesh mesh) {
        m_Mesh = mesh;
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
