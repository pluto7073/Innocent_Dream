#version 400 core

in vec2 position;
in vec2 texture_coordinates;

out vec2 textureCoords;

void main() {

    gl_Position = vec4(position, 0.0, 1.0);
    textureCoords = texture_coordinates;

}