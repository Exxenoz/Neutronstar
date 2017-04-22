package at.autrage.projects.zeta.model;

import at.autrage.projects.zeta.module.AssetManager;
import at.autrage.projects.zeta.opengl.Color;
import at.autrage.projects.zeta.opengl.Font;
import at.autrage.projects.zeta.opengl.Fonts;
import at.autrage.projects.zeta.opengl.GlyphBlock;
import at.autrage.projects.zeta.opengl.TextAlignmentOptions;
import at.autrage.projects.zeta.opengl.TextMaterial;
import at.autrage.projects.zeta.opengl.TextOverflowModes;
import at.autrage.projects.zeta.opengl.TextRenderer;

public class Text extends Component {
    private Font font;
    private float fontSize;
    private String text;
    private TextAlignmentOptions textAlignment;
    private TextOverflowModes textOverflowMode;
    private float maxWidth;
    private float maxHeight;
    private boolean wordWrappingEnabled;
    private GlyphBlock glyphBlock;
    private boolean isDirty;

    private TextMaterial textMaterial;
    private TextRenderer textRenderer;

    public Text(GameObject gameObject) {
        super(gameObject);

        font = AssetManager.getInstance().getFont(Fonts.Arial);
        setFontSize(32f);
        text = "";
        textAlignment = TextAlignmentOptions.TopLeft;
        textOverflowMode = TextOverflowModes.Overflow;
        maxWidth = 0f;
        maxHeight = 0f;
        wordWrappingEnabled = false;
        glyphBlock = null;
        isDirty = false;

        textMaterial = new TextMaterial();
        textRenderer = null;

        textMaterial.setTexture(font.FontAtlasTexture);
    }

    @Override
    protected void onStart() {
        textRenderer = gameObject.addComponent(TextRenderer.class);
        textRenderer.setMaterial(textMaterial);
    }

    @Override
    protected void onUpdate() {
        if (isDirty) {
            if (font != null) {
                glyphBlock = font.measureText(text, fontSize, textOverflowMode, maxWidth, maxHeight, wordWrappingEnabled);
                textRenderer.rebuildTextMesh(glyphBlock, textAlignment);
            }

            isDirty = false;
        }
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

        isDirty = true;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        if (fontSize <= 0f) {
            fontSize = 0.001f;
        }

        if (this.fontSize == fontSize) {
            return;
        }

        this.fontSize = fontSize;

        gameObject.setScale(fontSize, fontSize, gameObject.getScaleZ());

        isDirty = true;
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

        isDirty = true;
    }

    public TextAlignmentOptions getTextAlignment() {
        return textAlignment;
    }

    public void setTextAlignment(TextAlignmentOptions textAlignment) {
        if (this.textAlignment == textAlignment) {
            return;
        }

        this.textAlignment = textAlignment;

        isDirty = true;
    }

    public TextOverflowModes getTextOverflowMode() {
        return textOverflowMode;
    }

    public void setTextOverflowMode(TextOverflowModes textOverflowMode) {
        if (this.textOverflowMode == textOverflowMode) {
            return;
        }

        this.textOverflowMode = textOverflowMode;

        isDirty = true;
    }

    public float getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(float maxWidth) {
        this.maxWidth = maxWidth;

        isDirty = true;
    }

    public float getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(float maxHeight) {
        this.maxHeight = maxHeight;

        isDirty = true;
    }

    public boolean isWordWrappingEnabled() {
        return wordWrappingEnabled;
    }

    public void setWordWrappingEnabled(boolean wordWrappingEnabled) {
        if (this.wordWrappingEnabled == wordWrappingEnabled) {
            return;
        }

        this.wordWrappingEnabled = wordWrappingEnabled;

        isDirty = true;
    }

    public Color getColor() {
        return textMaterial.getColor();
    }

    public void setColor(Color color) {
        textMaterial.setColor(color);
    }

    public int getDrawOrderID() {
        return textRenderer.getDrawOrderID();
    }

    public void setDrawOrderID(int drawOrderID) {
        textRenderer.setDrawOrderID(drawOrderID);
    }
}
