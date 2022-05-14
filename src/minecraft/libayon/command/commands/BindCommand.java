package libayon.command.commands;

import java.util.ArrayList;
import java.util.List;

import libayon.BattleBonk;
import libayon.command.Command;
import libayon.module.Category;
import libayon.module.Module;
import libayon.module.ModuleManager;
import libayon.utils.KeyUtils;

public class BindCommand extends Command {

	public BindCommand() {
		super("bind", "bind <module> <key>", "Bind a module to a key");
	}

	@Override
	public String executeCommand(String line, String[] args) {
		if (args.length < 2)
			return getSyntax("&c");

		ModuleManager moduleManager = BattleBonk.instance.getModuleManager();
		Module module = moduleManager.getModule(args[0]);

		if (module == null)
			return String.format("&cThe module &e%1$s&c does not exist.", args[0]);

		if (module.getCategory().equals(Category.HIDDEN))
			return String.format("&cThe module &e%1$s&c is hidden.", args[0]);

		int key = KeyUtils.getKey(args[1]);

		module.setKey(key);

		return String.format("&aThe module &e%1$s&a has been bound to &d%2$s", module.getName(), KeyUtils.getKeyName(key));
	}

}