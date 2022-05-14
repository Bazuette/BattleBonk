package libayon.module.modules.player;

import libayon.event.events.packets.PacketSentEvent;
import libayon.event.events.player.PreMotionEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import libayon.utils.BlockUtils;
import libayon.utils.timers.Timer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.Arrays;

public class InvManager extends Module {
    private final boolean inventoryPackets = true;
    private final boolean onlyWhileNotMoving = false;
    private final boolean inventoryOnly = true;
    private final boolean dropArchery = true;
    private final boolean invOnly = true;
    private final int slotWeapon = 1;
    private final int slotPick = 3;
    private final int slotAxe = 4;
    private final int slotShovel = 5;
    private final int slotBow = 6;
    private final int slotBlock = 9;
    private final int slotGapple = 2;
    private final boolean open = false;
    private final String[] shitItems = {"bread", "gold", "Steak", "melon", "iron", "tnt", "stick", "egg", "string", "cake", "mushroom", "flint", "compass", "dyePowder", "feather", "bucket", "chest", "snow", "fish", "enchant", "exp", "shears", "anvil", "torch", "seeds", "leather", "reeds", "skull", "record", "snowball", "piston"};
    private final String[] serverItems = {"inventory", "selector", "tracking compass", "(right click)", "tienda ", "perfil", "salir", "shop", "collectibles", "game", "profil", "lobby", "show all", "hub", "friends only", "cofre", "(click", "teleport", "play", "exit", "hide all", "jeux", "gadget", " (activ", "emote", "amis", "bountique", "choisir", "choose "};
    private final Timer startTimer = new Timer();
    private final Timer timer = new Timer();
    private double delay = 30;
    private boolean isInvOpen;

    public InvManager() {
        super("InvManager", "Organizes your inventory", 0, Category.PLAYER, Mode.NONE);
    }

    public static float getDamageScore(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return 0;

        float damage = 0;
        Item item = stack.getItem();

        if (item instanceof ItemSword) {
            damage += ((ItemSword) item).getDamageVsEntity();
        } else if (item instanceof ItemTool) {
            damage += item.getMaxDamage();
        }

        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F +
                EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.1F;

        return damage;
    }

