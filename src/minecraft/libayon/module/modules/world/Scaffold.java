package libayon.module.modules.world;

import libayon.event.events.client.UpdateEvent;
import libayon.event.events.packets.PacketSentEvent;
import libayon.event.events.player.PreMotionEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import libayon.utils.Random;
import libayon.utils.movement.MovementUtils;
import libayon.utils.player.PlayerUtils;
import libayon.utils.player.RotationUtils;
import libayon.utils.timers.Timer;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP.Direction;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class Scaffold extends Module {
    public static int extend = 1;
    public static boolean sprint = false;
    public static int jumpticks = 0;
    public Timer delayTimer = new Timer();
    int lastslot = -1;
    private BlockCache blockCache, lastBlockCache;
    private int lastticks;
    private boolean blockplaced = false;
    private boolean jumped = false;

    public Scaffold() {
        super("Scaffold", "Autobridge lol", Keyboard.KEY_F, Category.WORLD, Mode.BATTLEASYA, Mode.VULCAN, Mode.NCP);
    }


    @Override
    public void setup() {
        moduleSettings.addDefault("Extend Range", 1.0);
        moduleSettings.addDefault("Max Delay", 0.1);
        moduleSettings.addDefault("Min Delay", 0.3);
        moduleSettings.addDefault("Jump", false);

        List<String> TowerMode = new ArrayList<>();
        TowerMode.add("Battleasya");
        TowerMode.add("Vulcan");
        moduleSettings.addDefault("TowerMode", TowerMode);
        moduleSettings.addDefault("Current TowerMode", TowerMode.get(0));
    }

    @Override
    public void onUpdate(UpdateEvent e) {
        if (lastticks > 0) {
            lastticks--;
        }
        if (lastticks == 1) {
            mc.thePlayer.setSneaking(false);
            mc.gameSettings.keyBindSneak.setPressed(false);
            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            tower();
        }
        if (moduleSettings.getBoolean("Jump")) {
            if (mc.gameSettings.keyBindForward.isKeyDown() && !mc.gameSettings.keyBindJump.isKeyDown()) {
                if (mc.thePlayer.onGround && !(
                        mc.thePlayer.getDirection() == Direction.SE ||
                                mc.thePlayer.getDirection() == Direction.SW ||
                                mc.thePlayer.getDirection() == Direction.NE ||
                                mc.thePlayer.getDirection() == Direction.NW ||
                                mc.thePlayer.getDirection() == Direction.NE ||
                                mc.thePlayer.getDirection() == Direction.SW ||
                                mc.thePlayer.getDirection() == Direction.NE ||
                                mc.thePlayer.getDirection() == Direction.SW)) {
                    mc.thePlayer.jump();
                    jumpticks = 0;
                    jumped = true;
                }
                if (jumped) {
                    jumpticks++;
                }
            }
        }

    }

    private void tower() {
        if (mc.thePlayer.onGround && mc.gameSettings.keyBindJump.isKeyDown()) {

            mc.thePlayer.motionY = 0;
            mc.thePlayer.jump();
            jumped = true;
        }
        if (jumped) {
            jumpticks++;
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        }

        mc.gameSettings.keyBindBack.setPressed(false);

        switch (moduleSettings.getString("Current TowerMode")) {
            case "Battleasya":
                if (jumpticks > 3) {
                    mc.thePlayer.motionY = 0.03;
                    jumpticks = 0;
                    jumped = false;
                }
                break;
        }
    }

    @Override
    public void onPreMotion(PreMotionEvent event) {

        blockCache = grab();
        if (blockCache != null) {
            lastBlockCache = grab();
        } else {
            return;
        }

        int slot = PlayerUtils.grabBlockSlot();
        if (slot == -1) return;

        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
        double delay = Random.randomDouble(moduleSettings.getDouble("Min Delay"), moduleSettings.getDouble("Max Delay"));
        System.out.println(delay * 100 + " " + delayTimer.getTimePassed());

        if (blockCache == null) return;
        if (!delayTimer.hasReached(delay * 100)) return;
        blockplaced = true;
        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getStackInSlot(slot), lastBlockCache.position, lastBlockCache.facing, PlayerUtils.getHypixelVec3(lastBlockCache));
        mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());


        delayTimer.reset();
        return;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindSneak.setPressed(false);
        mc.thePlayer.setSprinting(true);
        mc.timer.timerSpeed = 1;
        if (blockplaced && (lastslot != mc.thePlayer.inventory.currentItem)) {
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }
        mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
    }

    @Override
    public void onEnable() {
        delayTimer.reset();
        mc.timer.timerSpeed = 1;
        blockplaced = false;
        mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
        lastticks = 0;
        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(1));
    }

    @Override
    public void onPacketSent(PacketSentEvent event) {
        if (mc.thePlayer == null)
            return;
        Packet p = event.getPacket();
        float yaw = 0;
        float pitch = 90;
        switch (getMode()) {
            case BATTLEASYA:
                switch (mc.thePlayer.getDirection()) {
                    case N:
                        yaw = 0;
                        break;
                    case S:
                        yaw = -180;
                        break;
                    case E:
                        yaw = 90;
                        break;
                    case W:
                        yaw = -90;
                        break;
                    case NE:
                        yaw = 45;
                        break;
                    case NW:
                        yaw = -45;
                        break;
                    case SE:
                        yaw = 135;
                        break;
                    case SW:
                        yaw = -135;
                        break;
                }
                pitch = 71;
                break;
            case NCP:
                if (blockCache == null) return;
                yaw = RotationUtils.getFacingRotations(blockCache)[0];
                pitch = RotationUtils.getFacingRotations(blockCache)[1];
                break;
            case VULCAN:
                float r = Random.randomFloat(-5, 5);
                switch (mc.thePlayer.getDirection()) {
                    case N:
                        yaw = 0 + r;
                        break;
                    case S:
                        yaw = -180 + r;
                        break;
                    case E:
                        yaw = 90 + r;
                        break;
                    case W:
                        yaw = -90 + r;
                        break;
                    case NE:
                        yaw = 45 + r;
                        break;
                    case NW:
                        yaw = -45 + r;
                        break;
                    case SE:
                        yaw = 135 + r;
                        break;
                    case SW:
                        yaw = -135 + r;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + mc.thePlayer.getDirection());
                }
                pitch = MathHelper.clamp_float(71 + r, -90, 90);
                break;
        }

        float m = 0.005f * (mc.gameSettings.mouseSensitivity) / 0.005f;
        float f = m * 0.6f + 0.2f;
        float gcd = m * m * m * 1.2f;

        yaw -= yaw % gcd;
        pitch -= pitch % gcd;
        if (p instanceof C03PacketPlayer) {
            C03PacketPlayer c03 = (C03PacketPlayer) p;
            c03.setYaw(yaw);
            c03.setPitch(pitch);
            RotationUtils.setRotations(c03.getYaw(), c03.getPitch());
        }
        if (p instanceof C08PacketPlayerBlockPlacement && getMode() != Mode.VULCAN) {
            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            lastticks = 4;

        } else if (getMode() == Mode.VULCAN && p instanceof C08PacketPlayerBlockPlacement) {
            MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed() / 1.5);
            mc.gameSettings.keyBindSneak.setPressed(true);
            mc.thePlayer.setSneaking(true);
            lastticks = 3;
        }
        if (p instanceof C0BPacketEntityAction) {
            C0BPacketEntityAction c0b = (C0BPacketEntityAction) p;
            if (c0b.getAction() == C0BPacketEntityAction.Action.START_SPRINTING) {
                event.setCancelled(true);
            }
        }
        if (p instanceof C09PacketHeldItemChange) {
            C09PacketHeldItemChange c09 = (C09PacketHeldItemChange) p;
            if (lastslot == c09.getSlotId()) {
                event.setCancelled(true);
            } else {
                lastslot = c09.getSlotId();
            }
        }
    }

    public BlockCache grab() {
        final EnumFacing[] invert = {EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH,
                EnumFacing.EAST, EnumFacing.WEST};
        BlockPos position = new BlockPos(0, 0, 0);
        if (MovementUtils.isMoving() && !mc.gameSettings.keyBindJump.isKeyDown()) {
            for (double n2 = moduleSettings.getDouble("Extend Range") + 0.0001, n3 = 0.0; n3 <= n2; n3 += n2 / (Math.floor(n2))) {
                float y = 0;
                if (moduleSettings.getBoolean("Jump") && !(
                        mc.thePlayer.getDirection() == Direction.SE ||
                                mc.thePlayer.getDirection() == Direction.SW ||
                                mc.thePlayer.getDirection() == Direction.NE ||
                                mc.thePlayer.getDirection() == Direction.NW ||
                                mc.thePlayer.getDirection() == Direction.NE ||
                                mc.thePlayer.getDirection() == Direction.SW ||
                                mc.thePlayer.getDirection() == Direction.NE ||
                                mc.thePlayer.getDirection() == Direction.SW)) {
                    if (jumpticks > 0 && !mc.thePlayer.onGround && jumpticks < 11) {
                        y = 1.4f;
                    } else {
                        return null;
                    }
                } else {
                    y = 1;
                }
                final BlockPos blockPos2 = new BlockPos(
                        mc.thePlayer.posX - MathHelper.sin(RotationUtils.clampRotation()) * n3,
                        //((jumpticks > 1 && !mc.thePlayer.onGround && jumpticks < 10) ? 1.4: 0)
                        mc.thePlayer.posY - y,
                        mc.thePlayer.posZ + MathHelper.cos(RotationUtils.clampRotation()) * n3);
                final IBlockState blockState = mc.theWorld.getBlockState(blockPos2);
                if (blockState != null && blockState.getBlock() == Blocks.air) {
                    position = blockPos2;
                    break;
                }
            }
        } else {
            position = new BlockPos(new BlockPos(mc.thePlayer.getPositionVector().xCoord,
                    mc.thePlayer.getPositionVector().yCoord, mc.thePlayer.getPositionVector().zCoord))
                    .offset(EnumFacing.DOWN);
        }

        if (!(mc.theWorld.getBlockState(position).getBlock() instanceof BlockAir)
                && !(mc.theWorld.getBlockState(position).getBlock() instanceof BlockLiquid)) {
            return null;
        }
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing facing = values[i];
            final BlockPos offset = position.offset(facing);
            if (!(mc.theWorld.getBlockState(offset).getBlock() instanceof BlockAir)
                    && !(mc.theWorld.getBlockState(position).getBlock() instanceof BlockLiquid)) {
                return new BlockCache(offset, invert[facing.ordinal()]);
            }
        }
        final BlockPos[] offsets = {new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1),
                new BlockPos(0, 0, 1)};
        BlockPos[] array;
        for (int length2 = (array = offsets).length, j = 0; j < length2; ++j) {
            final BlockPos offset2 = array[j];
            final BlockPos offsetPos = position.add(offset2.getX(), 0, offset2.getZ());
            if (mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir) {
                EnumFacing[] values2;
                for (int length3 = (values2 = EnumFacing.values()).length, k = 0; k < length3; ++k) {
                    final EnumFacing facing2 = values2[k];
                    final BlockPos offset3 = offsetPos.offset(facing2);
                    if (!(mc.theWorld.getBlockState(offset3).getBlock() instanceof BlockAir)) {
                        return new BlockCache(offset3, invert[facing2.ordinal()]);
                    }
                }
            }

        }
        return null;
    }

    public static class BlockCache {
        public BlockPos position;
        public EnumFacing facing;

        public BlockCache(final BlockPos position, final EnumFacing facing) {
            this.position = position;
            this.facing = facing;
        }

        public BlockPos getPosition() {
            return this.position;
        }

        private EnumFacing getFacing() {
            return this.facing;
        }

    }


}