package libayon.module.modules.player;

import libayon.event.events.client.UpdateEvent;
import libayon.event.events.packets.PacketReceivedEvent;
import libayon.event.events.packets.PacketSentEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class NoFall extends Module {
	public NoFall() {
		super("NoFall", "Autobridge lol", 0, Category.PLAYER, Mode.VULCAN, Mode.AAC);
	}

	@Override
	public void setup() {
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		switch (getMode()) {
		case VULCAN:
			if (mc.thePlayer.fallDistance > 2.5 && !checkvoid()) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
				mc.thePlayer.fallDistance = 0;
				mc.thePlayer.motionY = -0.02;
			}
			break;
		}
	}
	

	@Override
	public void onPacketSent(PacketSentEvent event) {
	}
	@Override
	public void onPacketReceived(PacketReceivedEvent event) {
		
		
	}
	public Boolean checkvoid() {
		Boolean Void = true;
		for (int i = 0;i<mc.thePlayer.posY-1;i++) {
			if (!mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX,i,mc.thePlayer.posZ))) {
				Void = false;
			}
        }
		return Void;
	}

}