    public static float getProtScore(ItemStack stack) {
        float prot = 0;
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor) stack.getItem();
            prot += armor.damageReduceAmount
                    + ((100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075F;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100F;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100F;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100F;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 25.F;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, stack) / 100F;
        }
        return prot;
    }

    public void click(int slot, int mouseButton, boolean shiftClick) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, mouseButton, shiftClick ? 1 : 0, mc.thePlayer);
    }

    public void drop(int slot) {
        mc.playerController.windowClick(0, slot, 1, 4, mc.thePlayer);
    }

    @Override
    public void setup() {
        moduleSettings.addDefault("Armor delay", 80);
        moduleSettings.addDefault("Delay", 30);
    }

    public void onPreMotion(PreMotionEvent e) {

        delay = moduleSettings.getDouble("Delay");

        if ((this.invOnly && !(mc.currentScreen instanceof GuiInventory)) || (this.onlyWhileNotMoving)) {
            startTimer.reset();
            timer.reset();
            return;
        }
        if (mc.thePlayer.openContainer instanceof ContainerChest) {
            // so it doesn't put on armor immediately after closing a chest
            timer.reset();
        }
        if (startTimer.getTimePassed() > moduleSettings.getDouble("armor delay")) {
            for (int armorSlot = 5; armorSlot < 9; armorSlot++) {
                if (equipBest(armorSlot)) {
                    startTimer.reset();
                    break;
                }
            }
        }
        if (true) {
            if (stop()) return;
            if (!mc.thePlayer.isUsingItem() && (mc.currentScreen == null || mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiIngameMenu)) {
                double delay = this.delay;
                if (timer.getTimePassed() > delay) {
                    Slot slot = mc.thePlayer.inventoryContainer.getSlot(getDesiredSlot(ItemType.WEAPON));
                    if (!slot.getHasStack() || !isBestWeapon(slot.getStack())) {
                        getBestWeapon();
                    }
                }
                if (timer.getTimePassed() > delay) getBestPickaxe();
                if (timer.getTimePassed() > delay) getBestAxe();
                if (timer.getTimePassed() > delay) getBestShovel();


                if (timer.getTimePassed() > delay) {

                    for (int i = 9; i < 45; i++) {
                        if (stop()) return;
                        Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
                        ItemStack is = slot.getStack();

                        if (is != null && shouldDrop(is, i)) {

                            drop(i);
                            timer.reset();
                            break;
                        }
                    }
                }

                if (timer.getTimePassed() > delay) swapBlocks();
                if (timer.getTimePassed() > delay) getBestBow();
                if (timer.getTimePassed() > delay) getGoldenApple();
                if (timer.getTimePassed() > delay) getEPearl();
            }
        }
        return;
    }

    public void onPacketSend(PacketSentEvent event) {
        if (isInvOpen) {
            Packet<?> packet = event.getPacket();
            if ((packet instanceof C16PacketClientStatus && ((C16PacketClientStatus) packet).getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT)
                    || packet instanceof C0DPacketCloseWindow) {
                event.setCancelled(true);
            } else if (packet instanceof C02PacketUseEntity) {
                fakeClose();
            }
        }
    }

    private boolean equipBest(int armorSlot) {
        int equipSlot = -1, currProt = -1;
        ItemArmor currItem = null;
        ItemStack slotStack = mc.thePlayer.inventoryContainer.getSlot(armorSlot).getStack();
        if (slotStack != null && slotStack.getItem() instanceof ItemArmor) {
            currItem = (ItemArmor) slotStack.getItem();
            currProt = currItem.damageReduceAmount
                    + EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, mc.thePlayer.inventoryContainer.getSlot(armorSlot).getStack());
        }
        // find best piece
        for (int i = 9; i < 45; i++) {
            ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (is != null && is.getItem() instanceof ItemArmor) {
                int prot = ((ItemArmor) is.getItem()).damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, is);
                if ((currItem == null || currProt < prot) && isValidPiece(armorSlot, (ItemArmor) is.getItem())) {
                    currItem = (ItemArmor) is.getItem();
                    equipSlot = i;
                    currProt = prot;
                }
            }
        }
        // equip best piece (if there is a better one)
        if (equipSlot != -1) {
            if (slotStack != null) {
                drop(armorSlot);
            } else {
                click(equipSlot, 0, true);
            }
            return true;
        }
        return false;
    }

    private boolean isValidPiece(int armorSlot, ItemArmor item) {
        String unlocalizedName = item.getUnlocalizedName();
        return armorSlot == 5 && unlocalizedName.startsWith("item.helmet")
                || armorSlot == 6 && unlocalizedName.startsWith("item.chestplate")
                || armorSlot == 7 && unlocalizedName.startsWith("item.leggings")
                || armorSlot == 8 && unlocalizedName.startsWith("item.boots");
    }

    private int getDesiredSlot(ItemType tool) {
        int slot = 36;
        switch (tool) {
            case WEAPON:
                slot = (this.slotWeapon);
                break;
            case PICKAXE:
                slot = (this.slotPick);
                break;
            case AXE:
                slot = (this.slotAxe);
                break;
            case SHOVEL:
                slot = (this.slotShovel);
                break;
            case BLOCK:
                slot = (this.slotBlock);
                break;
            case BOW:
                slot = (this.slotBow);
                break;
            case GOLDEN_APPLE:
                slot = (this.slotGapple);
                break;
            case EPEARL:
                slot = 8;
                break;
        }
        return slot + 35;
    }

    private boolean isBestWeapon(ItemStack is) {
        float damage = getDamageScore(is);
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                ItemStack is2 = slot.getStack();
                if (getDamageScore(is2) > damage && is2.getItem() instanceof ItemSword) {
                    return false;
                }
            }
        }
        return is.getItem() instanceof ItemSword;
    }

    private void getBestWeapon() {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (isBestWeapon(is) && getDamageScore(is) > 0 && is.getItem() instanceof ItemSword) {
                    swap(i, getDesiredSlot(ItemType.WEAPON) - 36);
                    break;
                }
            }
        }
    }

    private void getGoldenApple() {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if ((is.getItem() instanceof ItemAppleGold && is.getItem().getRarity(is).toString() == "RARE")) {
                    swap(i, getDesiredSlot(ItemType.GOLDEN_APPLE) - 36);
                    timer.reset();
                    break;
                }
            }
        }
    }

    private void getEPearl() {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (is.getItem() instanceof ItemEnderPearl) {
                    swap(i, getDesiredSlot(ItemType.EPEARL) - 36);
                    timer.reset();
                    break;
                }
            }
        }
    }

    private boolean shouldDrop(ItemStack stack, int slot) {
        String stackName = stack.getDisplayName().toLowerCase();
        Item item = stack.getItem();
        String ulName = item.getUnlocalizedName();
        if (Arrays.stream(serverItems).anyMatch(stackName::contains)) return false;
        if (ulName.contains("tnt") || ulName.contains("ingot")) {
            return true;
        }
        if (item instanceof ItemBlock) {
            return !BlockUtils.isValidBlock(((ItemBlock) item).getBlock(), true);
        }
        int weaponSlot = getDesiredSlot(ItemType.WEAPON);
        int pickaxeSlot = getDesiredSlot(ItemType.PICKAXE);
        int axeSlot = getDesiredSlot(ItemType.AXE);
        int shovelSlot = getDesiredSlot(ItemType.SHOVEL);
        if ((slot != weaponSlot || !isBestWeapon(mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack()))
                && (slot != pickaxeSlot || !isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack()))
                && (slot != axeSlot || !isBestAxe(mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack()))
                && !(mc.thePlayer.inventoryContainer.getSlot(slot).getStack().getItem() instanceof ItemEnderPearl)
                && !(mc.thePlayer.inventoryContainer.getSlot(slot).getStack().getItem() instanceof ItemAppleGold)
                && (slot != shovelSlot || !isBestShovel(mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack()))) {
            if (item instanceof ItemArmor) {
                for (int type = 1; type < 5; type++) {
                    if (mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
                        ItemStack is = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
                        if (isBestArmor(is, type)) {
                            continue;
                        }
                    }
                    if (isBestArmor(stack, type)) {
                        return false;
                    }
                }
            }

            if ((item == Items.wheat) || item == Items.spawn_egg
                    || (item instanceof ItemPotion && isShitPotion(stack))) {
                return true;
            } else if (!(item instanceof ItemSword) && !(item instanceof ItemAppleGold) && !(item instanceof ItemTool) && !(item instanceof ItemHoe) && !(item instanceof ItemArmor)) {
                if (this.dropArchery && (item instanceof ItemBow || item == Items.arrow)) {
                    return true;
                } else {
                    return item instanceof ItemGlassBottle || Arrays.stream(shitItems).anyMatch(ulName::contains);
                }
            }
            return true;
        }

        return false;
    }

    private void getBestPickaxe() {
        for (int i = 9; i < 45; ++i) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                ItemStack is = slot.getStack();
                if (isBestPickaxe(is) && !isBestWeapon(is)) {
                    int desiredSlot = getDesiredSlot(ItemType.PICKAXE);
                    if (i == desiredSlot) return;
                    Slot slot2 = mc.thePlayer.inventoryContainer.getSlot(desiredSlot);
                    if (!slot2.getHasStack() || !isBestPickaxe(slot2.getStack())) {
                        swap(i, desiredSlot - 36);
                    }
                }
            }
        }
    }

    private void getBestAxe() {
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                ItemStack is = slot.getStack();
                if (isBestAxe(is) && !isBestWeapon(is)) {
                    int desiredSlot = getDesiredSlot(ItemType.AXE);
                    if (i == desiredSlot) return;
                    Slot slot2 = mc.thePlayer.inventoryContainer.getSlot(desiredSlot);
                    if (!slot2.getHasStack() || !isBestAxe(slot2.getStack())) {
                        swap(i, desiredSlot - 36);
                        timer.reset();
                    }
                }
            }
        }
    }

    private void getBestShovel() {
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                ItemStack is = slot.getStack();
                if (isBestShovel(is) && !isBestWeapon(is)) {
                    int desiredSlot = getDesiredSlot(ItemType.SHOVEL);
                    if (i == desiredSlot) return;
                    Slot slot2 = mc.thePlayer.inventoryContainer.getSlot(desiredSlot);
                    if (!slot2.getHasStack() || !isBestShovel(slot2.getStack())) {
                        swap(i, desiredSlot - 36);
                        timer.reset();
                    }
                }
            }
        }
    }

    private void getBestBow() {
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            if (slot.getHasStack()) {
                ItemStack is = slot.getStack();
                String stackName = is.getDisplayName().toLowerCase();
                if (Arrays.stream(serverItems).anyMatch(stackName::contains) || !(is.getItem() instanceof ItemBow))
                    continue;
                if (isBestBow(is) && !isBestWeapon(is)) {
                    int desiredSlot = getDesiredSlot(ItemType.BOW);
                    if (i == desiredSlot) return;
                    Slot slot2 = mc.thePlayer.inventoryContainer.getSlot(desiredSlot);
                    if (!slot2.getHasStack() || !isBestBow(slot2.getStack())) {
                        swap(i, desiredSlot - 36);
                    }
                }
            }
        }
    }


    private int getMostBlocks() {
        int stack = 0;
        int biggestSlot = -1;
        for (int i = 9; i < 45; i++) {
            Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
            ItemStack is = slot.getStack();
            if (is != null && is.getItem() instanceof ItemBlock && is.stackSize > stack && Arrays.stream(serverItems).noneMatch(is.getDisplayName().toLowerCase()::contains)) {
                stack = is.stackSize;
                biggestSlot = i;
            }
        }
        return biggestSlot;
    }

    private void swapBlocks() {
        int mostBlocksSlot = getMostBlocks();
        int desiredSlot = getDesiredSlot(ItemType.BLOCK);
        if (mostBlocksSlot != -1 && mostBlocksSlot != desiredSlot) {
            // only switch if the hotbar slot doesn't already have blocks of the same quantity to prevent an inf loop
            Slot dss = mc.thePlayer.inventoryContainer.getSlot(desiredSlot);
            ItemStack dsis = dss.getStack();
            if (!(dsis != null && dsis.getItem() instanceof ItemBlock && dsis.stackSize >= mc.thePlayer.inventoryContainer.getSlot(mostBlocksSlot).getStack().stackSize && Arrays.stream(serverItems).noneMatch(dsis.getDisplayName().toLowerCase()::contains))) {
                swap(mostBlocksSlot, desiredSlot - 36);
            }
        }
    }

    private boolean isBestPickaxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemPickaxe)) {
            return false;
        } else {
            float value = getToolScore(stack);
            for (int i = 9; i < 45; i++) {
                Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
                if (slot.getHasStack()) {
                    ItemStack is = slot.getStack();
                    if (is.getItem() instanceof ItemPickaxe && getToolScore(is) > value) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private boolean isBestShovel(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSpade)) {
            return false;
        } else {
            float score = getToolScore(stack);
            for (int i = 9; i < 45; i++) {
                Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
                if (slot.getHasStack()) {
                    ItemStack is = slot.getStack();
                    if (is.getItem() instanceof ItemSpade && getToolScore(is) > score) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private boolean isBestAxe(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemAxe)) {
            return false;
        } else {
            float value = getToolScore(stack);
            for (int i = 9; i < 45; i++) {
                Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
                if (slot.getHasStack()) {
                    ItemStack is = slot.getStack();
                    if (getToolScore(is) > value && is.getItem() instanceof ItemAxe && !isBestWeapon(stack)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private boolean isBestBow(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemBow)) {
            return false;
        } else {
            float value = getPowerLevel(stack);
            for (int i = 9; i < 45; i++) {
                Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
                if (slot.getHasStack()) {
                    ItemStack is = slot.getStack();
                    if (getPowerLevel(is) > value && is.getItem() instanceof ItemBow && !isBestWeapon(stack)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private float getPowerLevel(ItemStack stack) {
        float score = 0;
        Item item = stack.getItem();
        if (item instanceof ItemBow) {
            score += EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
            score += EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) * 0.5F;
            score += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) * 0.1F;
        }
        return score;
    }

    private float getToolScore(ItemStack stack) {
        float score = 0;
        Item item = stack.getItem();
        if (item instanceof ItemTool) {
            ItemTool tool = (ItemTool) item;
            String name = item.getUnlocalizedName().toLowerCase();
            if (item instanceof ItemPickaxe) {
                score = tool.getStrVsBlock(stack, Blocks.stone) - (name.contains("gold") ? 5 : 0);
            } else if (item instanceof ItemSpade) {
                score = tool.getStrVsBlock(stack, Blocks.dirt) - (name.contains("gold") ? 5 : 0);
            } else {
                if (!(item instanceof ItemAxe)) return 1;
                score = tool.getStrVsBlock(stack, Blocks.log) - (name.contains("gold") ? 5 : 0);
            }
            score += EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075F;
            score += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100F;
        }
        return score;
    }

    private boolean isShitPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            ItemPotion pot = (ItemPotion) stack.getItem();
            if (pot.getEffects(stack) == null) return true;
            for (PotionEffect effect : pot.getEffects(stack)) {
                if (effect.getPotionID() == Potion.moveSlowdown.getId()
                        || effect.getPotionID() == Potion.weakness.getId()
                        || effect.getPotionID() == Potion.poison.getId()
                        || effect.getPotionID() == Potion.harm.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isBestArmor(ItemStack stack, int type) {
        String typeStr = "";
        switch (type) {
            case 1:
                typeStr = "helmet";
                break;
            case 2:
                typeStr = "chestplate";
                break;
            case 3:
                typeStr = "leggings";
                break;
            case 4:
                typeStr = "boots";
                break;
        }
        if (stack.getUnlocalizedName().contains(typeStr)) {
            float prot = getProtScore(stack);
            for (int i = 5; i < 45; i++) {
                Slot slot = mc.thePlayer.inventoryContainer.getSlot(i);
                if (slot.getHasStack()) {
                    ItemStack is = slot.getStack();
                    if (is.getUnlocalizedName().contains(typeStr) && getProtScore(is) > prot) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private void fakeOpen() {
        if (!isInvOpen) {
            timer.reset();
            if (this.inventoryOnly && this.inventoryPackets)
                mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            isInvOpen = true;
        }
    }

    private void fakeClose() {
        if (isInvOpen) {
            if (!this.inventoryOnly && this.inventoryPackets)
                mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(mc.thePlayer.inventoryContainer.windowId));
            isInvOpen = false;
        }
    }

    public void swap1(int slot, int hSlot) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hSlot, 2, mc.thePlayer);
    }

    private void swap(int slot, int hSlot) {
        fakeOpen();
        swap1(slot, hSlot);
        fakeClose();
        timer.reset();
    }

    private boolean stop() {
        return (this.inventoryOnly && !(mc.currentScreen instanceof GuiInventory)) || (this.onlyWhileNotMoving);
    }

    private enum ItemType {
        WEAPON, PICKAXE, AXE, SHOVEL, BLOCK, BOW, GOLDEN_APPLE, EPEARL
    }
}