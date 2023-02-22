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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.lwjgl.BufferUtils.createByteBuffer;

public final class Utils {

    public static File RUN_DIR;
    public static final Map<String, String> RUN_ARGS = new HashMap<>();

    private Utils() {}

    private static void setupRunDir() {
        File runDir = newFile("run");

        if (RUN_ARGS.containsKey("runDir")) {
            String specifiedRunDirPath = RUN_ARGS.get("runDir");
            InnocentDream.logger.info("Run args contains run dir path: %s".formatted(specifiedRunDirPath));
            File testDir = newFile(specifiedRunDirPath);
            runDir = testDir;
        }

        runDir.mkdirs();
        RUN_DIR = runDir;
    }

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

    public static File newFileInRunDir(String subPath) {
        subPath = subPath.replace("\\", File.separator);
        return new File(RUN_DIR, subPath);
    }

    /**
     * Tests if an array contains a specified object
     * @param array The array to test from
     * @param object The object ot look for
     * @return If the array contains this object
     * @param <T> The Datatype of the array
     */
    public static <T> boolean arrayContains(T[] array, T object) {
        for (T t : array) {
            if (t.equals(object)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the index of an object in an array
     * @param array The array to look in
     * @param object The object to look for
     * @return The index of the first instance of the object found, <code>-1</code> if not found
     * @param <T> The datatype of the array
     */
    public static <T> int findIndex(T[] array, T object) {
        for (int i = 0; i < array.length; i++) {
            T t = array[i];
            if (t.equals(object)) {
                return i;
            }
        }
        return -1;
    }

    public static void init(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("--")) {
                int index = findIndex(args, arg) + 1;
                String property = arg.replace("--", "");
                String value = args[index];
                RUN_ARGS.put(property, value);
            }
        }

        setupRunDir();
    }

}
