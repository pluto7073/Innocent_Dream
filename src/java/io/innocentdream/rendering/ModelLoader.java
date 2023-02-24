package io.innocentdream.rendering;

import io.innocentdream.InnocentDream;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL15.*;

public class ModelLoader {

    private final List<Integer> vaos = new ArrayList<>();
    private final List<Integer> vbos = new ArrayList<>();

    public Model loadToVAO(float[] positions, float[] textureCoordinates) {
        int vaoID = createVAO();
        bindIndicesBuffer(new int[] { 0, 1, 3, 1, 2, 3 });
        storeDataInList(0, 3, positions);
        storeDataInList(1, 2, textureCoordinates);
        unbindVAO();
        return new Model(vaoID, positions.length / 3);
    }

    public Model loadToVAO(float[] positions) {
        int vaoID = createVAO();
        storeDataInList(0, 2, positions);
        storeDataInList(1, 2, new float[] { 0, 1, 0, 0, 1, 0, 1, 1 });
        unbindVAO();
        return new Model(vaoID, positions.length / 2);
    }

    public void bindIndicesBuffer(int[] indices) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = toIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    public void cleanUp() {
        for (int id : vaos) {
            glDeleteVertexArrays(id);
        }
        for (int id : vbos) {
            glDeleteBuffers(id);
        }
    }

    private int createVAO() {
        int vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        vaos.add(vaoID);
        return vaoID;
    }

    private void storeDataInList(int index, int size, float[] data) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer dataBuffer = toFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, dataBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO() {
        glBindVertexArray(0);
    }

    private FloatBuffer toFloatBuffer(float[] array) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(array.length);
        buffer.put(array);
        buffer.flip();
        return buffer;
    }

    private IntBuffer toIntBuffer(int[] array) {
        IntBuffer buffer = BufferUtils.createIntBuffer(array.length);
        buffer.put(array);
        buffer.flip();
        return buffer;
    }

}
