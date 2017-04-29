package at.autrage.projects.zeta.opengl;

import java.util.ArrayList;
import java.util.List;

import at.autrage.projects.zeta.model.GameObject;
import at.autrage.projects.zeta.module.Logger;

public class TextRenderer extends MeshRenderer {
    private TextMesh[] textMeshes;

    public TextRenderer(GameObject gameObject) {
        super(gameObject);

        textMeshes = new TextMesh[3];
        for (int i = 0; i < textMeshes.length; i++) {
            textMeshes[i] = new TextMesh();
        }
        mesh = textMeshes[0];
    }

    public void rebuildTextMesh(GlyphBlock glyphBlock, TextAlignmentOptions textAlignment) {
        if (glyphBlock == null) {
            Logger.E("Could not rebuild text mesh, because glyph block parameter may not be null!");
            return;
        }

        TextMesh editableTextMesh = getEditableTextMesh();
        if (editableTextMesh == null) {
            Logger.E("Could not rebuild text mesh, because no editable text mesh could be found!");
            return;
        }

        editableTextMesh.rebuildTextMesh(glyphBlock, textAlignment);
        setMesh(editableTextMesh);
    }

    private TextMesh getEditableTextMesh() {
        int updateIdx = _shaderParamsUpdateIdx.get();

        List<Mesh> usedTextMeshes = new ArrayList<>(3);
        for (int i = 0; i < _shaderParams.length; i++) {
            if (!usedTextMeshes.contains(_shaderParams[i].Mesh)) {
                usedTextMeshes.add(_shaderParams[i].Mesh);
            }
        }

        // Every single text mesh is used currently
        if (usedTextMeshes.size() == textMeshes.length) {
            return (TextMesh)_shaderParams[updateIdx].Mesh;
        }
        // Search for unused text mesh
        else for (int i = 0; i < textMeshes.length; i++) {
            if (!usedTextMeshes.contains(textMeshes[i])) {
                return textMeshes[i];
            }
        }

        return null;
    }
}
