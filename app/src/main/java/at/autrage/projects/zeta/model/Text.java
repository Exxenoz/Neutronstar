package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.opengl.Font;
import at.autrage.projects.zeta.opengl.Fonts;
import at.autrage.projects.zeta.opengl.TextMaterial;
import at.autrage.projects.zeta.opengl.TextRenderer;

public class Text extends Component {
    private Font font;
    private String text;

    private TextMaterial textMaterial;
    private TextRenderer textRenderer;

    public Text(GameObject gameObject) {
        super(gameObject);

        font = AssetManager.getInstance().getFont(Fonts.Arial);
        text = "";

        textMaterial = new TextMaterial();
        textRenderer = null;

        textMaterial.setTexture(font.FontAtlasTexture);
    }

    @Override
    protected void onStart() {
        textRenderer = gameObject.addComponent(TextRenderer.class);
        textRenderer.setMaterial(textMaterial);
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        if (font == null) {
            return;
        }

        if (this.font == font) {
            return;
        }

        this.font = font;

        textMaterial.setTexture(font.FontAtlasTexture);
        textRenderer.rebuildTextMesh(font, text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text == null) {
            text = "";
        }

        if (text.equals(this.text)) {
            return;
        }

        this.text = text;

        textRenderer.rebuildTextMesh(font, text);
    }

    public int getDrawOrderID() {
        return textRenderer.getDrawOrderID();
    }

    public void setDrawOrderID(int drawOrderID) {
        textRenderer.setDrawOrderID(drawOrderID);
    }
}
