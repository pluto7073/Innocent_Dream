package io.innocentdream.rendering.shaders;

public class GUIShader extends ShaderProgram {

    public GUIShader() {
        super("gui");
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "texture_coordinates");
    }

}
