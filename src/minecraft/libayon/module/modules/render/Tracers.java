package libayon.module.modules.render;

import libayon.event.events.render.Render2DEvent;
import libayon.event.events.render.Render3DEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import libayon.utils.player.PlayerUtils;
import libayon.utils.render.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class Tracers extends Module {

	public Tracers() {
		super("Tracers", "Shows where players are", 0, Category.RENDER,Mode.NONE);
	}



	@Override
	public void onRender3D(Render3DEvent e) {
		for (Entity entity : mc.theWorld.loadedEntityList) {
			if (entity instanceof EntityPlayer && entity != mc.thePlayer && !PlayerUtils.isSameTeam(entity)) {
				double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosX;
			    double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosY;
			    double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosZ;
			    RenderUtils.drawTracerLine(x, y, z, 0, 1, 1, 1, 0.00001f);
			}
		}
	}
	@Override
	public void onRender2D(Render2DEvent e) {
		for (Entity entity : mc.theWorld.loadedEntityList) {
			if (entity instanceof EntityPlayer && entity != mc.thePlayer) {
			    
			}
		}
	}

	@Override
	public void onDisable() {
	}
}
