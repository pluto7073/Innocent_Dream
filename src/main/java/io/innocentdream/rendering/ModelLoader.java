package io.innocentdream.rendering;

import io.innocentdream.InnocentDream;
import io.innocentdream.math.BiContainer;
import io.innocentdream.utils.Utils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL15.*;

public class ModelLoader {

    private final Map<Float[], BiContainer<Integer, Integer>> MODELS = new HashMap<>();

    private final List<Integer> vaos = new ArrayList<>();
    private final List<Integer> vbos = new ArrayList<>();

    public Model loadToVAO(float[] positions, float[] textureCoordinates) {
        if (MODELS.containsKey(Utils.convertToClassArray(positions))) {
            BiContainer<Integer, Integer> holder = MODELS.get(
                    Utils.convertToClassArray(Utils.join(positions, textureCoordinates)));
            return new Model(holder.x != null ? holder.x : 0, positions.length / 3);
        }
        int vaoID = createVAO();
        BiContainer<Integer, Integer> holder = new BiContainer<>();
        holder.x = vaoID;
        holder.y = bindIndicesBuffer(new int[] { 0, 1, 3, 1, 2, 3 });
        MODELS.put(Utils.convertToClassArray(Utils.join(positions, textureCoordinates)), holder);
        storeDataInList(0, 3, positions);
        storeDataInList(1, 2, textureCoordinates);
        unbindVAO();
        return new Model(vaoID, positions.length / 3);
    }

    public Model loadToVAO(float[] positions) {
        if (MODELS.containsKey(Utils.convertToClassArray(positions))) {
            BiContainer<Integer, Integer> holder = MODELS.get(Utils.convertToClassArray(positions));
            return new Model(holder.x != null ? holder.x : 0, positions.length / 3);
        }
        int vaoID = createVAO();
        storeDataInList(0, 2, positions);
        storeDataInList(1, 2, new float[] { 0, 1, 0, 0, 1, 0, 1, 1 });
        unbindVAO();
        BiContainer<Integer, Integer> holder = new BiContainer<>();
        holder.x = vaoID;
        MODELS.put(Utils.convertToClassArray(positions), holder);
        return new Model(vaoID, positions.length / 2);
    }

    public int bindIndicesBuffer(int[] indices) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = toIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        return vbo;
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
