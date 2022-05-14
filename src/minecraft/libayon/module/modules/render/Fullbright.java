package libayon.module.modules.render;

import libayon.event.events.client.UpdateEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;

public class Fullbright extends Module {

	public Fullbright() {
		super("Fullbright", "Use if you are GAMMA Blind", 0, Category.RENDER,Mode.NONE);
	}



	@Override
	public void onUpdate(UpdateEvent e) {
		mc.gameSettings.gammaSetting = 100;
	}

	@Override
	public void onDisable() {
		mc.gameSettings.gammaSetting = 1;
	}
}
