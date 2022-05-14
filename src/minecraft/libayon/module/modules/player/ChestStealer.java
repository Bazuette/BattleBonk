package libayon.module.modules.player;

import libayon.event.events.packets.PacketReceivedEvent;
import libayon.event.events.packets.PacketSentEvent;
import libayon.event.events.player.PreMotionEvent;
import libayon.event.events.render.Render3DEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import libayon.utils.timers.Timer;
import net.minecraft.inventory.ContainerChest;

import java.util.ArrayList;
import java.util.List;

public class ChestStealer extends Module {
    private final Timer timer = new Timer();
    private final Timer closeTimer = new Timer();
    private final Timer startTimer = new Timer();
    private double delay = moduleSettings.getDouble("delay");
    private boolean closed = false;
    private boolean opened = false;

    public ChestStealer() {
        super("ChestStealer", "Auto steal stuff", 0, Category.PLAYER, Mode.NONE);
    }


    @Override
    public void setup() {
        moduleSettings.addDefault("Instant", true);
        moduleSettings.addDefault("Delay", 30.0);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onPreMotion(PreMotionEvent event) {
        delay = moduleSettings.getBoolean("Instant") ? -1 : moduleSettings.getDouble("Delay");
        if (mc.thePlayer.openContainer instanceof ContainerChest) {

            ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
            String chestName = chest.getLowerChestInventory().getName();
            if (!((chestName.contains("Chest") && !chestName.equals("Ender Chest")) || chestName.equals("LOW")))
                return;
            List<Integer> slots = new ArrayList<>();


            for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); i++) {
                if (chest.getLowerChestInventory().getStackInSlot(i) != null) {
                    if (!containsBlacklistedName(i, chest)) {
                        slots.add(i);
                    }
                }
            }
            if (!slots.isEmpty() && !opened) {
                startTimer.reset();
                opened = true;
            }
            if (startTimer.getTimePassed() < 100 && !moduleSettings.getBoolean("Instant")) {
                return;
            }
            for (int slot : slots) {
                if (timer.getTimePassed() > delay) {
                    timer.reset();
                    mc.playerController.windowClick(chest.windowId, slot, 0, 1, mc.thePlayer);
                }
            }

            if ((slots.isEmpty() || this.isInventoryFull()) && !closed) {
                closed = true;
                closeTimer.reset();
            }
            if (closeTimer.getTimePassed() > (moduleSettings.getBoolean("Instant") ? -1 : 175) && closed) {
                mc.thePlayer.closeScreen();
            }
        } else {
            opened = false;
            closed = false;
        }
        return;
    }

    @Override
    public void onPacketSent(PacketSentEvent event) {
    }

    @Override
    public void onPacketReceived(PacketReceivedEvent event) {
    }

    @Override
    public void onRender3D(Render3DEvent event) {
    }

    private boolean isInventoryFull() {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getStack() == null) {
                return false;
            }
        }
        return true;
    }

    private boolean containsBlacklistedName(int slot, ContainerChest chest) {
        String[] itemarray = {"flint", "bow", " egg ", "cake", "redstone", "melon", "anvil", "fishing rod", "lapis lazuli", "table", "steak", "arrow", "snowball", "tnt", "weakness"};
        Boolean statusBoolean = false;
        for (String item : itemarray) {
            if (chest.getInventory().get(slot).getDisplayName().toLowerCase().contains(item)) {
                statusBoolean = true;
            }

        }
        return statusBoolean;
    }
}