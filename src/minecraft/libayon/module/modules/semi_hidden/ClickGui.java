package libayon.module.modules.semi_hidden;

import org.lwjgl.input.Keyboard;

import libayon.BattleBonk;
import libayon.gui.clickgui.components.ModuleButton;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;

public class ClickGui extends Module {

	public ClickGui() {
		super("ClickGui", "This is the ClickGui", Keyboard.KEY_RSHIFT, Category.SEMI_HIDDEN, Mode.VANILLA);
	}

	@Override
	public void onEnable() {
		mc.displayGuiScreen(BattleBonk.instance.getClickGui());
		super.toggle();
	}
	

}