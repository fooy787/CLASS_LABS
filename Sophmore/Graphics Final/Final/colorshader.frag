#version 410

in vec4 worldPos;

out vec4 FragColor;

uniform vec4 inColor;

void main() {
	FragColor = inColor;
}
