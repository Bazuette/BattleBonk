package libayon.module.modules.movement;

import libayon.event.events.client.UpdateEvent;
import libayon.event.events.packets.PacketReceivedEvent;
import libayon.event.events.packets.PacketSentEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import libayon.utils.player.PlayerUtils;
import libayon.utils.timers.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Keyboard;


public class Fly extends Module {
    public Timer timer = new Timer();


    private int s08tick = 0;
    private boolean s08 = false;
    private int s08times = 0;


    public Fly() {
        super("Fly", "Allows you to fly", Keyboard.KEY_G, Category.MOVEMENT, Mode.AAC, Mode.VULCAN, Mode.LUCKYNETWORK, Mode.BATTLEASYA, Mode.VANILLA, Mode.VERUS);
    }

    @Override
    public void setup() {
    }

    @Override
    public void onEnable() {
        s08times = 0;
        timer.reset();
        s08 = false;
        s08tick = 0;
        if (getMode() == Mode.AAC) {
            mc.timer.timerSpeed = 0.2f;
            mc.getNetHandler().getNetworkManager().sendPacketWithoutEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 11, mc.thePlayer.posZ, false));
        }
        if (getMode() == Mode.LUCKYNETWORK) {
            mc.thePlayer.ticksExisted = 14;
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1;
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionZ = 0;
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        switch (getMode()) {
            case VERUS:
                if (mc.thePlayer.ticksExisted % 3 == 0) {
                    mc.thePlayer.motionY = 0.02;
                } else {
                    mc.thePlayer.motionY = -0.1;
                }
                break;
            case VULCAN:
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                }
                if (mc.thePlayer.onGround) {
                    toggle();
                }
                if (mc.thePlayer.ticksExisted % 2 == 0) {
                    mc.thePlayer.motionY = -0.1;
                } else {
                    mc.thePlayer.motionY = -0.16;
                }
                PlayerUtils.speed(0.23, false);
                if (mc.thePlayer.fallDistance > 2.5) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                    mc.thePlayer.fallDistance = 0;
                }
                break;
            case VANILLA:
                mc.thePlayer.motionY = 0;
                if (mc.gameSettings.keyBindForward.isKeyDown()) {
                    PlayerUtils.speed(2, true);
                } else {
                    PlayerUtils.speed(0, true);
                }
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.thePlayer.motionY = 0.3;
                } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.thePlayer.motionY = -0.3;
                }
                break;
            case AAC:
                PlayerUtils.cancelMotion();
                if (s08) {
                    s08tick++;
                }
                if (mc.thePlayer.ticksExisted % 5 == 0) {
                    mc.getNetHandler().getNetworkManager().sendPacketWithoutEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 11, mc.thePlayer.posZ, false));
                }
                if (s08tick == 2) {
                    PlayerUtils.speed(8, false);
                    mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                    mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                    s08 = false;
                    s08tick = 0;
                }
                break;
            case LUCKYNETWORK:
                if (s08) {
                    s08tick++;
                }
                if (mc.thePlayer.ticksExisted % 10 == 0) {

                    mc.getNetHandler().getNetworkManager().sendPacketWithoutEvent(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 11, mc.thePlayer.posZ, true));
                }
                if (s08tick == 2) {
                    if (mc.gameSettings.keyBindJump.isKeyDown()) {
                        mc.thePlayer.motionY = 2;
                    }
                    if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                        mc.thePlayer.motionY = -2;
                    }
                    PlayerUtils.speed(4, false);

                }
                if (s08tick == 5) {
                    s08tick = 0;
                    s08times = 0;
                    s08 = false;
                }
                break;
            case BATTLEASYA:
                if (mc.thePlayer.hurtTime > 0 && !s08) {
                    PlayerUtils.speed(3, false);
                    mc.thePlayer.motionY = 1;
                    s08 = true;
                }

                break;
        }

    }


    @Override
    public void onPacketReceived(PacketReceivedEvent event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            s08 = true;
            s08times++;
        }
    }

    @Override
    public void onPacketSent(PacketSentEvent event) {
        Packet p = event.getPacket();
        if (mc.thePlayer == null) return;
        if (getMode() == Mode.BATTLEASYA && s08) {
            if (p instanceof C03PacketPlayer) {
            }
        }
        if ((p instanceof C03PacketPlayer || p instanceof C00PacketKeepAlive) && mc.thePlayer.ticksExisted % 3 == 0) {
            return;
        }
    }


}