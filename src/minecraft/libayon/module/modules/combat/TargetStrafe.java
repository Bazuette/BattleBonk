package libayon.module.modules.combat;

import libayon.BattleBonk;
import libayon.event.events.client.UpdateEvent;
import libayon.event.events.packets.PacketReceivedEvent;
import libayon.event.events.player.PostMotionEvent;
import libayon.event.events.player.PreMotionEvent;
import libayon.event.events.render.Render2DEvent;
import libayon.event.events.render.Render3DEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import libayon.utils.movement.MovementUtils;
import libayon.utils.player.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class TargetStrafe extends Module {
    static Minecraft mc = Minecraft.getMinecraft();
    private static int strafe = 1;

    public TargetStrafe() {
        super("TargetStrafe", "TargetStrafe", 0, Category.COMBAT, Mode.NONE);
    }

    public static boolean strafe() {
        return strafe(MovementUtils.getSpeed());
    }

    public static boolean strafe(double moveSpeed) {
        if (canStrafe()) {
            MovementUtils.setSpeed(moveSpeed, RotationUtils.getYaw(KillAura.target.getPositionVector()), strafe, mc.thePlayer.getDistanceToEntity(KillAura.target) <= 2 ? 0 : 1);
            return true;
        }
        return false;
    }

    public static boolean canStrafe() {
        return true;
    }

    @Override
    public void setup() {
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onPacketReceived(PacketReceivedEvent event) {
    }

    @Override
    public void onUpdate(UpdateEvent e) {
    }

    @Override
    public void onRender2D(Render2DEvent e) {

    }

    @Override
    public void onRender3D(Render3DEvent e) {
        if (KillAura.target != null) {
            double rad = 2;
            boolean shade = true;
            EntityLivingBase entity = (EntityLivingBase) KillAura.target;
            Minecraft mc = Minecraft.getMinecraft();
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glEnable(2832);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glHint(3154, 4354);
            GL11.glHint(3155, 4354);
            GL11.glHint(3153, 4354);
            GL11.glDepthMask(false);
            GlStateManager.alphaFunc(516, 0.0F);
            if (shade)
                GL11.glShadeModel(7425);
            GlStateManager.disableCull();
            GL11.glBegin(5);
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosX;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosY;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosZ;
            float i;
            for (i = 0.0F; i < 6.283185307179586D; i = (float) (i + 0.09817477042468103D)) {
                double vecX = x + rad * Math.cos(i);
                double vecZ = z + rad * Math.sin(i);
                Color c = new Color(255, 255, 255, 130);
                if (shade) {
                    GL11.glColor4f(c.getRed() / 255.0F, c
                            .getGreen() / 255.0F, c
                            .getBlue() / 255.0F, 0.0F);
                    GL11.glVertex3d(vecX, y - Math.cos(System.currentTimeMillis() / 200.0D) / 2.0D, vecZ);
                    GL11.glColor4f(c.getRed() / 255.0F, c
                            .getGreen() / 255.0F, c
                            .getBlue() / 255.0F, 0.85F);
                }
                GL11.glVertex3d(vecX, y, vecZ);
            }
            GL11.glEnd();
            if (shade)
                GL11.glShadeModel(7424);
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableCull();
            GL11.glDisable(2848);
            GL11.glDisable(2848);
            GL11.glEnable(2832);
            GL11.glEnable(3553);
            GL11.glPopMatrix();
            GL11.glColor3f(255.0F, 255.0F, 255.0F);
        }
    }

    @Override
    public void onPreMotion(PreMotionEvent e) {
        if (!BattleBonk.instance.getModuleManager().getModule(KillAura.class).isToggled()) return;
        strafe(0.23);
        if (mc.thePlayer.isCollidedHorizontally) strafe = -strafe;
        if (mc.thePlayer.onGround) {
            mc.thePlayer.jump();
        }
    }

    @Override
    public void onPostMotion(PostMotionEvent e) {
    }

}