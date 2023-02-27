package io.innocentdream.screens;

import io.innocentdream.InnocentDream;
import io.innocentdream.objects.AABB;
import io.innocentdream.objects.GUIObject;
import io.innocentdream.objects.texts.CharacterManager;
import io.innocentdream.objects.texts.Text;
import io.innocentdream.rendering.DisplayManager;
import io.innocentdream.rendering.Model;
import io.innocentdream.rendering.ResourceManager;
import io.innocentdream.rendering.TextureHelper;
import io.innocentdream.utils.Identifier;
import io.innocentdream.utils.Task;
import io.innocentdream.utils.Utils;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class LoadScreen extends Screen implements Runnable {

    private static final List<Task> LOADING_TASKS = new ArrayList<>();

    private final GUIObject plutoGamesLogo;
    private final GUIObject innocentDreamLogo;
    private final Text loadingText;
    private int stage;
    private boolean startedLoading;
    private boolean doneLoading;
    private final Date date;
    private final Function<Identifier, ? extends Screen> afterLoadBuilder;

    public LoadScreen(Function<Identifier, ? extends Screen> afterLoadBuilder) {
        super("loading", new int[] { 0, 0, 0, 0 });
        this.plutoGamesLogo = new GUIObject(-512, -512, 1024, 1024, new Identifier("assets", "gui/pluto-games.png"));
        this.innocentDreamLogo = new GUIObject(-512, -512, 1024, 1024, new Identifier("assets", "gui/icon/icon-32.png"));
        this.date = new Date();
        this.stage = 0;
        this.startedLoading = false;
        this.doneLoading = false;
        this.loadingText = new Text("Loading...");
        this.loadingText.setScale(100f);
        this.afterLoadBuilder = afterLoadBuilder;
    }

    @Override
    public void drawScreen() {}

    @Override
    public void drawGUI() {
        switch (stage) {
            case 0 -> this.plutoGamesLogo.draw();
            case 1, 2 -> this.innocentDreamLogo.draw();
        }
        this.loadingText.draw(-loadingText.getWidth() / 2, -512 - loadingText.getHeight() * 2);
    }

    @Override
    public void tick() {
        switch (stage) {
            case 0 -> this.plutoGamesLogo.update();
            case 1, 2 -> this.innocentDreamLogo.update();
        }
        if (checkTime(5000) && stage == 0) {
            stage = 1;
        }

        if (checkTime(7500) && stage == 1) {
            stage = 2;
        }

        if (stage == 2 && !startedLoading) {
            Thread thread = new Thread(this);
            thread.setName("Game Loader");
            thread.start();
            startedLoading = true;
        }

        if (stage == 2 && doneLoading) {
            int index = InnocentDream.display.activeScreens.indexOf(this);
            InnocentDream.display.activeScreens.set(index, afterLoadBuilder.apply(new Identifier("innocent_dream", "load")));
        }
    }

    private boolean checkTime(int millis) {
        Date date = new Date();
        long now = date.getTime();
        long then = this.date.getTime();
        return (now - then) >= millis;
    }

    @Override
    public void run() {
        for (Task t : LOADING_TASKS) {
            loadingText.setText(t.getTaskName() + "...");
            InnocentDream.logger.info(t.getTaskName() + "...");
            t.preformTask();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
        }
        this.doneLoading = true;
    }

    public static void addTask(Task task) {
        LOADING_TASKS.add(task);
    }
}
