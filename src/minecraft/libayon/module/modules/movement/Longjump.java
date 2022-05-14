package libayon.module.modules.movement;

import libayon.event.events.client.UpdateEvent;
import libayon.event.events.packets.PacketReceivedEvent;
import libayon.event.events.packets.PacketSentEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import libayon.utils.network.PacketUtils;
import libayon.utils.player.PlayerUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import org.lwjgl.input.Keyboard;


public class Longjump extends Module {

    public boolean receiveds08;
    public int s08ticks;
    public int ticks;
    public boolean damaged;
    public boolean tried = false;

    public Longjump() {
        super("Longjump", "Allows you to fly", Keyboard.KEY_X, Category.MOVEMENT, Mode.BATTLEASYABEST, Mode.VULCAN, Mode.BATTLEASYAMOTION, Mode.VERUS);
    }

    @Override
    public void setup() {
    }

    @Override
    public void onEnable() {
        ticks = 0;
        damaged = false;
        receiveds08 = false;
        s08ticks = 0;
        tried = false;
        if (getMode() == Mode.BATTLEASYABEST || getMode() == Mode.BATTLEASYAMOTION || getMode() == Mode.BATTLEASYASAFE) {
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 4, mc.thePlayer.posZ);
            if (mc.theWorld.isRemote) {
                EntityPlayerMP e = (EntityPlayerMP) mc.theWorld.getClosestPlayerToEntity(mc.thePlayer, 0);
            }
        }
        if (getMode() == Mode.VERUS) {
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 68, mc.thePlayer.posZ);
        }
    }

    @Override
    public void onDisable() {
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionZ = 0;
        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        mc.timer.timerSpeed = 1f;
    }


    @Override
    public void onUpdate(UpdateEvent event) {
        ticks++;
        double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        if (receiveds08)
            s08ticks++;
        switch (getMode()) {
            case VULCAN:
                if (ticks < 20) {
                    PlayerUtils.cancelMotion();
                }
                if (ticks == 20) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.25, mc.thePlayer.posZ, false));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                    mc.thePlayer.jump();
                    PlayerUtils.speed(9.5f, false);
                }
                if (ticks == 21) {
                    PlayerUtils.speed(0.221f, false);
                }
                break;
            case VERUS:
                if (s08ticks < 2) {
                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionZ = 0;
                }
                if (s08ticks == 2) {
                    PacketUtils.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.2, mc.thePlayer.posZ, false));
                    PacketUtils.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                    PacketUtils.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                    PacketUtils.sendPacketNoEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                }
                if (mc.thePlayer.hurtTime > 5) {
                    System.out.println("Lame");
                    PlayerUtils.speed(0.9, false);
                    mc.thePlayer.motionY = 0.7;
                }
                break;
            case BATTLEASYABEST:

                if (s08ticks == 1) {


                    mc.thePlayer.motionX = -Math.sin(yaw) * 4;
                    mc.thePlayer.motionZ = Math.cos(yaw) * 4;
                    clip(8, 0);
                }
                if (s08ticks == 2) {
                    mc.thePlayer.motionX = -Math.sin(yaw) * 1.2;
                    mc.thePlayer.motionZ = Math.cos(yaw) * 1.2;
                    clip(8, 0);
                    mc.thePlayer.motionY = 1;
                }
                if (s08ticks > 5 && mc.thePlayer.onGround) {

                    toggle();
                }
                break;
            case BATTLEASYAMOTION:
                if (s08ticks == 2) {
                    mc.thePlayer.motionX = -Math.sin(yaw) * 1.1;
                    mc.thePlayer.motionZ = Math.cos(yaw) * 1.1;
                    mc.thePlayer.motionY = 0.6;
                }
                if (s08ticks > 5 && mc.thePlayer.onGround) {
                    toggle();
                }
                if (s08ticks > 10 && s08ticks < 13) {
                    mc.thePlayer.motionY = 0;
                }
                break;
            default:
                break;
        }
        if (mc.thePlayer.onGround && s08ticks > 5) {
            toggle();
        }
    }

    @Override
    public void onPacketReceived(PacketReceivedEvent event) {
        if ((event.getPacket() instanceof S08PacketPlayerPosLook)) {
            receiveds08 = true;
        }
        switch (getMode()) {
            case VULCAN:
                if (event.getPacket() instanceof S12PacketEntityVelocity)
                    event.setCancelled(true);
                break;
        }
    }

    @Override
    public void onPacketSent(PacketSentEvent event) {
        Packet p = event.getPacket();
        switch (getMode()) {
            case VULCAN:
                if (ticks < 20 && p instanceof C03PacketPlayer) {
                    event.setCancelled(true);
                }
                break;
        }
    }

    public void clip(double distance, double height) {
        double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        double x = -Math.sin(yaw) * distance;
        double z = Math.cos(yaw) * distance;
        mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY + height, mc.thePlayer.posZ + z);
        mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + height, mc.thePlayer.posZ, true));
    }


}