package libayon.event.events.packets;

import libayon.event.CancellableEvent;
import net.minecraft.network.Packet;

public class PacketSentEvent extends CancellableEvent {

	private Packet<?> packet;

	public PacketSentEvent(Packet<?> packet) {
		this.packet = packet;
	}

	public Packet<?> getPacket() {
		return packet;
	}

	public void setPacket(Packet<?> packet) {
		this.packet = packet;
	}

}