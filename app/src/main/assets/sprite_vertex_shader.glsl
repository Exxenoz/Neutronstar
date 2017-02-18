uniform mat4 u_ModelMatrix;     // A 4x4 matrix representing the model matrix.
uniform mat4 u_VPMatrix;        // A 4x4 matrix representing the combined view/projection matrix.
uniform vec4 a_Color;           // Per-vertex color information we will pass in.

attribute vec4 a_Position;		// Per-vertex position information we will pass in.
attribute vec2 a_TexCoordinate; // Per-vertex texture coordinate information we will pass in.

varying vec4 v_Color;			// This will be passed into the fragment shader.
varying vec2 v_TexCoordinate;   // This will be passed into the fragment shader.

// The entry point for our vertex shader.
void main()
{
	// Pass through the color.
	v_Color = a_Color;

	// Pass through the texture coordinate.
    v_TexCoordinate = a_TexCoordinate;

	// gl_Position is a special variable used to store the final position.
	// Multiply the vertex by the model/view/projection matrix to get the final point in normalized screen coordinates.
	gl_Position = u_VPMatrix * u_ModelMatrix * a_Position;
}
