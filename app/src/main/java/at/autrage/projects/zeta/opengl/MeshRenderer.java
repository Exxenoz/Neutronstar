package at.autrage.projects.zeta.opengl;

import java.util.concurrent.ConcurrentLinkedQueue;

import at.autrage.projects.zeta.model.Component;
import at.autrage.projects.zeta.model.GameObject;
import at.autrage.projects.zeta.module.Logger;

public class MeshRenderer extends Component {
    private int drawOrderID;
    private Material material;
    private Mesh mesh;
    public ConcurrentLinkedQueue<MeshRenderer> Holder;

    private ShaderParams _shaderParams;

    public MeshRenderer(GameObject gameObject) {
        super(gameObject);

        drawOrderID = 0;
        material = null;
        mesh = null;
        Holder = null;

        _shaderParams = new ShaderParams();
    }

    @Override
    protected void onEnable() {
        gameObject.getGameView().addMeshRenderer(this, drawOrderID);
    }

    @Override
    protected void onDisable() {
        gameObject.getGameView().removeMeshRenderer(this);
    }

    @Override
    protected void onDestroy() {
        gameObject.getGameView().removeMeshRenderer(this);
    }

    public void shift() {
        _shaderParams.Enabled = isEnabled();

        _shaderParams.Material = material;
        if (_shaderParams.Material != null) {
            _shaderParams.Material.shift(_shaderParams);
        }

        _shaderParams.Mesh = mesh;
        if (_shaderParams.Mesh != null) {
            _shaderParams.Mesh.shift(_shaderParams);
        }

        System.arraycopy(gameObject.getModelMatrix(), 0, _shaderParams.ModelMatrix, 0, 16);
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

    public void setDrawOrderID(int drawOrderID) {
        this.drawOrderID = drawOrderID;

        if (isEnabled()) {
            gameObject.getGameView().removeMeshRenderer(this);
            gameObject.getGameView().addMeshRenderer(this, drawOrderID);
        }
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public int getDrawOrderID() {
        return drawOrderID;
    }

    public Material getMaterial() {
        return material;
    }

    public Mesh getMesh() {
        return mesh;
    }
}
