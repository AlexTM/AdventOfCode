#version 330

flat in vec3 outColor;

out vec4 fragColor;

void main()
{
//    fragColor = vec4(1.0, 0.0, 0.0, 1.0);
    fragColor = vec4(outColor, 1.0);
}