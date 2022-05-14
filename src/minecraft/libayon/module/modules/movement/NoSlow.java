package libayon.module.modules.movement;

import libayon.event.events.client.UpdateEvent;
import libayon.event.events.packets.PacketReceivedEvent;
import libayon.event.events.packets.PacketSentEvent;
import libayon.event.events.player.PostMotionEvent;
import libayon.event.events.player.PreMotionEvent;
import libayon.event.events.player.SlowDownEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import libayon.utils.network.PacketUtils;
import libayon.utils.timers.Timer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.LinkedList;

public class NoSlow extends Module {
    private final boolean blockingStatus = false;
    private boolean lastBlockingStat = false;
    private boolean canAttack = false;
    private boolean waitC03 = false;
    private LinkedList packetBuf = new LinkedList<Packet<INetHandlerPlayServer>>();
    private Timer msTimer = new Timer();
    private boolean nextTemp = false;

    public NoSlow() {
        super("NoSlow", "Prevents you from slowing down", 0, Category.PLAYER, Mode.VANILLA, Mode.VULCAN);
    }

    @Override
    public void setup() {

    }

    public void onSlowdown(SlowDownEvent event) {
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        msTimer.reset();
        packetBuf.clear();
        nextTemp = false;
        waitC03 = false;
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        switch (getMode()) {
            case VULCAN:
                if (isBlocking()) return;
                if (msTimer.hasReached(230) && nextTemp) {
                    nextTemp = false;

                    PacketUtils.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
                    if (!packetBuf.isEmpty()) {
                        canAttack = false;
                        for (Object p : packetBuf) {
                            System.out.println(p);
                            if (p instanceof C03PacketPlayer) {
                                canAttack = true;
                            }
                            if (!((p instanceof C02PacketUseEntity || p instanceof C0APacketAnimation) && !canAttack)) {
                                PacketUtils.sendPacketNoEvent((Packet) p);
                            }
                        }
                        packetBuf.clear();
                    }
                }
                if (!nextTemp) {
                    lastBlockingStat = isBlocking();
                    if (!isBlocking())
                        return;
                    PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f));
                    nextTemp = true;
                    waitC03 = true;
                    msTimer.reset();
                }
                break;
        }
    }

    @Override
    public void onPreMotion(PreMotionEvent event) {
    }

    @Override
    public void onPostMotion(PostMotionEvent event) {

    }

    @Override
    public void onSlowDown(SlowDownEvent event) {
        if (mc.gameSettings.keyBindSprint.isKeyDown()) {
            mc.thePlayer.setSprinting(true);
        }
        event.setCancelled(true);
    }

    @Override
    public void onPacketSent(PacketSentEvent e) {
        Packet p = e.getPacket();
        switch (getMode()) {

            case VULCAN:
                if (mc.thePlayer == null) return;
                if (isBlocking()) return;
                if (nextTemp) {
                    if ((p instanceof C07PacketPlayerDigging || p instanceof C08PacketPlayerBlockPlacement) && isBlocking()) {
                        e.setCancelled(true);
                    } else if (p instanceof C03PacketPlayer || p instanceof C0APacketAnimation || p instanceof C0BPacketEntityAction || p instanceof C02PacketUseEntity || p instanceof C07PacketPlayerDigging || p instanceof C08PacketPlayerBlockPlacement) {

                        if (waitC03 && p instanceof C03PacketPlayer) {
                            waitC03 = false;
                            e.setCancelled(true);
                        }
                        packetBuf.add(p);
                        e.setCancelled(true);
                    }
                }
                break;
        }
    }

    @Override
    public void onPacketReceived(PacketReceivedEvent e) {

    }

    private boolean isBlocking() {
        boolean block = false;
        if (mc.thePlayer.isUsingItem() && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            block = true;
        }
        return block;

    }


}