package libayon.module.modules.player;

import libayon.event.events.packets.PacketSentEvent;
import libayon.event.events.player.PreMotionEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;

import java.util.LinkedList;

public class Blink extends Module {
    public LinkedList<Packet<NetHandlerPlayServer>> packets = new LinkedList<>();

    public Blink() {
        super("Blink", "Delays all packets", 0, Category.PLAYER, Mode.NONE);
    }

    @Override
    public void onPreMotion(PreMotionEvent e) {
    }

    @Override
    public void onDisable() {
        for (Packet<NetHandlerPlayServer> packet : packets) {
            mc.getNetHandler().getNetworkManager().sendPacketWithoutEvent(packet);
        }
        packets.clear();
    }

    @Override
    public void onPacketSent(PacketSentEvent e) {
        packets.add((Packet) e.getPacket());
        e.setCancelled(true);
    }
}
