package libayon.module.modules.movement;

import libayon.event.events.client.UpdateEvent;
import libayon.event.events.packets.PacketReceivedEvent;
import libayon.event.events.player.JumpEvent;
import libayon.event.events.render.Render3DEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import libayon.utils.movement.MovementUtils;
import libayon.utils.player.PlayerUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import org.lwjgl.input.Keyboard;

public class Speed extends Module {
    public int times;
    public int jumpticks = 0;
    public int ticks;
    public EntityPlayerSP player;
    private boolean jumped = false;

    public Speed() {
        super("Speed", "Allows you to BHop", Keyboard.KEY_Z, Category.MOVEMENT, Mode.AAC, Mode.VULCAN, Mode.LUCKYNETWORK, Mode.BATTLEASYAKITPVP, Mode.VERUS, Mode.ACENTRAMC);
    }

    @Override
    public void setup() {
    }

    @Override
    public void onEnable() {
        switch (getMode()) {
            case BATTLEASYA:
                mc.timer.timerSpeed = 1f;
                break;
        }
        jumped = false;
        jumpticks = 0;
        ticks = 0;
        player = mc.thePlayer;
        times = 0;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1;
    }

    @Override
    public void onJump(JumpEvent e) {
        switch (getMode()) {
            case VERUS:
                break;
            case VULCAN:
                break;
            case AAC:
                PlayerUtils.speed(0.26, false);
                break;
        }
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (!isToggled()) return;
        Mode m = getMode();
        switch (m) {
            case VULCAN:
                mc.gameSettings.keyBindJump.setPressed(false);
                mc.thePlayer.setSprinting(true);
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                    jumped = true;
                    jumpticks = 0;
                    PlayerUtils.speed(0.47, true);
                }
                if (jumped) jumpticks++;
                if (jumpticks == 5) {
                    PlayerUtils.speed(MovementUtils.getSpeed(), true);
                    mc.thePlayer.motionY = -0.3;
                }
                break;
            case AAC:
                mc.gameSettings.keyBindJump.setPressed(false);
                mc.thePlayer.setSprinting(true);
                if (mc.thePlayer.onGround) {
                    jumpticks = 0;
                    jumped = true;
                    mc.thePlayer.jump();
                } else {
                    // speed(0.27);
                }
                if (jumped) jumpticks++;
                if (jumpticks == 1) {
                    PlayerUtils.speed(0.42, false);
                    mc.thePlayer.motionY = 0.15;
                    jumped = false;
                    jumpticks = 0;
                }
                break;
            case BATTLEASYAKITPVP:
                mc.gameSettings.keyBindJump.setPressed(false);
                mc.thePlayer.setSprinting(true);
                if (mc.thePlayer.onGround) {
                    jumpticks = 0;
                    jumped = true;
                    mc.thePlayer.jump();
                }
                if (jumped) jumpticks++;
                if (jumpticks == 6) {
                    mc.thePlayer.motionY = -0.15;

                }
                if (jumpticks > 7) {
                    mc.thePlayer.motionY = 0.8;
                }
                if (jumpticks == 8) {
                    jumped = false;
                    jumpticks = 0;
                }

                break;
            case LUCKYNETWORK:
                mc.gameSettings.keyBindJump.setPressed(false);
                mc.thePlayer.setSprinting(true);
                if (mc.thePlayer.onGround) {
                    jumpticks = 0;
                    jumped = true;
                    mc.thePlayer.jump();
                }
                if (jumped) jumpticks++;
                if (jumpticks > 5) {
                    mc.thePlayer.motionY = -0.2;

                    jumped = false;
                    jumpticks = 0;
                }
                break;
            case VERUS:
                mc.timer.timerSpeed = 1.1f;
                mc.thePlayer.setSprinting(true);
                mc.gameSettings.keyBindSprint.setPressed(true);
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                } else {
                    PlayerUtils.speed(0.27D, true);
                }
                System.out.println(jumpticks);
                break;
            case ACENTRAMC:
                if (mc.gameSettings.keyBindForward.isKeyDown()) {
                    mc.thePlayer.setSprinting(true);
                    mc.gameSettings.keyBindSprint.setPressed(true);
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                    } else {
                        PlayerUtils.speed(0.3D, true);
                    }
                }
                break;
        }

    }

    @Override
    public void onRender3D(Render3DEvent e) {
    }

    @Override
    public void onPacketReceived(PacketReceivedEvent event) {
        Packet p = event.getPacket();
    }

}