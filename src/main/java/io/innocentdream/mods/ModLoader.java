package io.innocentdream.mods;

import io.innocentdream.utils.Task;
import io.innocentdream.utils.Utils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModLoader implements Task {

    public static final Logger LOGGER = LogManager.getLogger("ModLoader");
    public static final List<Mod> LOADED_MODS = new ArrayList<>();

    static {
        LOGGER.setLevel(Level.ALL);
    }

    public static void init() {
        LOGGER.setLevel(Level.ALL);
        File modFolder = Utils.newFileInRunDir("mods");
        modFolder.mkdirs();
        File[] modFiles = modFolder.listFiles();
        if (modFiles == null) {
            return;
        }
        for (File file : modFiles) {
            if (!file.getName().endsWith(".jar")) {
                continue;
            }
            try {
                Mod mod = Mod.loadModFromJar(file);
                LOGGER.info("Loaded Mod: " + mod.getId());
                LOADED_MODS.add(mod);
            } catch (ModInitializationException e) {
                LOGGER.error("Error Occurred in loading Mod: \n", e);
            }
        }
    }

    @Override
    public void preformTask() {
        init();
    }

    @Override
    public String getTaskName() {
        return "id.loading.mods";
    }
}
