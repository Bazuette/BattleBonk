package libayon.utils.render;

import libayon.BattleBonk;
import libayon.font.Font;
import libayon.font.UnicodeFontRenderer;
import libayon.utils.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderUtils {

    public static Font getFontRenderer() {
        return BattleBonk.instance.getFontRenderer();
    }

    public static void drawTargetHud(Entity target) {
        if (target != null) {
            EntityLivingBase tar = (EntityLivingBase) target;
            ScaledResolution sr = RenderUtils.getScaledResolution();
            UnicodeFontRenderer fr = RenderUtils.getFontRenderer().getFont();

            String name = tar.getName() + "  ";
            int nameWidth = fr.getStringWidth(name);
            String verb = "Bonking ";
            int verbWidth = fr.getStringWidth(verb);

            int x = sr.getScaledWidth();
            int y = sr.getScaledHeight();

            float hue = (System.currentTimeMillis() % 2000) / 2000f;
            float namehue = (System.currentTimeMillis() % 5000) / 5000f;

            int namecolor = Color.HSBtoRGB(namehue, 0.7f, 1);

            int outlinecolor = Color.HSBtoRGB(hue, 0.6f, 1);
            int boxcolor = new Color(0, 0, 0, 87).getRGB();

            if (fr.getStringWidth(tar.getPosition().getX() + "    " + tar.getPosition().getY() + "   " + tar.getPosition().getZ()) < nameWidth + fr.getStringWidth(verb)) {
                RoundedUtils.drawRoundedOutline(x / 2 + 58, y / 2 - 3, x / 2 + 65 + ((nameWidth + verbWidth)) - 7, y / 2 + 32, 9, 2, outlinecolor);
                RoundedUtils.drawSmoothRoundedRect(x / 2 + 58, y / 2 - 3, x / 2 + 65 + ((nameWidth + verbWidth)) - 7, y / 2 + 32, 9, boxcolor);
            } else {
                RoundedUtils.drawSmoothRoundedRect(x / 2 + 58, y / 2 - 3, x / 2 + 65 + ((nameWidth + verbWidth)) + 10, y / 2 + 32, 9, boxcolor);
                RoundedUtils.drawRoundedOutline(x / 2 + 58, y / 2 - 3, x / 2 + 65 + ((nameWidth + verbWidth)) + 10, y / 2 + 32, 9, 2, outlinecolor);
            }


            RenderUtils.drawString(verb, x / 2 + 60, y / 2, Color.YELLOW.getRGB(), true);
            Minecraft.getMinecraft().fontRendererObj.drawString("\u2764", x / 2 + 60, y / 2 + 10, Color.RED.getRGB(), true);
            RenderUtils.drawString(name, x / 2 + 60 + verbWidth, y / 2, namecolor, 20, true);
            RenderUtils.drawString("     " + Math.round(tar.getHealth()), x / 2 + 60, y / 2 + 10, -1, 17, true);
            double distance = Math.round(tar.getDistanceToEntity(Minecraft.getMinecraft().thePlayer) * 100.0) / 100.0;
            RenderUtils.drawString("DIST: " + distance, x / 2 + 60, y / 2 + 20, -1, 17, true);
        }
    }

    public static void renderItem(int xPos, int yPos, ItemStack itemStack) {
        GuiIngame guiInGame = Minecraft.getMinecraft().ingameGUI;

        if (guiInGame == null)
            return;

        if (itemStack == null)
            return;

        RenderItem itemRenderer = guiInGame.itemRenderer;

        Minecraft.getMinecraft().getTextureManager().bindTexture(guiInGame.getWidgetsTexPath());

        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderHelper.enableGUIStandardItemLighting();

        RenderHelper.enableGUIStandardItemLighting();
        itemRenderer.renderItemAndEffectIntoGUI(itemStack, xPos, yPos);

        itemRenderer.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRendererObj, itemStack, xPos, yPos, "");
        RenderHelper.disableStandardItemLighting();

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
    }

    public static void drawCircle(Entity entity, double rad, int color, boolean shade) {
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
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosY + Math.sin(System.currentTimeMillis() / 200.0D) + 1.0D;
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

    public static void drawString(String text, int x, int y, int color, boolean shadow, UnicodeFontRenderer font) {
        if (color == -1)
            color = Color.WHITE.getRGB();

        text = Strings.simpleTranslateColors(text);
        if (shadow)
            font.drawStringWithShadow(text, x, y, color);
        else
            font.drawString(text, x, y, color);
    }

    public static void drawString(String text, int x, int y, int color, int fontSize, boolean shadow) {
        drawString(text, x, y, color, shadow, getFontRenderer().getFont(fontSize));
    }

    public static void drawString(String text, int x, int y, int color, boolean shadow) {
        drawString(text, x, y, color, getFontRenderer().getFontSize(), shadow);
    }

    public static void drawString(String text, int x, int y, int color, int fontSize) {
        drawString(text, x, y, color, fontSize, true);
    }

    public static void drawString(String text, int x, int y, int color) {
        if (color == -1)
            color = Color.WHITE.getRGB();
        drawString(text, x, y, color, true);
    }

    public static void drawStringFromTopRight(String text, int x, int y, int color, int fontSize, boolean shadow) {
        drawString(text, getScaledResolution().getScaledWidth() - Strings.getStringWidthCFR(text) - x, y, color, shadow);
    }

    public static void drawStringFromTopRight(String text, int x, int y, int color, boolean shadow) {
        drawStringFromTopRight(text, x, y, color, getFontRenderer().getFontSize(), shadow);
    }

    public static void drawStringFromTopRight(String text, int x, int y, int color, int fontSize) {
        drawStringFromTopRight(text, x, y, color, fontSize, true);
    }

    public static void drawStringFromTopRight(String text, int x, int y, int color) {
        drawStringFromTopRight(text, x, y, color, getFontRenderer().getFontSize(), true);
    }

    public static void drawStringFromBottomRight(String text, int x, int y, int color, int fontSize, boolean shadow) {
        drawStringFromTopRight(text, x, getScaledResolution().getScaledHeight() - y * 2, color, fontSize, shadow);
    }

    public static void drawStringFromBottomRight(String text, int x, int y, int color, boolean shadow) {
        drawStringFromBottomRight(text, x, y, color, getFontRenderer().getFontSize(), true);
    }

    public static void drawStringFromBottomRight(String text, int x, int y, int color, int fontSize) {
        drawStringFromBottomRight(text, x, y, color, fontSize, true);
    }

    public static void drawStringFromBottomRight(String text, int x, int y, int color) {
        drawStringFromBottomRight(text, x, y, color, getFontRenderer().getFontSize());
    }

    public static void drawStringFromBottomLeft(String text, int x, int y, int color) {
        drawStringFromBottomLeft(text, x, y, color, getFontRenderer().getFontSize(), true);
    }

    public static void drawStringFromBottomLeft(String text, int x, int y, int color, int fontSize) {
        drawStringFromBottomLeft(text, x, y, color, fontSize, true);
    }

    public static void drawStringFromBottomLeft(String text, int x, int y, int color, int fontSize, boolean shadow) {
        drawString(text, x, getScaledResolution().getScaledHeight() - y * 2, color, fontSize, shadow);
    }

    public static void drawCenteredString(String name, int width, int height, int color, int fontsize, boolean shadow) {
        drawString(name, width - getFontRenderer().getFont(fontsize).getStringWidth(name) / 2, height - getFontRenderer().getFont(fontsize).getStringHeight(name) / 2, color, fontsize, shadow);
    }

    public static void drawRect(int left, int top, int right, int bottom, int color) {
        Gui.drawRect(left, top, right, bottom, color);
    }

    public static void drawModalRect(int xCord, int yCord, int width, int height, int color) {
        Gui.drawRect(xCord, getScaledResolution().getScaledHeight() - yCord, xCord + width, getScaledResolution().getScaledHeight() - yCord - height, color);
    }

    public static void drawModalRectFromRight(int xCord, int yCord, int width, int height, int color) {
        drawModalRect(getScaledResolution().getScaledWidth() - xCord, yCord, width, height, color);
    }

    public static void drawModalRectFromTopRight(int xCord, int yCord, int width, int height, int color) {
        Gui.drawRect(getScaledResolution().getScaledWidth() - xCord, yCord, getScaledResolution().getScaledWidth() - xCord + width, yCord + height, color);
    }

    public static void drawModalRectFromTopLeft(int xCord, int yCord, int width, int height, int color) {
        Gui.drawRect(xCord, yCord, xCord + width, yCord + height, color);
    }

    public static ScaledResolution getScaledResolution() {
        return new ScaledResolution(Minecraft.getMinecraft());
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        int[] colors = new int[]{10, 0, 30, 15};
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();

//		worldRenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
//		worldRenderer.pos(-259, 102, 19).endVertex();
//		worldRenderer.pos(-259, 104, 17).endVertex();
//		worldRenderer.pos(-257, 102, 19).endVertex();
//		worldRenderer.pos(-257, 104, 19).endVertex();
//		tessellator.draw();

        worldRenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ);
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawOutlinedBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        // GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(red, green, blue, alpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        // GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        // GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        // GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawSolidBlockESP(double x, double y, double z, float red, float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        // GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        // GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawOutlinedEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        // GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        // GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawSolidEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        // GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        // GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        // GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glLineWidth(lineWdith);
        GL11.glColor4f(lineRed, lineGreen, lineBlue, lineAlpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        // GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawTracerLine(double x, double y, double z, float red, float green, float blue, float alpha, float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
//		GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(lineWdith);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(2);
        GL11.glVertex3d(0.0D, 0.0D + Minecraft.getMinecraft().thePlayer.getEyeHeight() - 0.2, 0.0D);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
//		GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    public static void renderBoundingBox(Entity entityIn, double x, double y, double z, float partialTicks, int red, int green, int blue, double xAdd, double yAdd, double zAdd) {
        GlStateManager.depthMask(false);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        AxisAlignedBB axisalignedbb = entityIn.getEntityBoundingBox();
        AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(axisalignedbb.minX + xAdd - entityIn.posX + x, axisalignedbb.minY - entityIn.posY + y, axisalignedbb.minZ + zAdd - entityIn.posZ + z, axisalignedbb.maxX - xAdd - entityIn.posX + x, axisalignedbb.maxY + yAdd - entityIn.posY + y, axisalignedbb.maxZ - zAdd - entityIn.posZ + z);

        RenderGlobal.func_181563_a(axisalignedbb1, red, green, blue, 255);

//		Tessellator tessellator = Tessellator.getInstance();
//		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
    }

    public static void renderPlayerESP(Entity entityIn, double x, double y, double z, int red, int green, int blue, float partialTicks) {
        renderBoundingBox(entityIn, x, y, z, partialTicks, red, green, blue, 0.75, 0.15, 0.75);
    }

    public static void renderItemsESP(Entity entityIn, double x, double y, double z, float partialTicks) {
        int red = 150;
        int green = 20;
        int blue = 150;

        renderBoundingBox(entityIn, x, y, z, partialTicks, red, green, blue, 0.35, 0.30, 0.35);
    }

    private void circle(Entity entity, double rad, Color color) {
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
        GlStateManager.disableCull();
        GL11.glBegin(5);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - (mc.getRenderManager()).viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - (mc.getRenderManager()).viewerPosY + 0.01D;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - (mc.getRenderManager()).viewerPosZ;
        for (int i = 0; i <= 90; i++) {
            GL11.glColor3f(255.0F, 255.0F, 255.0F);
            GL11.glVertex3d(x + rad * Math.cos(i * 6.283185307179586D / 45.0D), y, z + rad * Math.sin(i * 6.283185307179586D / 45.0D));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GlStateManager.enableCull();
        GL11.glDisable(2848);
        GL11.glDisable(2848);
        GL11.glEnable(2832);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
        GL11.glColor3f(255.0F, 255.0F, 255.0F);
    }

}