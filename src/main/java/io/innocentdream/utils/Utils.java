package io.innocentdream.utils;

import io.innocentdream.InnocentDream;
import io.innocentdream.rendering.DisplayManager;
import io.innocentdream.rendering.ResourceManager;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.util.*;
import java.util.function.Supplier;

import static org.lwjgl.BufferUtils.createByteBuffer;

public final class Utils {

    public static File RUN_DIR;
    public static final Map<String, String> RUN_ARGS = new HashMap<>();

    private static final float CONSTANT_MODIFIER_X = 256f / 3840f;
    private static final float CONSTANT_MODIFIER_Y = 256f / 2160f;
    private static final int DESIRED_WIDTH = 3840;
    private static final int DESIRED_HEIGHT = 2160;
    private static final List<File> TEMP_FILES = new ArrayList<>();

    private Utils() {}

    private static void setupRunDir() {
        File runDir = newFile("run");

        if (System.getenv().containsKey("innocentdream.runDir")) {
            String specifiedRunDirPath = System.getenv("innocentdream.runDir");
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
     *
     * @return the resource data
     *
     * @throws IOException if an IO error occurs
     */
    public static ByteBuffer ioResourceToByteBuffer(Identifier resource) throws IOException {
        return inputStreamToByteBuffer(ResourceManager.get(resource).get());
    }

    public static ByteBuffer inputStreamToByteBuffer(InputStream stream) throws IOException {
        ByteBuffer buffer;

        ReadableByteChannel rbc = Channels.newChannel(stream);
        buffer = createByteBuffer(1);

        while (true) {
            int bytes = rbc.read(buffer);
            if ( bytes == -1 ) {
                break;
            }
            if (buffer.remaining() == 0) {
                buffer = resizeBuffer(buffer, buffer.capacity() * 2);
            }
        }

        buffer.flip();
        return buffer;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File createTempFile(String name) throws IOException {
        File tempFolder = newFileInRunDir("temp");
        tempFolder.mkdirs();
        File file = new File(tempFolder, name);
        file.getParentFile().mkdirs();
        file.createNewFile();
        TEMP_FILES.add(file);
        return file;
    }

    public static float[] convertScreenToPixelCoordinates(float[] screenCoordinates) {
        float x = screenCoordinates[0] * DisplayManager.WIDTH;
        float y = screenCoordinates[1] * DisplayManager.HEIGHT;
        return new float[] { x, y };
    }

    public static float[] convertPixelToScreenCoordinates(float[] pixelCoordinates) {
        float x = pixelCoordinates[0] / DisplayManager.WIDTH;
        float y = pixelCoordinates[1] / DisplayManager.HEIGHT;
        return new float[] { x, y };
    }

    public static float[] convertWorldToScreenCoordinates(float[] worldCoordinates) {
        float[] pixelCoords = convertWorldToPixelCoordinates(worldCoordinates);
        return convertPixelToScreenCoordinates(pixelCoords);
    }

    public static float[] convertWorldToPixelCoordinates(float[] worldCoordinates) {
        float modX = CONSTANT_MODIFIER_X * DisplayManager.WIDTH;
        float modY = CONSTANT_MODIFIER_Y * DisplayManager.HEIGHT;
        float newX = worldCoordinates[0] * modX;
        float newY = worldCoordinates[1] * modY;
        return new float[] { newX, newY };
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

    public static int countArrayOccurrences(Object[] array, Object object) {
        int count = 0;
        for (Object t : array) {
            if (t.equals(object)) {
                count++;
            }
        }
        return count;
    }

    public static int countArrayOccurrences(int[] array, int object) {
        int count = 0;
        for (int i : array) {
            if (i == object) {
                count++;
            }
        }
        return count;
    }

    public static int countArrayOccurrences(char[] array, char character) {
        int count = 0;
        for (char c : array) {
            if (c == character) {
                count++;
            }
        }
        return count;
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

    @SuppressWarnings("DuplicateExpressions")
    public static Path getPath(URI uri) throws IOException {
        try {
            return Paths.get(uri);
        } catch (FileSystemNotFoundException ignored) {
        } catch (Throwable e) {
            InnocentDream.logger.warn("Unable to get path for: " + uri, e);
        }

        try {
            FileSystems.newFileSystem(uri, Collections.emptyMap());
        } catch (FileSystemAlreadyExistsException ignored) {
        }
        return Paths.get(uri);
    }

    public static Float[] convertToClassArray(float[] array) {
        Float[] arr = new Float[array.length];
        for (int i = 0; i < array.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    public static float[] join (float[] arr1, float[] arr2) {
        float[] arr3 = new float[arr1.length + arr2.length];
        int i;
        for (i = 0; i < arr1.length; i++) {
            arr3[i] = arr1[i];
        }
        for (float f : arr2) {
            arr3[i] = f;
            i++;
        }
        return arr3;
    }

    public static <T> T make(Supplier<T> supplier) {
        return supplier.get();
    }

    public static void cleanUp() {
        for (File f : TEMP_FILES) {
            if (f.exists()) {
                f.delete();
            }
        }
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
