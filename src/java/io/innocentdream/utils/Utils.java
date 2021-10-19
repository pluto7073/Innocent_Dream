package io.innocentdream.utils;

import io.innocentdream.InnocentDream;
import io.innocentdream.rendering.ResourceManager;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import static org.lwjgl.BufferUtils.createByteBuffer;

public final class Utils {

    private Utils() {}

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    /**
     * Reads the specified resource and returns the raw data as a ByteBuffer.
     *
     * @param resource   the resource to read
     * @param bufferSize the initial buffer size
     *
     * @return the resource data
     *
     * @throws IOException if an IO error occurs
     */
    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        try (
                InputStream source = ResourceManager.RESOURCES.get(resource).get();
                ReadableByteChannel rbc = Channels.newChannel(source)
        ) {
            buffer = createByteBuffer(bufferSize);

            while (true) {
                int bytes = rbc.read(buffer);
                if ( bytes == -1 )
                    break;
                if ( buffer.remaining() == 0 )
                    buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                }
            }

        buffer.flip();
        return buffer;
    }

    public static String generateUID(Random random) {
        random = new Random();
        StringBuilder uid = new StringBuilder();
        char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        for (int i = 0; i < 8; i++) {
            int x = random.nextInt(16);
            uid.append(hex[x]);
        }
        uid.append('-');
        for (int i = 0; i < 4; i++) {
            int x = random.nextInt(16);
            uid.append(hex[x]);
        }
        uid.append('-');
        for (int i = 0; i < 4; i++) {
            int x = random.nextInt(16);
            uid.append(hex[x]);
        }
        uid.append('-');
        for (int i = 0; i < 4; i++) {
            int x = random.nextInt(16);
            uid.append(hex[x]);
        }
        uid.append('-');
        for (int i = 0; i < 12; i++) {
            int x = random.nextInt(16);
            uid.append(hex[x]);
        }
        return uid.toString();
    }

    public static File newFile(String path) {
        path = path.replace("\\", File.separator);
        return new File(path);
    }

}
