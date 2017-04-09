package at.autrage.projects.zeta.opengl;

import java.util.ArrayList;
import java.util.List;

import at.autrage.projects.zeta.model.GameObject;
import at.autrage.projects.zeta.module.Logger;

public class TextRenderer extends MeshRenderer {
    private String text;
    private Font font;
    private TextMesh[] textMeshes;

    public TextRenderer(GameObject gameObject) {
        super(gameObject);

        text = "";
        font = null;
        textMeshes = new TextMesh[3];
        for (int i = 0; i < textMeshes.length; i++) {
            textMeshes[i] = new TextMesh();
        }
        mesh = textMeshes[0];
    }

    public void rebuildTextMesh() {
        if (font == null) {
            Logger.E("Could not rebuild text mesh, because no font was set!");
            return;
        }

        TextMesh editableTextMesh = getEditableTextMesh();
        if (editableTextMesh == null) {
            Logger.E("Could not rebuild text mesh, because no editable text mesh could be found!");
            return;
        }

        editableTextMesh.rebuildTextMesh(text, font);
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text != null) {
            this.text = text;
        }
        else {
            this.text = "";
        }
        rebuildTextMesh();
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
        rebuildTextMesh();
    }
}
