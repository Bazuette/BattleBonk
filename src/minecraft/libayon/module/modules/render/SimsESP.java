package libayon.module.modules.render;

import libayon.event.events.render.Render3DEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import libayon.utils.player.PlayerUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class SimsESP extends Module {

    public SimsESP() {
        super("SimsESP", "What else do u expect its a sims ESP retard", 0, Category.RENDER, Mode.NONE);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
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
        GL11.glDisable(2929);
        GL11.glFrontFace(2304);
        int i = 0;
        for (EntityPlayer en : mc.theWorld.playerEntities) {
            if (en.isInvisible())
                continue;
            if (en == mc.thePlayer)
                continue;
            if (PlayerUtils.isSameTeam(en))
                continue;
            i++;
            Color color = Color.GREEN;
            if (en.hurtTime > 0)
                color = Color.RED;
            GL11.glBegin(5);
            GL11.glColor3f(color.getRed(), color.getGreen(), color.getBlue());
            double x = en.lastTickPosX + (en.posX - en.lastTickPosX) * mc.timer.renderPartialTicks - (mc.getRenderManager()).viewerPosX;
            double y = en.lastTickPosY + (en.posY - en.lastTickPosY) * mc.timer.renderPartialTicks - (mc.getRenderManager()).viewerPosY + en.getEyeHeight() + 0.4D + Math.sin(((float) (System.currentTimeMillis() % 1000000L) / 333.0F + i)) / 10.0D;
            double z = en.lastTickPosZ + (en.posZ - en.lastTickPosZ) * mc.timer.renderPartialTicks - (mc.getRenderManager()).viewerPosZ;
            GL11.glColor3f(color.darker().darker().getRed(), color.darker().darker().getGreen(), color.darker().darker().getBlue());
            GL11.glVertex3d(x, y, z);
            GL11.glVertex3d(x - 0.1D, y + 0.3D, z - 0.1D);
            GL11.glVertex3d(x - 0.1D, y + 0.3D, z + 0.1D);
            GL11.glColor3f(color.getRed(), color.getGreen(), color.getBlue());
            GL11.glVertex3d(x + 0.1D, y + 0.3D, z);
            GL11.glColor3f(color.darker().darker().getRed(), color.darker().darker().getGreen(), color.darker().darker().getBlue());
            GL11.glVertex3d(x, y, z);
            GL11.glColor3f(color.darker().darker().darker().getRed(), color.darker().darker().darker().getGreen(), color.darker().darker().darker().getBlue());
            GL11.glVertex3d(x + 0.1D, y + 0.3D, z);
            GL11.glVertex3d(x - 0.1D, y + 0.3D, z - 0.1D);
            GL11.glEnd();
        }
        GL11.glShadeModel(7424);
        GL11.glFrontFace(2305);
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glCullFace(1029);
        GlStateManager.enableCull();
        GL11.glDisable(2848);
        GL11.glEnable(2832);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
        GL11.glColor3f(255.0F, 255.0F, 255.0F);
    }
}
