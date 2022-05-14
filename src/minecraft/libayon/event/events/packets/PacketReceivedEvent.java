package libayon.event.events.packets;

import libayon.event.CancellableEvent;
import net.minecraft.network.Packet;

public class PacketReceivedEvent extends CancellableEvent {

	private Packet<?> packet;

	public PacketReceivedEvent(Packet<?> packet) {
		this.packet = packet;
	}

	public Packet<?> getPacket() {
		return packet;
	}

	public void setPacket(Packet<?> packet) {
		this.packet = packet;
	}

}