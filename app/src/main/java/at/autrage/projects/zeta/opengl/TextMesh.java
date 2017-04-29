package at.autrage.projects.zeta.opengl;

import java.nio.ShortBuffer;

import at.autrage.projects.zeta.module.Logger;

public class TextMesh extends Mesh {
    private static final ShortBuffer quadIndexBuffer;

    static {
        quadIndexBuffer = createShortBuffer(PustafinGL.TEXT_MESH_INDEX_BUFFER_CAPACITY * 6);

        short[] quadIndices = new short[] {
                0, 1, 2, // Triangle1
                1, 3, 2  // Triangle2
        };

        for (int quad = 0, length = quadIndices.length; quad < PustafinGL.TEXT_MESH_INDEX_BUFFER_CAPACITY; quad++) {
            quadIndexBuffer.put(quadIndices);

            for (int i = 0; i < length; i++) {
                quadIndices[i] += 4;
            }
        }

        quadIndexBuffer.rewind();
    }

    private int capacity;

    private float[] fastVertexBuffer;
    private float[] fastTexCoordBuffer;

    public TextMesh() {
        allocateCapacity(PustafinGL.DEFAULT_TEXT_MESH_CHARACTER_CAPACITY);
    }

    private void allocateCapacity(int capacity) {
        this.capacity = capacity;
        vertexCount = capacity * 4;
        indexDrawCount = 0;

        vertexBuffer = createFloatBuffer(vertexCount * PustafinGL.FLOATS_PER_VERTEX);
        indexBuffer = quadIndexBuffer;
        textureCoordBuffer = createFloatBuffer(vertexCount * PustafinGL.FLOATS_PER_TEXTURE_COORD);

        fastVertexBuffer = new float[vertexCount * PustafinGL.FLOATS_PER_VERTEX];
        fastTexCoordBuffer = new float[vertexCount * PustafinGL.FLOATS_PER_TEXTURE_COORD];
    }

    public void rebuildTextMesh(GlyphBlock glyphBlock, TextAlignmentOptions textAlignment) {
        if (glyphBlock == null) {
            Logger.E("Could not rebuild text mesh, because glyph block parameter may not be null!");
            return;
        }

        // ToDo: Not very accurate, because whitespaces should be excluded
        int size = glyphBlock.getGlyphCount();

        if (size > PustafinGL.TEXT_MESH_INDEX_BUFFER_CAPACITY) {
            Logger.W("Could not draw " + (size - PustafinGL.TEXT_MESH_INDEX_BUFFER_CAPACITY) +  " quads of text mesh with text \"" + glyphBlock.OriginalText + "\", because the capacity of static index buffer for quads is too low!");
            size = PustafinGL.TEXT_MESH_INDEX_BUFFER_CAPACITY;
        }

        if (capacity < size) {
            int factor = (int)(size / (float)capacity + 1f);
            allocateCapacity(Math.min(capacity * factor, PustafinGL.TEXT_MESH_INDEX_BUFFER_CAPACITY));
        }

        vertexBuffer.rewind();
        textureCoordBuffer.rewind();

        float glyphPositionX = 0f;
        float glyphPositionY = 0f;
        float glyphLineOffsetY = 0f;

        float[] quadVertices = new float[12];

        int v = 0;
        int t = 0;
        int quads = 0;

        for (GlyphLine glyphLine : glyphBlock.GlyphLines)
        {
            if (quads >= size)
            {
                break;
            }

            switch (textAlignment)
            {
                case TopLeft:
                    glyphPositionX = 0f;
                    glyphPositionY = 0f - glyphLineOffsetY;
                    break;
                case TopCenter:
                    glyphPositionX = glyphBlock.HalfMaxWidthNorm - glyphLine.HalfWidthNorm;
                    glyphPositionY = 0f - glyphLineOffsetY;
                    break;
                case TopRight:
                    glyphPositionX = glyphBlock.MaxWidthNorm - glyphLine.WidthNorm;
                    glyphPositionY = 0f - glyphLineOffsetY;
                    break;
                case CenterLeft:
                    glyphPositionX = 0f;
                    glyphPositionY = -(glyphBlock.HalfMaxHeightNorm - glyphBlock.HalfHeightNorm) - glyphLineOffsetY;
                    break;
                case CenterCenter:
                    glyphPositionX = glyphBlock.HalfMaxWidthNorm - glyphLine.HalfWidthNorm;
                    glyphPositionY = -(glyphBlock.HalfMaxHeightNorm - glyphBlock.HalfHeightNorm) - glyphLineOffsetY;
                    break;
                case CenterRight:
                    glyphPositionX = glyphBlock.MaxWidthNorm - glyphLine.WidthNorm;
                    glyphPositionY = -(glyphBlock.HalfMaxHeightNorm - glyphBlock.HalfHeightNorm) - glyphLineOffsetY;
                    break;
                case BottomLeft:
                    glyphPositionX = 0f;
                    glyphPositionY = -(glyphBlock.MaxHeightNorm - glyphBlock.HeightNorm) - glyphLineOffsetY;
                    break;
                case BottomCenter:
                    glyphPositionX = glyphBlock.HalfMaxWidthNorm - glyphLine.HalfWidthNorm;
                    glyphPositionY = -(glyphBlock.MaxHeightNorm - glyphBlock.HeightNorm) - glyphLineOffsetY;
                    break;
                case BottomRight:
                    glyphPositionX = glyphBlock.MaxWidthNorm - glyphLine.WidthNorm;
                    glyphPositionY = -(glyphBlock.MaxHeightNorm - glyphBlock.HeightNorm) - glyphLineOffsetY;
                    break;
            }

            for (Glyph glyph : glyphLine.Glyphs)
            {
                if (quads >= size)
                {
                    break;
                }

                if (glyph.Character != ' ') {
                    // BottomCenter CenterLeft
                    quadVertices[0] = glyphPositionX + glyph.XOffsetNorm;
                    quadVertices[1] = glyphPositionY - glyph.YOffsetNorm - glyph.HNorm;

                    // BottomCenter CenterRight
                    quadVertices[3] = glyphPositionX + glyph.WNorm + glyph.XOffsetNorm;
                    quadVertices[4] = glyphPositionY - glyph.HNorm - glyph.YOffsetNorm;

                    // TopCenter CenterLeft
                    quadVertices[6] = glyphPositionX + glyph.XOffsetNorm;
                    quadVertices[7] = glyphPositionY - glyph.YOffsetNorm;

                    // TopCenter CenterRight
                    quadVertices[9] = glyphPositionX + glyph.WNorm + glyph.XOffsetNorm;
                    quadVertices[10] = glyphPositionY - glyph.YOffsetNorm;

                    System.arraycopy(quadVertices, 0, fastVertexBuffer, v, 12);
                    System.arraycopy(glyph.TextureCoordinates, 0, fastTexCoordBuffer, t, 8);

                    v += 12;
                    t += 8;

                    quads++;
                }

                glyphPositionX += glyph.XAdvanceNorm;
            }

            glyphLineOffsetY += glyphLine.HeightNorm;
        }

        vertexBuffer.put(fastVertexBuffer, 0, v);
        textureCoordBuffer.put(fastTexCoordBuffer, 0, t);

        indexDrawCount = quads * 6;

        vertexBuffer.rewind();
        textureCoordBuffer.rewind();

        Logger.D("Rebuild text mesh for text \"" + glyphBlock.OriginalText + "\" with " + quads + " quads and " + indexDrawCount + " indices!");
    }
}
