package libayon.module.modules.combat;

import libayon.event.events.client.UpdateEvent;
import libayon.event.events.packets.PacketReceivedEvent;
import libayon.event.events.player.PostMotionEvent;
import libayon.event.events.player.PreMotionEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import libayon.utils.Random;
import libayon.utils.timers.Timer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class AutoSoup extends Module {
    private final int health = 15;
    private final double minDelay = 300;
    private final double maxDelay = 500;
    private final boolean dropBowl = true;
    private final Timer timer = new Timer();

    private boolean switchBack;

    private long decidedTimer;

    private int soup = -37;

    public AutoSoup() {
        super("AutoSoup", "Magic Auto Healer omg pog!", 0, Category.COMBAT, Mode.NONE);
    }

    @Override
    public void onDisable() {
        this.switchBack = false;
        this.soup = -37;
    }

    public void setup() {
    }

    public void onEnable() {
    }

    @Override
    public void onPacketReceived(PacketReceivedEvent event) {
    }

    @Override
    public void onUpdate(UpdateEvent e) {
    }

    @Override
    public void onPreMotion(PreMotionEvent e) {
        if (this.switchBack) {
            System.out.println("Tried");
            mc.gameSettings.keyBindUseItem.setPressed(false);
            mc.thePlayer.inventory.currentItem = 0;
            this.switchBack = false;
        }

        if (mc.thePlayer.ticksExisted > 10 && mc.thePlayer.getHealth() < health) {
            this.soup = findSoup() - 36;
            if (this.soup != -37) {
                mc.thePlayer.inventory.currentItem = this.soup;
                //mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(this.soup));
                mc.gameSettings.keyBindUseItem.setPressed(true);
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                this.switchBack = true;
            }
            int delayFirst = (int) Math.floor(Math.min(minDelay, maxDelay));
            int delaySecond = (int) Math.ceil(Math.max(minDelay, maxDelay));
            this.decidedTimer = (long) Random.randomDouble(delayFirst, delaySecond);
            this.timer.reset();
        }
    }

    @Override
    public void onPostMotion(PostMotionEvent e) {

    }

    public int findSoup() {
        for (int i = 36; i < 45; i++) {
            ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getDisplayName().contains("Stew") && itemStack.stackSize > 0 && itemStack.getItem() instanceof net.minecraft.item.ItemFood)
                return i;
        }
        return -1;
    }

}