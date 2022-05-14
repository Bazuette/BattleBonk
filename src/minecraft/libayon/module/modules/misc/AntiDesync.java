package libayon.module.modules.misc;

import libayon.event.events.packets.PacketSentEvent;
import libayon.event.events.player.PreMotionEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class AntiDesync extends Module {
    public int slot;

    public AntiDesync() {
        super("AntiDesync", "Prevents you from getting desynced", 0, Category.MISC, Mode.VANILLA);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onPacketSent(PacketSentEvent e) {
        if (e.getPacket() instanceof C09PacketHeldItemChange) {
            slot = ((C09PacketHeldItemChange) e.getPacket()).getSlotId();
        }
    }

    @Override
    public void onPreMotion(PreMotionEvent e) {
        if (slot != mc.thePlayer.inventory.currentItem && slot != -1) {
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            System.out.println("Sent");
        }
    }
}