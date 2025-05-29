#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_time;
uniform vec2 u_direction;

const int NUM_GHOSTS = 48;
const float SPACING = 0.025;
const vec3 TRAIL_COLOR = vec3(1.5, 1.5, 0.3); // Super brillante "amarillo patito"

void main() {
    vec4 base = texture2D(u_texture, v_texCoords);

    if (length(u_direction) < 0.1) {
        gl_FragColor = base;
        return;
    }

    vec2 dir = normalize(u_direction);
    vec4 accum = vec4(0.0);

    for (int i = 1; i <= NUM_GHOSTS; ++i) {
        float t = float(i) / float(NUM_GHOSTS);
        float offset = t * SPACING;

        vec2 uv = v_texCoords - dir * offset;

        if (uv.x < 0.0 || uv.x > 1.0 || uv.y < 0.0 || uv.y > 1.0) continue;

        vec4 ghost = texture2D(u_texture, uv);

        float alphaFade = pow(1.0 - t, 2.0);
        float pulse = sin(u_time * 20.0 - t * 40.0) * 0.5 + 0.5;

        ghost.rgb = mix(ghost.rgb, TRAIL_COLOR, 1.0);
        ghost.rgb *= (pulse + 0.3) * alphaFade * 2.0;
        ghost.a *= alphaFade * 0.5;

        accum.rgb += ghost.rgb * ghost.a;
    }

    gl_FragColor = vec4(base.rgb + accum.rgb, base.a);
}
