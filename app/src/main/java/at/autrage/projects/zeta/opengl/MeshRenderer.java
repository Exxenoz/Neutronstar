package at.autrage.projects.zeta.opengl;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import at.autrage.projects.zeta.model.Component;
import at.autrage.projects.zeta.model.GameObject;
import at.autrage.projects.zeta.module.Logger;

public class MeshRenderer extends Component {
    protected int drawOrderID;
    protected Material material;
    protected Mesh mesh;
    public ConcurrentLinkedQueue<MeshRenderer> Holder;

    protected ShaderParams[] _shaderParams;
    protected AtomicInteger _shaderParamsUpdateIdx;
    protected AtomicInteger _shaderParamsRenderIdx;

    public MeshRenderer(GameObject gameObject) {
        super(gameObject);

        drawOrderID = 0;
        material = null;
        mesh = null;
        Holder = null;

        _shaderParams = new ShaderParams[3];
        for (int i = 0; i < _shaderParams.length; i++) {
            _shaderParams[i] = new ShaderParams();
        }
        _shaderParamsUpdateIdx = new AtomicInteger(1);
        _shaderParamsRenderIdx = new AtomicInteger(0);
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

    public void lateUpdate() {
        int renderIdx = _shaderParamsRenderIdx.get();
        int updateIdx = _shaderParamsUpdateIdx.get();

        ShaderParams shaderParams = _shaderParams[updateIdx];

        shaderParams.Enabled = isEnabled();

        if (material != null) {
            shaderParams.Shader = material._shader;
            material.shift(shaderParams);
        }
        else {
            shaderParams.Enabled = false;
            Logger.W("Could not draw mesh, because material member is null.");
        }

        if (mesh != null) {
            shaderParams.Mesh = mesh;
        }
        else {
            shaderParams.Enabled = false;
            Logger.W("Could not draw mesh, because mesh member is null.");
        }

        System.arraycopy(gameObject.getModelMatrix(), 0, shaderParams.ModelMatrix, 0, 16);

        int indexBeforeUpdateIdx = updateIdx - 1;
        if (indexBeforeUpdateIdx < 0) {
            indexBeforeUpdateIdx = _shaderParams.length - 1;
        }

        if (indexBeforeUpdateIdx == renderIdx) {
            updateIdx++;
            if (updateIdx >= _shaderParams.length) {
                updateIdx = 0;
            }
            _shaderParamsUpdateIdx.set(updateIdx);
        }
    }

    public void draw(float[] vpMatrix) {
        int renderIdx = _shaderParamsRenderIdx.get();
        int updateIdx = _shaderParamsUpdateIdx.get();

        int indexBeforeRenderIdx = renderIdx - 1;
        if (indexBeforeRenderIdx < 0) {
            indexBeforeRenderIdx = _shaderParams.length - 1;
        }

        if (indexBeforeRenderIdx == updateIdx) {
            renderIdx++;
            if (renderIdx >= _shaderParams.length) {
                renderIdx = 0;
            }
            _shaderParamsRenderIdx.set(renderIdx);
        }

        ShaderParams shaderParams = _shaderParams[renderIdx];

        if (shaderParams.Enabled) {
            // Copy of reference is OK, because
            // the matrix is only accessible by
            // the render thread.
            shaderParams.VPMatrix = vpMatrix;

            shaderParams.Shader.draw(shaderParams);
        }
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
