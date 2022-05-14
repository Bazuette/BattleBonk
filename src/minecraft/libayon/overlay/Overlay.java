package libayon.overlay;

import libayon.BattleBonk;
import libayon.event.EventListener;
import libayon.event.events.render.Render2DEvent;
import libayon.font.UnicodeFontRenderer;
import libayon.gui.clickgui.ClickGui;
import libayon.gui.clickgui.GuiBind;
import libayon.notifications.Notification;
import libayon.notifications.NotificationManager;
import libayon.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ConcurrentModificationException;

public class Overlay extends EventListener {

    public Overlay() {
        BattleBonk.instance.getEventManager().registerListener(this);
    }

    /**
     * The rest of the code is in GuiIngame#renderTooltip()
     */

    @Override
    public void onRender2D(Render2DEvent event) {
        if (BattleBonk.instance.isDefaultHotbar())
            return;


        GuiScreen currentScreen = mc.currentScreen;

        if (currentScreen != null && !(currentScreen instanceof ClickGui) && !(currentScreen instanceof GuiBind))
            return;
        ScaledResolution sr = RenderUtils.getScaledResolution();
        UnicodeFontRenderer fr = RenderUtils.getFontRenderer().getFont();

        String string = BattleBonk.instance.getClientName() + " - " + BattleBonk.instance.getClientVersion();
        int offset = 400;
        float hue = (System.currentTimeMillis() % 2000 + offset * 200) / 2000f + offset * 200;
        int namecolor = Color.HSBtoRGB(hue, 0.4f, 1);

        RenderUtils.drawModalRectFromTopLeft(3, 5, BattleBonk.instance.getUSFont().getFont(40).getStringWidth(string) + 5, 20, new Color(0, 0, 0, 200).getRGB());
        BattleBonk.instance.getUSFont().getFont(40).drawStringWithShadow(string, 5, 5, namecolor);
        RenderUtils.drawModalRectFromTopLeft(5, 23, BattleBonk.instance.getUSFont().getFont(40).getStringWidth(string) + 1, 1, namecolor);


        renderText(event, Minecraft.getDebugFPS() + "&7[&dFPS&7]&5 ", 2, 10);
        renderText(event, (double) Math.round((Math.hypot(this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX, this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ) * this.mc.timer.timerSpeed * 20.0D) * 10) / 10 + "&7[&dBPS&7]&5 ", 2, 17);

        if (!NotificationManager.getNotificationList().isEmpty())
            try {
                for (Notification not : NotificationManager.getNotificationList()) {
                    if (not.getTimer().hasReached(1000)) {
                        NotificationManager.getNotificationList().remove(not);
                    }
                    RenderUtils.drawStringFromBottomLeft(not.getMsg(), 10, 10, -1);
                    RenderUtils.drawModalRect(8, 8, fr.getStringWidth(not.getMsg()) + 6, fr.FONT_HEIGHT + 2, new Color(0, 0, 0, 70).getRGB());
                }
            } catch (ConcurrentModificationException e) {

            }

    }

    private void renderText(Render2DEvent event, String text, int x, int y) {
        String fpsText = text;
        RenderUtils.drawStringFromBottomRight(fpsText, x, y, -1);
    }

}