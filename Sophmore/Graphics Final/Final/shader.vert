#version 410

layout (location = 0) in vec3 VertexPosition;
layout (location = 1) in vec2 VertexTexCoords;

uniform mat4 ModelMatrix;
uniform mat4 ViewMatrix;
uniform mat4 ProjectionMatrix;

out vec4 worldPos;
out vec2 uv;

void main() {
	uv = VertexTexCoords;
	worldPos = ModelMatrix * vec4(VertexPosition, 1);
	vec4 eyePos = ViewMatrix * worldPos;
	gl_Position = ProjectionMatrix * eyePos;
}
