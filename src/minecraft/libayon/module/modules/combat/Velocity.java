package libayon.module.modules.combat;

import libayon.BattleBonk;
import libayon.event.events.client.UpdateEvent;
import libayon.event.events.packets.PacketReceivedEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

import java.util.ArrayList;
import java.util.List;

public class Velocity extends Module {
    List<Packet<NetHandlerPlayServer>> packetlist = new ArrayList<Packet<NetHandlerPlayServer>>();

    public Velocity() {
        super("Velocity", "Prevents you from getting knocked off", 0, Category.COMBAT, Mode.AAC, Mode.VULCAN);
    }

    public void setup() {
    }

    public void onEnable() {
    }

    @Override
    public void onPacketReceived(PacketReceivedEvent event) {
        Packet<?> p = event.getPacket();
        if (p instanceof S12PacketEntityVelocity && !BattleBonk.instance.getModuleManager().getModule("Fly").isToggled()) {
            S12PacketEntityVelocity s12 = (S12PacketEntityVelocity) p;
            if (s12.getEntityID() == mc.thePlayer.getEntityId()) {
                switch (getMode()) {
                    case AAC:
                        event.setCancelled(true);
                        break;
                    case VULCAN:
                        break;
                }
            }
        }
    }

    public void onUpdate(UpdateEvent e) {
    }

    public void speed(double speed) {
        double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }

}