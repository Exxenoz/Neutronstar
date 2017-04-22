package at.autrage.projects.zeta.opengl;

public class PustafinGL {
    public static final int BYTES_PER_FLOAT = 4;
    public static final int BYTES_PER_SHORT = 2;

    public static final int FLOATS_PER_VERTEX = 3;  // Amount floats per vertex (x, y, z)
    public static final int FLOATS_PER_COLOR = 4;   // Amount of floats per color (r, g, b, a)

    public static final int FLOATS_PER_TEXTURE_COORD = 2;
    public static final int SHORTS_PER_TRIANGLE_INDEX = 6;

    public static final int BYTES_PER_VERTEX = FLOATS_PER_VERTEX * BYTES_PER_FLOAT;
    public static final int BYTES_PER_TRIANGLE_INDEX = SHORTS_PER_TRIANGLE_INDEX * BYTES_PER_SHORT;
    public static final int BYTES_PER_COLOR = FLOATS_PER_COLOR * BYTES_PER_FLOAT;
    public static final int BYTES_PER_TEXTURE_COORD = FLOATS_PER_TEXTURE_COORD * BYTES_PER_FLOAT;

    public static final int VERTICES_PER_RADIUS_IN_PIXEL = 3;

    public static final int TEXT_MESH_INDEX_BUFFER_CAPACITY = 1024;
    public static final int DEFAULT_TEXT_MESH_CHARACTER_CAPACITY = 256; // Must be lower than TEXT_MESH_INDEX_BUFFER_CAPACITY
}
