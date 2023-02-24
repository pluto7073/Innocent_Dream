package io.innocentdream.rendering.shaders;

import io.innocentdream.rendering.DisplayManager;
import io.innocentdream.rendering.ResourceManager;
import io.innocentdream.utils.Identifier;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.glfw.GLFW.*;

public abstract class ShaderProgram {

    public static final Logger LOGGER = LogManager.getLogger("ShaderProgram");

    private final int programID;
    private final int vertexShaderID;
    private final int fragmentShaderID;
    protected final String name;

    public ShaderProgram(String name) {
        this.name = name;
        vertexShaderID = loadShader(new Identifier("assets", "shaders/%s/%s.vsh".formatted(name, name)), GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(new Identifier("assets", "shaders/%s/%s.fsh".formatted(name, name)), GL_FRAGMENT_SHADER);
        programID = glCreateProgram();
        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        glLinkProgram(programID);
        glValidateProgram(programID);
    }

    public void start() {
        glUseProgram(programID);
    }

    public void stop() {
        glUseProgram(0);
    }

    public void cleanUp() {
        stop();
        glDetachShader(programID, vertexShaderID);
        glDetachShader(programID, fragmentShaderID);
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
        glDeleteProgram(programID);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String name) {
        glBindAttribLocation(programID, attribute, name);
    }

    private static int loadShader(Identifier file, int type) {
        StringBuilder source = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(ResourceManager.getOrLoad(file).get()));
            String line;
            while ((line = reader.readLine()) != null) {
                source.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            LOGGER.error("Could not read shader " + file.toString(), e);
            System.exit(e.hashCode());
        }
        glfwMakeContextCurrent(DisplayManager.win);
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, source);
        glCompileShader(shaderID);
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            LOGGER.error(glGetShaderInfoLog(shaderID, 500));
            LOGGER.error("Could not compile shader " + file.toString());
            System.exit(-1);
        }
        return shaderID;
    }

    static {
        LOGGER.setLevel(Level.ALL);
    }

}
