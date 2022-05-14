package libayon.module.modules.player;

import org.lwjgl.input.Keyboard;

import libayon.event.events.client.UpdateEvent;
import libayon.event.events.packets.PacketReceivedEvent;
import libayon.event.events.packets.PacketSentEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;

public class Phase extends Module {
	public Phase() {
		super("Phase", "Phase through blocks", Keyboard.KEY_V, Category.PLAYER, Mode.VULCAN, Mode.AAC);
	}

	@Override
	public void setup() {
	}

	@Override
	public void onEnable() {
		switch (getMode()) {
		case AAC:
			mc.thePlayer.sendQueue.addToSendQueue(new C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY-0.0000001, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
			mc.thePlayer.sendQueue.addToSendQueue(new C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY-1, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
			toggle();
			break;
		case VULCAN:
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY-3, mc.thePlayer.posZ);
			toggle();
			break;
		}
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onUpdate(UpdateEvent event) {
	}
	

	@Override
	public void onPacketSent(PacketSentEvent event) {
	}
	@Override
	public void onPacketReceived(PacketReceivedEvent event) {
		
		
	}

}