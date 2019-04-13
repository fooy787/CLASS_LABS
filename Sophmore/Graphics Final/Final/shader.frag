#version 410

in vec4 worldPos;
in vec2 uv;

out vec4 FragColor;

uniform vec4 inColor;
uniform sampler2D sampler;

void main() {
	vec4 c = texture(sampler, uv).rgba;
	FragColor = c;
}
