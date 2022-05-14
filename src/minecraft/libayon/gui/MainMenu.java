package libayon.gui;

import libayon.BattleBonk;
import libayon.font.UnicodeFontRenderer;
import libayon.gui.altmanager.GuiAltManager;
import libayon.utils.render.RenderUtils;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import viamcp.gui.GuiProtocolSelector;

import java.awt.*;
import java.io.IOException;

public class MainMenu extends GuiScreen {
    public BattleBonk bb = BattleBonk.instance;
    String[] buttons = {"Singleplayer", "Multiplayer", "Alt Manager", "Language", "Settings", "Version", "Quit"};

    public MainMenu() {


    }

    public void initGui() {
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mc.gameSettings.ofFastRender = false;
        mc.getTextureManager().bindTexture(new ResourceLocation("background/Background.png"));
        drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);
        ScaledResolution sr = RenderUtils.getScaledResolution();
        UnicodeFontRenderer fr = BattleBonk.instance.getRobotoFont().getFont(26);
        int fontsize = mc.gameSettings.fullScreen ? 330 : 120;

        int count = 0;

        for (String name : buttons) {
            int x = sr.getScaledWidth() / 2;
            int y = (sr.getScaledHeight() / 2 + 5) + (15 * count);

            boolean hovered = mouseX >= x - fr.getStringWidth(name) / 2 && mouseY >= y + 5 - fr.FONT_HEIGHT / 2 && mouseX < x + fr.getStringWidth(name) / 2 && mouseY < y + fr.FONT_HEIGHT / 2;
            RenderUtils.drawString(name, x - fr.getStringWidth(name) / 2, sr.getScaledHeight() / 2 + (15 * count), hovered ? Color.yellow.getRGB() : Color.GRAY.getRGB(), true, fr);
            //RenderUtils.drawCenteredString(name, x, sr.getScaledHeight() / 2 + (15 * count), hovered ? Color.yellow.getRGB() : -1, 26, true);
            count++;
        }
        BattleBonk.instance.getVeganFont().getFont(fontsize).drawStringWithShadow(bb.getClientName(), sr.getScaledWidth() / 2 - BattleBonk.instance.getVeganFont().getFont(fontsize).getStringWidth(bb.getClientName()) / 2, height / 6 - bb.getVeganFont().getFont(fontsize).FONT_HEIGHT / 2, Color.yellow.getRGB());
        RenderUtils.drawCenteredString("Made by " + bb.getAuthor(), width / 2, height / 6 + fontsize / 4, Color.YELLOW.getRGB(), mc.gameSettings.fullScreen ? 35 : 20, true);


    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        int count = 0;
        for (String name : buttons) {
            ScaledResolution sr = RenderUtils.getScaledResolution();
            UnicodeFontRenderer fr = BattleBonk.instance.getRobotoFont().getFont(26);
            int x = sr.getScaledWidth() / 2;
            int y = (sr.getScaledHeight() / 2 + 5) + (15 * count);
            if (mouseX >= x - fr.getStringWidth(name) / 2 && mouseY >= y + 5 - fr.FONT_HEIGHT / 2 && mouseX < x + fr.getStringWidth(name) / 2 && mouseY < y + fr.FONT_HEIGHT / 2) {
                switch (name) {
                    case "Singleplayer":
                        mc.displayGuiScreen(new GuiSelectWorld(this));
                        break;
                    case "Multiplayer":
                        mc.displayGuiScreen(new GuiMultiplayer(this));
                        break;
                    case "Settings":
                        mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                        break;
                    case "Language":
                        mc.displayGuiScreen(new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager()));
                        break;
                    case "Version":
                        //mc.shutdown();
                        this.mc.displayGuiScreen(new GuiProtocolSelector(this));
                        break;
                    case "Alt Manager":
                        mc.displayGuiScreen(new GuiAltManager());
                        break;
                    case "Quit":
                        mc.shutdown();
                        break;
                }
            }
            count++;
        }
    }

    public void onGuiClosed() {
    }

    protected void actionPerformed(GuiButton button) throws IOException {
    }

}
