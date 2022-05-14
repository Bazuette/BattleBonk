package libayon.command.commands;

import libayon.BattleBonk;
import libayon.command.Command;
import libayon.module.Module;
import libayon.utils.ChatUtils;
import libayon.utils.Strings;
import libayon.utils.config.Files;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;

public class ConfigCommand extends Command {

    public ConfigCommand() {
        super("config", "config <|create|load|list|path> <cfg>", "Manage the config");

    }

    @Override
    public String executeCommand(String line, String[] args) {
        if (args.length < 1) return getSyntax("&c");
        if (args[0].equalsIgnoreCase("path")) {
            return String.format(BattleBonk.instance.configYaml.getFilePath());
        }
        if (args[0].equalsIgnoreCase("list")) {
            ChatUtils.print(Strings.translateColors("&eList of configs:"));
            for (String name : Files.listFilesUsingJavaIO(BattleBonk.instance.getClientName() + "/configs")) {
                String[] split = name.split("\\.");
                ChatUtils.print(Strings.translateColors("&a " + split[0]));
            }

            return "";
        }
        if (args.length < 2)
            return getSyntax("&c");

        switch (args[0].toLowerCase()) {
            case "create": {
                BattleBonk.instance.configYaml = new YamlFile(BattleBonk.instance.getClientName() + "/configs/" + args[1] + ".yml");
                for (Module m : BattleBonk.instance.getModuleManager().getModules()) {
                    m.getModuleSettings().getModule().setup();
                    m.loadFromSettings();
                }
                BattleBonk.instance.getBattleBonkYaml().set("Current Config", BattleBonk.instance.configYaml.getFilePath());
                BattleBonk.instance.saveBattleBonkConfig();
                return String.format("&aCreated the config &e" + args[1]);
            }
            case "load": {
                try {
                    BattleBonk.instance.configYaml = YamlFile.loadConfiguration(new File(BattleBonk.instance.getClientName() + "/configs/" + args[1] + ".yml"));
                    for (Module m : BattleBonk.instance.getModuleManager().getModules()) {
                        m.getModuleSettings().getModule().setup();
                        m.loadFromSettings();
                    }
                    BattleBonk.instance.getBattleBonkYaml().set("Current Config", BattleBonk.instance.configYaml.getFilePath());
                    BattleBonk.instance.saveBattleBonkConfig();
                } catch (IOException e) {
                    return String.format("&cConfig doesn't exist");
                }
                return String.format("&aLoaded the config &e" + args[1]);
            }
            default: {
                return getSyntax("&c");
            }
        }
    }

}