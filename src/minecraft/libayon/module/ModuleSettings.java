package libayon.module;

import libayon.BattleBonk;
import org.apache.commons.lang3.StringUtils;
import org.simpleyaml.configuration.file.YamlFile;

import java.util.List;

public class ModuleSettings {
    private final Module module;
    private BattleBonk bb = BattleBonk.instance;
    private boolean wasGenerated;

    public ModuleSettings(Module module) {
        this.module = module;
        bb.saveConfig();

    }

    public Module getModule() {
        return this.module;
    }


    public YamlFile getConfigYaml() {
        return bb.getConfigYaml();
    }

    public boolean wasGenerated() {
        return this.wasGenerated;
    }


    public void addDefault(String key, String value) {

        bb.getConfigYaml().addDefault(module.getName() + "." + key, value);
        bb.getConfigYaml().options().copyDefaults(true);
        bb.saveConfig();
    }

    public void addDefault(String key, boolean value) {
        bb.getConfigYaml().addDefault(module.getName() + "." + key, value);
        bb.getConfigYaml().options().copyDefaults(true);
        bb.saveConfig();
    }

    public void addDefault(String key, int value) {
        bb.getConfigYaml().addDefault(module.getName() + "." + key, value);
        bb.getConfigYaml().options().copyDefaults(true);
        bb.saveConfig();
    }

    public void addDefault(String key, char value) {
        bb.getConfigYaml().addDefault(module.getName() + "." + key, value);
        bb.getConfigYaml().options().copyDefaults(true);
        bb.saveConfig();
    }

    public void addDefault(String key, double value) {
        bb.getConfigYaml().addDefault(module.getName() + "." + key, value);
        bb.getConfigYaml().options().copyDefaults(true);
        bb.saveConfig();
    }

    public void addDefault(String key, float value) {
        bb.getConfigYaml().addDefault(module.getName() + "." + key, value);
        bb.getConfigYaml().options().copyDefaults(true);
        bb.saveConfig();
    }

    public void addDefault(String key, List<String> value) {
        bb.getConfigYaml().addDefault(module.getName() + "." + key, value);
        bb.getConfigYaml().options().copyDefaults(true);
        bb.saveConfig();
    }

    public void set(String key, Object value) {
        bb.getConfigYaml().set(module.getName() + "." + key, value);
        bb.getConfigYaml().options().copyDefaults(true);
        bb.saveConfig();
    }

    public void set(String key, String value) {
        bb.getConfigYaml().set(module.getName() + "." + key, value);
        bb.getConfigYaml().options().copyDefaults(true);
        bb.saveConfig();
    }

    public void set(String key, boolean value) {
        bb.getConfigYaml().set(module.getName() + "." + key, value);
        bb.getConfigYaml().options().copyDefaults(true);
        bb.saveConfig();
    }

    public void set(String key, int value) {
        bb.getConfigYaml().set(module.getName() + "." + key, value);
        bb.getConfigYaml().options().copyDefaults(true);
        bb.saveConfig();
    }

    public void set(String key, char value) {
        bb.getConfigYaml().set(module.getName() + "." + key, value);
        bb.getConfigYaml().options().copyDefaults(true);
        bb.saveConfig();
    }

    public void set(String key, double value) {
        bb.getConfigYaml().set(module.getName() + "." + key, value);
        bb.getConfigYaml().options().copyDefaults(true);
        bb.saveConfig();
    }

    public void set(String key, float value) {
        bb.getConfigYaml().set(module.getName() + "." + key, value);
        bb.getConfigYaml().options().copyDefaults(true);
        bb.saveConfig();
    }

    public void set(String key, List<String> value) {
        bb.getConfigYaml().set(module.getName() + "." + key, value);
        bb.getConfigYaml().options().copyDefaults(true);
        bb.saveConfig();
    }

    public String getString(String key) {
        return bb.getConfigYaml().getString(module.getName() + "." + StringUtils.capitalize(key));
    }

    public boolean getBoolean(String key) {
        return bb.getConfigYaml().getBoolean(module.getName() + "." + StringUtils.capitalize(key));
    }

    public double getDouble(String key) {
        return bb.getConfigYaml().getDouble(module.getName() + "." + StringUtils.capitalize(key));
    }


    public float getFloat(String key) {
        return bb.getConfigYaml().getLong(module.getName() + "." + StringUtils.capitalize(key));
    }

    public Object getObject(String key) {
        return bb.getConfigYaml().get(module.getName() + "." + StringUtils.capitalize(key));
    }

    public int getInt(String key) {
        return bb.getConfigYaml().getInt(module.getName() + "." + StringUtils.capitalize(key));
    }

    public List<String> getStringList(String key) {
        return bb.getConfigYaml().getStringList(module.getName() + "." + key);
    }

}
