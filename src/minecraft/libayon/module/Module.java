package libayon.module;

import libayon.BattleBonk;
import libayon.event.EventListener;
import libayon.notifications.NotificationManager;
import libayon.utils.Random;
import libayon.utils.Strings;
import libayon.utils.timers.Timer;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Module extends EventListener {
    protected String name;

    protected String description;

    protected int key;

    protected Category category;

    protected Mode[] allowedModes;

    protected Mode mode;

    protected ModuleSettings moduleSettings;
    protected Color color;
    protected Timer timer;
    protected ExecutorService singleExecutorService;
    protected ExecutorService executorService;
    private boolean toggled;
    private boolean showInModuleArraylist;

    public Module(String name, String description, int key, Category category, Mode... allowedModes) {
        initializeModule(name, description, key, category, !category.equals(Category.HIDDEN), false, allowedModes);
    }

    public Module(String name, String description, int key, Category category, boolean showInModuleArrayList, Mode... allowedModes) {
        initializeModule(name, description, key, category, showInModuleArrayList, false, allowedModes);
    }

    public Module(String name, String description, int key, Category category, boolean showInModuleArrayList, boolean toggled, Mode... allowedModes) {
        initializeModule(name, description, key, category, showInModuleArrayList, toggled, allowedModes);
    }

    private void initializeModule(String name, String description, int key, Category category, boolean showInModuleArrayList, boolean toggled, Mode... allowedModes) {
        this.name = name;
        this.description = description;
        this.key = key;
        this.category = category;
        (new Mode[1])[0] = Mode.VANILLA;
        allowedModes = (allowedModes == null || allowedModes.length == 0) ? new Mode[1] : allowedModes;
        this.allowedModes = allowedModes;
        this.mode = allowedModes[0];
        this.showInModuleArraylist = showInModuleArrayList;
        this.moduleSettings = new ModuleSettings(this);
        this.timer = new Timer(true);
        this.singleExecutorService = Executors.newFixedThreadPool(1);
        this.executorService = Executors.newCachedThreadPool();
        loadFromSettings();
        setup();
        randomColor();
    }

    public void loadFromSettings() {
        moduleSettings.getConfigYaml().addDefault(name + ".mode", String.valueOf(allowedModes[0]));
        moduleSettings.getConfigYaml().addDefault(name + ".toggled", isToggled());
        moduleSettings.getConfigYaml().addDefault(name + ".key", getKey());

        BattleBonk.instance.getConfigYaml().options().copyDefaults(true);
        BattleBonk.instance.saveConfig();

        if (BattleBonk.instance.getConfigYaml().getBoolean(name + ".toggled")) {
            this.toggled = true;
            BattleBonk.instance.getEventManager().registerListener(this);
        } else {
            this.toggled = false;
        }
        this.key = moduleSettings.getConfigYaml().getInt(name + ".key");
        //this.mode = Mode.valueOf(this.moduleSettings.getString(name + ".mode").toUpperCase());
        this.mode = Mode.valueOf(moduleSettings.getConfigYaml().getString(name + ".mode").toUpperCase());
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
        this.moduleSettings.set(name + ".key", key);
    }

    public Category getCategory() {
        return this.category;
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setMode(Mode mode) {
        if (!Arrays.stream(allowedModes).anyMatch(mode::equals))
            return;
        moduleSettings.set("mode", String.valueOf(mode));
        this.mode = Mode.valueOf(moduleSettings.getConfigYaml().getString(name + ".mode"));

    }

    public Mode[] getallowedModes() {
        return this.allowedModes;
    }

    public boolean isShownInModuleArrayList() {
        return this.showInModuleArraylist;
    }

    public ModuleSettings getModuleSettings() {
        return this.moduleSettings;
    }

    public boolean isToggled() {
        return this.toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        NotificationManager.createNotification(getName());
        mc.thePlayer.playSound("random.click", 3F, (float) (toggled ? 1.2 : 0.7));
        if (toggled) {
            randomColor();
            this.moduleSettings.set("toggled", true);
            BattleBonk.instance.getEventManager().registerListener(this);
            onEnable();
        } else {
            this.moduleSettings.set("toggled", false);
            BattleBonk.instance.getEventManager().unregisterListener(this);
            onDisable();
        }
    }

    public Color getColor() {
        return this.color;
    }

    public String getNameWithMode() {
        return this.name + (this.mode.equals(Mode.NONE) ? "" : ("&6[&f" + (Strings.capitalizeFirstLetter(this.mode.name()) + "&6]")));
    }

    public void setup() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void toggle() {
        setToggled(!this.toggled);

    }

    private void randomColor() {
        this.color = Random.getRandomLightColor();
    }

    public Timer getTimer() {
        return this.timer;
    }
}
