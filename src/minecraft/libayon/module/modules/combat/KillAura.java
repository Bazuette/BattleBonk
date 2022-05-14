package libayon.module.modules.combat;

import libayon.BattleBonk;
import libayon.event.events.client.UpdateEvent;
import libayon.event.events.combat.AttackEvent;
import libayon.event.events.packets.PacketSentEvent;
import libayon.event.events.player.PostMotionEvent;
import libayon.event.events.player.PreMotionEvent;
import libayon.event.events.render.Render2DEvent;
import libayon.event.events.render.Render3DEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import libayon.module.modules.world.Scaffold;
import libayon.utils.Random;
import libayon.utils.player.PlayerUtils;
import libayon.utils.player.RotationUtils;
import libayon.utils.render.RenderUtils;
import libayon.utils.timers.Timer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class KillAura extends Module {
    public static float yaw = 0;
    public static float pitch = 0;
    public static Timer cpstimer = new Timer();
    public static Timer switchTimer = new Timer();
    public static boolean attack = false;
    public static double cps = 0;
    public static Entity target;
    List<Entity> targets = new ArrayList<>();

    public KillAura() {
        super("KillAura", "Auto attack players that are in range", 19, Category.COMBAT, Mode.VULCAN, Mode.VERUS);
    }

    @Override
    public void setup() {
        System.out.println("Yeah");
        moduleSettings.addDefault("Team Check", true);
        moduleSettings.addDefault("KeepSprint", true);
        moduleSettings.addDefault("Range", 5.0);
        moduleSettings.addDefault("Swing Range", 4.0);
        moduleSettings.addDefault("MinCPS", 6);
        moduleSettings.addDefault("MaxCPS", 15);
        moduleSettings.addDefault("Switch Delay", 0.5);

        List<String> BlockMode = new ArrayList<>();
        BlockMode.add("AcentraMC");
        BlockMode.add("None");
        moduleSettings.addDefault("BlockMode", BlockMode);
        moduleSettings.addDefault("Current BlockMode", BlockMode.get(0));

    }

    @Override
    public void onEnable() {
        attack = true;
        switchTimer.reset();
        target = null;
        targets.clear();
        mc.getNetHandler().getNetworkManager().sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
    }

    @Override
    public void onDisable() {
        yaw = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw);
        pitch = mc.thePlayer.rotationPitch;
        attack = false;
        mc.gameSettings.keyBindUseItem.setPressed(false);
    }

    @Override
    public void onAttack(AttackEvent e) {

    }

    @Override
    public void onUpdate(UpdateEvent e) {
    }

    @Override
    public void onPacketSent(PacketSentEvent event) {
        Packet p = event.getPacket();
        if (p instanceof C0BPacketEntityAction && ((C0BPacketEntityAction) p).getAction() == C0BPacketEntityAction.Action.START_SPRINTING) {

            event.setCancelled(true);
        }
        if (target != null && !(mc.currentScreen instanceof GuiInventory)) {

            float distance = mc.thePlayer.getDistanceToEntity(target);

            float[] rot = RotationUtils.limitAngleChange(new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch}, RotationUtils.getRotationsNeeded(target), 180);

            float yaw = RotationUtils.smoothRotation(mc.thePlayer.rotationYaw, rot[0], 180);
            float pitch = RotationUtils.smoothRotation(mc.thePlayer.rotationPitch, rot[1], 180);

            float m = 0.005f * (mc.gameSettings.mouseSensitivity) / 0.005f;
            float f = m * 0.6f + 0.2f;
            float gcd = m * m * m * 1.2f;

            yaw -= yaw % gcd;
            pitch -= pitch % gcd;

            if (getMode() == Mode.VERUS) {
                yaw = yaw + Random.randomFloat(-10, 10);
                pitch = pitch + Random.randomFloat(-10, 10);
            }


            pitch = MathHelper.clamp_float(pitch, -80, 80);


            if (p instanceof C03PacketPlayer) {
                C03PacketPlayer c03 = (C03PacketPlayer) p;
                c03.setPitch(pitch);
                c03.setYaw(yaw);
                //mc.thePlayer.rotationYaw = c03.getYaw();
                //mc.thePlayer.rotationPitch = c03.getPitch();
                RotationUtils.setRotations(c03.getYaw(), c03.getPitch());
            }
            KillAura.yaw = yaw;
            KillAura.pitch = pitch;

        }

    }


    @Override
    public void onPreMotion(PreMotionEvent event) {
        switch (moduleSettings.getString("Current BlockMode")) {
            case "None":
                break;
            case "AcentraMC":
                mc.gameSettings.keyBindUseItem.setPressed(true);
                break;
        }
        targets.clear();
        if (mc.currentScreen instanceof GuiInventory || BattleBonk.instance.getModuleManager().getModule(Scaffold.class).isToggled())
            return;

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity == mc.thePlayer) continue;
            if (!(entity instanceof EntityLivingBase)) continue;
            if (entity.getDistanceToEntity(mc.thePlayer) > moduleSettings.getDouble("Range")) continue;
            if (PlayerUtils.isSameTeam(entity) && moduleSettings.getBoolean("Team Check")) continue;
            if (!entity.isEntityAlive()) continue;

            targets.add(entity);
        }

        if (switchTimer.hasReached(moduleSettings.getDouble("Switch Delay") * 1000) && target != null) {
            boolean switched = false;
            for (Entity en : targets) {
                if ((en.getPosition().getX() != target.getPosition().getX() || en.getPosition().getY() != target.getPosition().getY() || en.getPosition().getZ() != target.getPosition().getZ()) && !switched) {
                    switched = true;
                    System.out.println(en.getPosition() + "      " + target.getPosition());
                    target = en;
                }
            }
            switchTimer.reset();
        } else {
            if (!targets.isEmpty() && target == null) {
                target = targets.get(0);
            }
        }

        if (target == null || !target.isEntityAlive()) {
            attack = true;
            target = null;
            return;
        }
        if (target != null && target.getDistanceToEntity(mc.thePlayer) > moduleSettings.getDouble("Range")) {
            target = null;
            targets.clear();
            attack = true;
        }
        if (true) {
            AttackEvent ae = new AttackEvent(target);
            BattleBonk.instance.getEventManager().call(ae);
            if (ae.isCancelled())
                return;
            int mincps = moduleSettings.getInt("MinCPS");
            int maxcps = moduleSettings.getInt("MaxCPS");

            double cps2 = Random.randomDouble(mincps, maxcps);
            cps = cps2;
            if (cpstimer.hasReached(1000 / cps2) || attack) {
                doAttack();
                cpstimer.reset();
                attack = false;
            }


        }

        return;
    }

    @Override
    public void onRender3D(Render3DEvent e) {
        if (target == null)
            return;
        RenderUtils.drawCircle(target, 0.58D, new Color(255, 255, 255, 130).getRGB(), true);
    }

    @Override
    public void onPostMotion(PostMotionEvent e) {
    }

    @Override
    public void onRender2D(Render2DEvent e) {
        RenderUtils.drawTargetHud(target);
    }

    private void doAttack() {
        if (target == null) return;
        cpstimer.reset();
        mc.thePlayer.swingItem();
        if (moduleSettings.getBoolean("KeepSprint")) {
            mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
        } else {
            mc.playerController.attackEntity(mc.thePlayer, target);
        }
    }

    private boolean canHit() {
        return RotationUtils.isFaced(target, moduleSettings.getDouble("Swing Range"));
    }

}
