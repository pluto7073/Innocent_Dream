package io.innocentdream.rendering.shaders;

public class MainShader extends ShaderProgram {

    public MainShader() {
        super("main");
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "texture_coordinates");
    }

}
