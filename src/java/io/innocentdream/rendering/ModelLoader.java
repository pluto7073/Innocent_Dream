package io.innocentdream.rendering;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL15.*;

public class ModelLoader {

    private final List<Integer> vaos = new ArrayList<>();
    private final List<Integer> vbos = new ArrayList<>();

    public Model loadToVAO(float[] positions) {
        int vaoID = createVAO();
        storeDataInList(0, positions);
        unbindVAO();
        return new Model(vaoID, positions.length / 2);
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

    private void storeDataInList(int index, float[] data) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer dataBuffer = toFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, dataBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(index, 2, GL11.GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
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

}
