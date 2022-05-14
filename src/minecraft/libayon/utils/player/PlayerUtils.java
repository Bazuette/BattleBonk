package libayon.utils.player;

import libayon.module.modules.world.Scaffold;
import libayon.utils.Strings;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.*;

public class PlayerUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void sendMessage(String message) {
        sendMessage(message, true);
    }

    public static void sendMessage(String message, boolean prefix) {
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(Strings.translateColors(("") + message)), false);
    }

    public static Boolean isSameTeam(Entity e) {
        char targetcolor = e.getDisplayName().getFormattedText().substring(1).charAt(0);
        char playercolor = mc.thePlayer.getDisplayName().getFormattedText().substring(1).charAt(0);
        return (targetcolor == playercolor);
    }

    public static int grabBlockSlot() {
        int slot = -1;
        int highestStack = -1;
        boolean didGetHotbar = false;
        for (int i = 0; i < 9; ++i) {
            final ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i];
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock && canItemBePlaced(itemStack) && itemStack.stackSize > 0) {
                if (Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i].stackSize > highestStack && i < 9) {
                    highestStack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i].stackSize;
                    slot = i;
                    if (slot == getLastHotbarSlot()) {
                        didGetHotbar = true;
                    }
                }
                if (i > 8 && !didGetHotbar) {
                    int hotbarNum = getFreeHotbarSlot();
                    if (hotbarNum != -1 && Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i].stackSize > highestStack) {
                        highestStack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i].stackSize;
                        slot = i;
                    }
                }
            }
        }
        if (slot > 8) {
            int hotbarNum = getFreeHotbarSlot();
            if (hotbarNum != -1) {
                Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, Minecraft.getMinecraft().thePlayer);
            } else {
                return -1;
            }
        }
        return slot;
    }

    public static int getLastHotbarSlot() {
        int hotbarNum = -1;
        for (int k = 0; k < 9; k++) {
            final ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[k];
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock && canItemBePlaced(itemStack) && itemStack.stackSize > 1) {
                hotbarNum = k;
                continue;
            }
        }
        return hotbarNum;
    }

    public static int getFreeHotbarSlot() {
        int hotbarNum = -1;
        for (int k = 0; k < 9; k++) {
            if (Minecraft.getMinecraft().thePlayer.inventory.mainInventory[k] == null) {
                hotbarNum = k;
                continue;
            } else {
                hotbarNum = 7;
            }
        }
        return hotbarNum;
    }

    public static boolean canIItemBePlaced(Item item) {
        if (Item.getIdFromItem(item) == 116)
            return false;
        if (Item.getIdFromItem(item) == 30)
            return false;
        if (Item.getIdFromItem(item) == 31)
            return false;
        if (Item.getIdFromItem(item) == 175)
            return false;
        if (Item.getIdFromItem(item) == 28)
            return false;
        if (Item.getIdFromItem(item) == 27)
            return false;
        if (Item.getIdFromItem(item) == 66)
            return false;
        if (Item.getIdFromItem(item) == 157)
            return false;
        if (Item.getIdFromItem(item) == 31)
            return false;
        if (Item.getIdFromItem(item) == 6)
            return false;
        if (Item.getIdFromItem(item) == 31)
            return false;
        if (Item.getIdFromItem(item) == 32)
            return false;
        if (Item.getIdFromItem(item) == 140)
            return false;
        if (Item.getIdFromItem(item) == 390)
            return false;
        if (Item.getIdFromItem(item) == 37)
            return false;
        if (Item.getIdFromItem(item) == 38)
            return false;
        if (Item.getIdFromItem(item) == 39)
            return false;
        if (Item.getIdFromItem(item) == 40)
            return false;
        if (Item.getIdFromItem(item) == 69)
            return false;
        if (Item.getIdFromItem(item) == 50)
            return false;
        if (Item.getIdFromItem(item) == 75)
            return false;
        if (Item.getIdFromItem(item) == 76)
            return false;
        if (Item.getIdFromItem(item) == 54)
            return false;
        if (Item.getIdFromItem(item) == 130)
            return false;
        if (Item.getIdFromItem(item) == 146)
            return false;
        if (Item.getIdFromItem(item) == 342)
            return false;
        if (Item.getIdFromItem(item) == 12)
            return false;
        if (Item.getIdFromItem(item) == 77)
            return false;
        if (Item.getIdFromItem(item) == 143)
            return false;
        return Item.getIdFromItem(item) != 46;
    }

    public static Vec3 getHypixelVec3(Scaffold.BlockCache data) {
        BlockPos pos = data.position;
        EnumFacing face = data.facing;
        double x = pos.getX(), y = pos.getY(), z = pos.getZ();
        if (face != EnumFacing.UP && face != EnumFacing.DOWN) {
            y += 0.5;
        } else {
            x += 0.3;
            z += 0.3;
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += 0.15;
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += 0.15;
        }
        return new Vec3(x, y, z);
    }

    public static boolean canItemBePlaced(ItemStack item) {
        if (Item.getIdFromItem(item.getItem()) == 116)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 30)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 31)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 175)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 28)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 27)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 66)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 157)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 31)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 6)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 31)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 32)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 140)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 390)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 37)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 38)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 39)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 40)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 69)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 50)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 75)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 76)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 54)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 130)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 146)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 342)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 12)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 77)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 143)
            return false;
        if (Item.getIdFromItem(item.getItem()) == 46)
            return false;
        return Item.getIdFromItem(item.getItem()) != 145;
    }

    public static void cancelMotion() {
        mc.thePlayer.motionY = 0;
        mc.thePlayer.motionZ = 0;
        mc.thePlayer.motionX = 0;
        mc.gameSettings.keyBindForward.setPressed(false);
        mc.gameSettings.keyBindBack.setPressed(false);
        mc.gameSettings.keyBindLeft.setPressed(false);
        mc.gameSettings.keyBindRight.setPressed(false);
    }

    public static Integer findItem(Item item) {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null || itemStack.getItem() == null) {
                if (item == null)
                    return i;
            } else if (itemStack.getItem() == item) {
                return i;
            }
        }
        return null;
    }

    public static BlockPos getBlockAtCursor(Double range) {
        return mc.thePlayer.rayTrace(range, 1.0F).getBlockPos();
    }

    public static boolean isOnLiquid() {
        boolean onLiquid = false;
        AxisAlignedBB playerBB = mc.thePlayer.getEntityBoundingBox();
        WorldClient world = mc.theWorld;
        int y = (int) (playerBB.offset(0.0D, -0.01D, 0.0D)).minY;
        for (int x = MathHelper.floor_double(playerBB.minX); x < MathHelper.floor_double(playerBB.maxX) + 1; x++) {
            for (int z = MathHelper.floor_double(playerBB.minZ); z < MathHelper.floor_double(playerBB.maxZ) + 1; z++) {
                Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && !(block instanceof net.minecraft.block.BlockAir)) {
                    if (!(block instanceof net.minecraft.block.BlockLiquid))
                        return false;
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }

    public static Block getBlockRelativeToPlayer(double offsetX, double offsetY, double offsetZ) {
        return mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX + offsetX, mc.thePlayer.posY + offsetY, mc.thePlayer.posZ + offsetZ)).getBlock();
    }

    public static Block getBlock(double offsetX, double offsetY, double offsetZ) {
        return mc.theWorld.getBlockState(new BlockPos(offsetX, offsetY, offsetZ)).getBlock();
    }

    public static void clip(double distance, double height) {
        double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        double x = -Math.sin(yaw) * distance;
        double z = Math.cos(yaw) * distance;
        mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY + height, mc.thePlayer.posZ + z);
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + height, mc.thePlayer.posZ, false));
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + height, mc.thePlayer.posZ, true));
    }

    public static void clipOnGround(double distance, double height) {
        double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        double x = -Math.sin(yaw) * distance;
        double z = Math.cos(yaw) * distance;
        mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY + height, mc.thePlayer.posZ + z);
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + height, mc.thePlayer.posZ, true));
    }

    public static void speed(double speed, boolean strafe) {
        double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        if (strafe) {
            if (mc.gameSettings.keyBindLeft.isKeyDown()) {
                yaw = yaw + -45;
            }
            if (mc.gameSettings.keyBindRight.isKeyDown()) {
                yaw = yaw + 45;
            }
        }
        mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }


}