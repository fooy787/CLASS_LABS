#version 410

layout (location = 0) in vec3 VertexPosition;
layout (location = 1) in vec3 VertexColor;

uniform mat4 ModelMatrix;
out vec3 vertColor;

void main() {
	vertColor = VertexColor;
	gl_Position = ModelMatrix * vec4(VertexPosition, 1.0);
}
