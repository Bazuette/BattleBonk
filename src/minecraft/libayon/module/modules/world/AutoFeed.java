package libayon.module.modules.world;

import libayon.event.events.client.UpdateEvent;
import libayon.event.events.packets.PacketSentEvent;
import libayon.event.events.player.PreMotionEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;

public class AutoFeed extends Module {
    public AutoFeed() {
        super("AutoFeed", "Just for fun lol", 0, Category.WORLD, Mode.VANILLA);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onUpdate(UpdateEvent e) {
        mc.gameSettings.keyBindForward.setPressed(true);
        mc.thePlayer.setSprinting(false);
    }

    @Override
    public void onPacketSent(PacketSentEvent e) {
    }

    @Override
    public void onPreMotion(PreMotionEvent e) {
        if (mc.thePlayer.fallDistance > 4) {
            mc.gameSettings.keyBindForward.setPressed(false);
        }
    }
}