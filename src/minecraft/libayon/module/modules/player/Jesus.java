package libayon.module.modules.player;

import libayon.event.events.packets.PacketSentEvent;
import libayon.event.events.player.PostMotionEvent;
import libayon.event.events.player.PreMotionEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import libayon.utils.BlockUtils;
import libayon.utils.player.PlayerUtils;

public class Jesus extends Module {
    public Jesus() {
        super("Jesus", "Walk on water lol", 0, Category.PLAYER, Mode.VULCAN, Mode.VERUS, Mode.SPARTAN);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onPacketSent(PacketSentEvent e) {
        if (mc.thePlayer.isInLava()) {
            mc.thePlayer.motionY = 0.8;
        }
        if (BlockUtils.isInLiquid() || BlockUtils.isOnLiquid() && !mc.thePlayer.isInLava()) {
            if (mc.thePlayer.isCollidedHorizontally) {
                mc.thePlayer.motionY = 0.4;
            } else {
                mc.thePlayer.motionY = (mc.thePlayer.ticksExisted % 5 == 0 ? 0.4 : -0.03);
                PlayerUtils.speed((mc.thePlayer.ticksExisted % 5 == 0 ? 0.05 : 0.07), true);
            }
        }
    }

    @Override
    public void onPreMotion(PreMotionEvent e) {
    }

    @Override
    public void onPostMotion(PostMotionEvent e) {
    }
}
