package libayon.module.modules.movement;

import libayon.event.events.player.PreMotionEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import libayon.utils.movement.MovementUtils;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class Step extends Module {
    private int stepTicks;
    private int ticksOnGround;
    private float stepheight;
    private float stepticks;

    public Step() {
        super("Step", "Steps you faster", 0, Category.PLAYER, Mode.VULCAN, Mode.NCP, Mode.VERUS, Mode.BATTLEASYA, Mode.VANILLA);
    }

    @Override
    public void setup() {
        moduleSettings.addDefault("Step Height", 1.0);
    }

    @Override
    public void onEnable() {
        stepticks = 0;
    }

    @Override
    public void onDisable() {
        mc.thePlayer.stepHeight = 0.6F;
    }

    @Override
    public void onPreMotion(PreMotionEvent event) {
        this.stepheight = moduleSettings.getFloat("Step Height");

        if (mc.thePlayer.onGround) {
            ticksOnGround++;
        } else {
            ticksOnGround = 0;
        }

        if (getMode() == Mode.VANILLA) mc.thePlayer.stepHeight = this.stepheight;
        Block above = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ)).getBlock();
        if (
                mc.thePlayer.onGround && mc.thePlayer.isCollidedHorizontally && MovementUtils.isMoving() && (above instanceof net.minecraft.block.BlockAir || above instanceof net.minecraft.block.BlockBush || above instanceof net.minecraft.block.BlockSnow)) {
            this.stepTicks = 0;
            switch (getMode()) {
                case NCP:
                    mc.thePlayer.motionY = 0.37;
                    break;
            }
        }
    }
}
