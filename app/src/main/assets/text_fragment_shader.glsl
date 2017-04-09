precision mediump float;

uniform sampler2D u_Texture;    // The input texture.

varying vec4 v_Color;           // This is the color from the vertex shader interpolated across the
                                // triangle per fragment.
varying vec2 v_TexCoordinate;   // Interpolated texture coordinate per fragment.

//const float smoothing = 1.0/16.0;

float contour(in float d, in float w) {
    // smoothstep(lower edge0, upper edge1, x)
    return smoothstep(0.5 - w, 0.5 + w, d);
}

float samp(in vec2 uv, float w) {
    return contour(texture2D(u_Texture, v_TexCoordinate).a, w);
}

// The entry point for our fragment shader.
void main()
{
    //****************
    // Basic version *
    //****************

    // Retrieve distance from texture
    //float distance = texture2D(u_Texture, v_TexCoordinate).a;
    //float alpha = smoothstep(0.5 - smoothing, 0.5 + smoothing, distance);
    //gl_FragColor = vec4(v_Color.rgb, v_Color.a * alpha);

    //***********************
    // Supersampled version *
    //***********************

    // Retrieve distance from texture
    float dist = texture2D(u_Texture, v_TexCoordinate).a;

    // fwidth helps keep outlines a constant width irrespective of scaling
    // GLSL's fwidth = abs(dFdx(uv)) + abs(dFdy(uv))
    float width = fwidth(dist);

    float alpha = contour( dist, width );

    // Supersample, 4 extra points
    float dscale = 0.354;
    vec2 duv = dscale * (dFdx(v_TexCoordinate) + dFdy(v_TexCoordinate));
    vec4 box = vec4(v_TexCoordinate - duv, v_TexCoordinate + duv);

    float asum = samp( box.xy, width )
               + samp( box.zw, width )
               + samp( box.xw, width )
               + samp( box.zy, width );

    // Weighted average, with 4 extra points having 0.5 weight each,
    // so 1 + 0.5*4 = 3 is the divisor
    alpha = (alpha + 0.5 * asum) / 3.0;

    gl_FragColor = vec4(v_Color.rgb, v_Color.a * alpha);
}
