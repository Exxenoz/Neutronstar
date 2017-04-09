package at.autrage.projects.zeta.opengl;

import java.nio.FloatBuffer;

import at.autrage.projects.zeta.module.Logger;

public class TextMesh extends Mesh {
    protected int capacity;

    public TextMesh() {
        allocateCapacity(PustafinGL.DEFAULT_TEXT_MESH_CHARACTER_CAPACITY);
    }

    private void allocateCapacity(int capacity) {
        this.capacity = capacity;
        vertexCount = capacity * 4;
        indexDrawCount = 0;

        vertexBuffer = createFloatBuffer(vertexCount * PustafinGL.FLOATS_PER_VERTEX);
        indexBuffer = createShortBuffer(capacity * 6);
        textureCoordBuffer = createFloatBuffer(vertexCount * PustafinGL.FLOATS_PER_TEXTURE_COORD);

        initializeIndices();
    }

    private void initializeIndices() {
        indexBuffer.rewind();

        short[] quadIndices = new short[] {
            0, 1, 2, // Triangle1
            1, 3, 2  // Triangle2
        };

        for (int quad = 0, length = quadIndices.length; quad < capacity; quad++) {
            indexBuffer.put(quadIndices);

            for (int i = 0; i < length; i++) {
                quadIndices[i] += 4;
            }
        }

        indexBuffer.rewind();
    }

    public void rebuildTextMesh(String text, Font font) {
        if (text == null) {
            return;
        }

        if (font == null) {
            return;
        }

        if (capacity < text.length()) {
            int factor = (int)(text.length() / (float)capacity + 1f);
            allocateCapacity(capacity * factor);
        }

        vertexBuffer.rewind();
        textureCoordBuffer.rewind();

        float glyphPositionX = 0f;
        float glyphPositionY = 0f;

        float[] quadVertices = new float[12];

        int quads = 0;

        for (int i = 0, length = text.length(); i < length; i++) {
            char c = text.charAt(i);

            switch (c) {
                case '\n':
                    glyphPositionX = 0f;
                    glyphPositionY -= font.getLineHeightNorm();
                    continue;
                default:
                    break;
            }

            Glyph glyph = font.getGlyphForCharacter(c);

            if (glyph == null) {
                Logger.W("Could not draw character '" + c + "', because glyph could not be found in font " + font.Name + "!");
                continue;
            }

            // Bottom Left
            quadVertices[0] = glyphPositionX + glyph.XOffsetNorm;
            quadVertices[1] = glyphPositionY - glyph.YOffsetNorm - glyph.HNorm;

            // Bottom Right
            quadVertices[3] = glyphPositionX + glyph.WNorm + glyph.XOffsetNorm;
            quadVertices[4] = glyphPositionY - glyph.HNorm - glyph.YOffsetNorm;

            // Top Left
            quadVertices[6] = glyphPositionX + glyph.XOffsetNorm;
            quadVertices[7] = glyphPositionY - glyph.YOffsetNorm;

            // Top Right
            quadVertices[9] = glyphPositionX + glyph.WNorm + glyph.XOffsetNorm;
            quadVertices[10] = glyphPositionY - glyph.YOffsetNorm;

            vertexBuffer.put(quadVertices);
            textureCoordBuffer.put(glyph.TextureCoordinates);

            glyphPositionX += glyph.XAdvanceNorm;

            quads++;
        }

        indexDrawCount = quads * 6;

        vertexBuffer.rewind();
        textureCoordBuffer.rewind();

        Logger.D("Rebuild text mesh for text \"" + text + "\" with " + quads + " quads and " + indexDrawCount + " indices!");
    }
}
