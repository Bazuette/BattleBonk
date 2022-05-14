package libayon.module.modules.combat;

import libayon.event.events.client.UpdateEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;

public class Reach extends Module {
    public static float combatrange;
    public static float blockrange;

    public Reach() {
        super("Reach", "Extends the player's reach", 0, Category.COMBAT, Mode.NONE);
    }

    public void setup() {
        moduleSettings.addDefault("Block Range", 5.0);
        moduleSettings.addDefault("Combat Range", 3.0);
    }

    public void onEnable() {
    }

    @Override
    public void onUpdate(UpdateEvent e) {
        blockrange = moduleSettings.getFloat("Block Range");
        combatrange = moduleSettings.getFloat("Combat Range");
    }
}