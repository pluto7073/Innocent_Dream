package io.innocentdream.rendering;

import io.innocentdream.objects.texts.CharacterManager;
import io.innocentdream.utils.Identifier;
import io.innocentdream.utils.LanguageManager;
import io.innocentdream.utils.Task;
import io.innocentdream.utils.Utils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ResourceManager implements Task {

    public static final Logger LOGGER = LogManager.getLogger("ResourceManager");

    static {
        LOGGER.setLevel(Level.ALL);
    }

    private static final Path PATH_TO_RESOURCES = Utils.make(() -> {
        String path = "/assets/.idaccess";
        URL url = ResourceManager.class.getResource(path);
        if (url == null) {
            LOGGER.warn("Could not pre-load resources");
            return null;
        }
        try {
            URI uri = url.toURI();
            String scheme = uri.getScheme();
            if (!scheme.equals("jar") && !scheme.equals("file")) {
                LOGGER.warn("URL '%s' uses unexpected schema".formatted(uri.toString()));
            }
            Path p = Utils.getPath(uri);
            return p.getParent();
        } catch (URISyntaxException | IOException e) {
            LOGGER.error("Could not pre-load resources", e);
            return null;
        }
    });

    private static Map<Identifier, Resource> RESOURCES;

    public static Map<Identifier, Resource> findResources(String folder, Predicate<Identifier> allowedMatches) {
        return new HashMap<>();
    }

    public static Set<String> getAllNamespaces() {
        List<String> namespaces = new ArrayList<>();
        for (Identifier i : RESOURCES.keySet()) {
            if (!namespaces.contains(i.getNamespace())) {
                namespaces.add(i.getNamespace());
            }
        }
        return Set.of(namespaces.toArray(new String[]{}));
    }

    public static Collection<Resource> getAllResources() {
        return RESOURCES.values();
    }

    public static Resource get(Identifier id) {
        return RESOURCES.get(id);
    }

    public static Resource getOrLoad(Identifier id) {
        if (RESOURCES != null && RESOURCES.containsKey(id)) {
            return RESOURCES.get(id);
        } else {
            return new Resource(id.getNamespace() + "/" + id.getPath(), id.getNamespace());
        }
    }

    private static void reload() {
        try (Stream<Path> stream = Files.walk(PATH_TO_RESOURCES)){
            Stream<Identifier> ids = stream.filter((path) -> !path.endsWith(".idaccess") && Files.isRegularFile(path)).mapMulti((path, consumer) -> {
                String relative = PATH_TO_RESOURCES.relativize(path).toString().replace("\\", "/");
                consumer.accept(new Identifier("assets", relative));
            });
            ids.forEach(id -> {
                Resource resource = new Resource(id.getNamespace() + "/" + id.getPath(), id.getNamespace());
                RESOURCES.put(id, resource);
            });
            LanguageManager.load();
        } catch (IOException e) {
            LOGGER.error("Could not reload resources", e);
        }
    }

    public static void init() {
        RESOURCES = new HashMap<>();
        if (PATH_TO_RESOURCES != null) {
            reload();
        } else {
            LOGGER.fatal("An unknown error occurred...");
            System.exit(-1);
        }
    }

    @Override
    public void preformTask() {
        init();
    }

    @Override
    public String getTaskName() {
        return "id.loading.resources";
    }
}
