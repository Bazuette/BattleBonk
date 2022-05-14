package libayon.utils.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class PacketUtils {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static void sendPacket(Packet<?> packet) {
        if (mc.thePlayer != null) {
            mc.getNetHandler().getNetworkManager().sendPacket(packet);
        }
    }

    public static void sendPacketNoEvent(Packet packet) {
        mc.getNetHandler().getNetworkManager().sendPacketWithoutEvent(packet);
    }


}
