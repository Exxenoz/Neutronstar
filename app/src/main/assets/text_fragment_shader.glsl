precision mediump float;

uniform sampler2D u_Texture;    // The input texture.

varying vec4 v_Color;           // This is the color from the vertex shader interpolated across the
                                // triangle per fragment.
varying vec2 v_TexCoordinate;   // Interpolated texture coordinate per fragment.

const float smoothing = 1.0/16.0;

// The entry point for our fragment shader.
void main()
{
    float distance = texture2D(u_Texture, v_TexCoordinate).a;
    float alpha = smoothstep(0.5 - smoothing, 0.5 + smoothing, distance);
    gl_FragColor = vec4(v_Color.rgb, v_Color.a * alpha);
}
