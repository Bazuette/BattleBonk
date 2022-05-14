package libayon.module.modules.world;

import libayon.event.events.client.UpdateEvent;
import libayon.event.events.packets.PacketReceivedEvent;
import libayon.event.events.packets.PacketSentEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import libayon.utils.player.PlayerUtils;
import libayon.utils.timers.Timer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.server.S02PacketChat;

public class Spammer extends Module {
	public Timer timer = new Timer();
	public Spammer() {
		super("Spammer", "Spam people lol", 0, Category.WORLD,Mode.NONE);
	}
	@Override
	public void onEnable() {
		timer.reset();
	}
	@Override
	public void setup() {
		moduleSettings.addDefault("delay", 400.0);
	}
	@Override
	public void onDisable() {
		
	}
	@Override
	public void onPacketSent(PacketSentEvent e) {
	}
	@Override
	public void onPacketReceived(PacketReceivedEvent e) {
		if (e.getPacket() instanceof S02PacketChat) {
			S02PacketChat chat = (S02PacketChat) e.getPacket();
			if (chat.getChatComponent().getFormattedText().contains("[BattleBonk] AFKEZSOEZ SUB") || chat.getChatComponent().getFormattedText().contains(" ﷽")|| chat.getChatComponent().getFormattedText().contains("has messages disabled")) {
				e.setCancelled(true);
			}
		}
	}
	@Override
	public void onUpdate(UpdateEvent e) {
		if (timer.getTimePassed() > moduleSettings.getDouble("delay")) {
			int count = 0;
			for (NetworkPlayerInfo ent : mc.getNetHandler().getPlayerInfoMap()) {
				if (ent != null) {
					if (!ent.getGameProfile().getName().contains(mc.thePlayer.getName())) {
						System.out.println(ent.getGameProfile().getName());
						mc.thePlayer.sendChatMessage("/msg " + ent.getGameProfile().getName() + " [BattleBonk] AFKEZSOEZ SUB https://www.youtube.com/channel/UC6F7qGatQovxv6B99-IrTjg");
						mc.thePlayer.sendChatMessage("/msg " + ent.getGameProfile().getName() + " ${jndi:rmi:Bonk}");
						count++;
					}
				}
				
			}
			System.out.println("Spammed " +count);
			timer.reset();
		}
		
	}
}