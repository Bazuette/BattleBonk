package libayon.module.modules.misc;

import io.netty.buffer.Unpooled;
import libayon.event.events.packets.PacketSentEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import libayon.utils.ChatUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class ClientSpoofer extends Module {

	public ClientSpoofer() {
		super("ClientSpoofer", "Fake what client you have", 0, Category.MISC,Mode.LUNAR);
	}
	@Override
	public void onEnable() {
		ChatUtils.print("Please rejoin if you want the ClientSpoofer to work");
	}
	@Override
	public void onDisable() {
		
	}
	@Override
	public void onPacketSent(PacketSentEvent e) {
		if (e.getPacket() instanceof C17PacketCustomPayload) {
			C17PacketCustomPayload c17 = (C17PacketCustomPayload) e.getPacket();
			c17.setChannel("REGISTER");
			c17.setData(createPacketBuffer("Lunar-Client", false));
		}
	}
	private PacketBuffer createPacketBuffer(String data, boolean string) {
	    if (string)
	      return (new PacketBuffer(Unpooled.buffer())).writeString(data); 
	    return new PacketBuffer(Unpooled.wrappedBuffer(data.getBytes()));
	  }

}