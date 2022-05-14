package libayon.module.modules.player;

import libayon.BattleBonk;
import libayon.event.events.client.UpdateEvent;
import libayon.event.events.packets.PacketReceivedEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.BlockPos;

public class AntiVoid extends Module {
	public double FallDistance = 2.5;
	public boolean fell;
	public BlockPos block;
	public AntiVoid() {
		super("Antivoid", "Saves you from the void", 0, Category.PLAYER, Mode.VULCAN);
	}

	@Override
	public void setup() {
	}

	@Override
	public void onEnable() {
		fell = false;
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if (fell) {
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionZ = 0;
			mc.gameSettings.keyBindLeft.setPressed(false);
			mc.gameSettings.keyBindRight.setPressed(false);
			mc.gameSettings.keyBindForward.setPressed(false);
			mc.gameSettings.keyBindBack.setPressed(true);
			mc.gameSettings.keyBindJump.setPressed(false);
		}
		if (fell && mc.thePlayer.onGround) {
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionZ = 0;
		}
		if (mc.thePlayer.fallDistance > FallDistance && !BattleBonk.instance.getModuleManager().getModule("Longjump").isToggled()) {
			if (checkvoid()) {
				block = new BlockPos(mc.thePlayer.posX,mc.thePlayer.posY,mc.thePlayer.posZ);
				mc.theWorld.setBlockState(new BlockPos(mc.thePlayer.posX,mc.thePlayer.posY - 1,mc.thePlayer.posZ), Blocks.barrier.getDefaultState());
				System.out.println(block);
				fell = true;
			}
		}
	}
	

	@Override
	public void onPacketReceived(PacketReceivedEvent event) {
		if (event.getPacket() instanceof S08PacketPlayerPosLook) {
			System.out.println("After" + " " + block);
			fell = false;
			mc.gameSettings.keyBindBack.setPressed(false);
		}
		if (event.getPacket() instanceof S23PacketBlockChange) {
			if (fell) {
				event.setCancelled(true);
			}
		}
		
		
	}
	public void cancelMotion() {
		mc.thePlayer.motionY = 0;
		mc.thePlayer.motionZ = 0;
		mc.thePlayer.motionX = 0;
		mc.gameSettings.keyBindForward.setPressed(false);
		mc.gameSettings.keyBindLeft.setPressed(false);
		mc.gameSettings.keyBindRight.setPressed(false);
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