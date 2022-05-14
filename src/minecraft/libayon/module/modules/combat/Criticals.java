package libayon.module.modules.combat;

import java.util.ArrayList;
import java.util.List;

import libayon.BattleBonk;
import libayon.event.events.*;
import libayon.event.events.client.UpdateEvent;
import libayon.event.events.combat.AttackEvent;
import libayon.event.events.packets.PacketReceivedEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class Criticals extends Module {
	public Criticals() {
		super("Criticals", "Automatically Crits for you", 0, Category.COMBAT,Mode.PACKET,Mode.VULCAN,Mode.VANILLA);
	}
	
	public void setup() {
	}
	public void onEnable() {
	}
	@Override
	public void onPacketReceived(PacketReceivedEvent event) {
	}
	@Override
	public void onAttack(AttackEvent event) {
		if (mc.thePlayer.movementInput.jump)
			return;
		switch (getMode()) {
		case PACKET:
			if (mc.thePlayer.onGround) {
				double[] values = {0.0625,0.001 - Math.random() / 10000.0};
				for (double d : values) {
					mc.getNetHandler().getNetworkManager().sendPacketWithoutEvent(new C04PacketPlayerPosition(mc.thePlayer.posX,mc.thePlayer.posY + d,mc.thePlayer.posZ,false));
				}
			}
			
			break;

		default:
			break;
		}
	}

}