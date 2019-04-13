#version 410

layout (location = 0) in vec3 VertexPosition;

uniform mat4 ModelMatrix;
uniform mat4 ViewMatrix;
uniform mat4 ProjectionMatrix;

out vec4 worldPos;

void main() {
	worldPos = ModelMatrix * vec4(VertexPosition, 1);
	vec4 eyePos = ViewMatrix * worldPos;
	gl_Position = ProjectionMatrix * eyePos;
}
