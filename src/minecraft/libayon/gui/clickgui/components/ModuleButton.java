package libayon.gui.clickgui.components;

import libayon.BattleBonk;
import libayon.font.UnicodeFontRenderer;
import libayon.gui.clickgui.ClickGui;
import libayon.gui.clickgui.GuiBind;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import libayon.module.ModuleSettings;
import libayon.utils.Integers;
import libayon.utils.Strings;
import libayon.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModuleButton extends GuiButton {
    private final BattleBonk bb = BattleBonk.instance;
    private final ClickGui clickGui;
    public Module module;
    UnicodeFontRenderer fr = RenderUtils.getFontRenderer().getFont();
    private Boolean moduleSettingsBoolean = false;
    private int mouseX, mouseY;

    public ModuleButton(int buttonId, int x, int y, int widthIn, int heightIn, Module module, ClickGui clickGui) {
        super(buttonId, x, y, widthIn, heightIn, Strings.capitalizeFirstLetter(module.getName()));
        this.module = module;
        this.clickGui = clickGui;
    }

    public static int[] getPosition(Dropdown dropdown, int buttonIndex) {
        int[] position = new int[2];

        position[0] = dropdown.getX() + 3;
        position[1] = dropdown.getY() + dropdown.getHeaderHeight() + ((buttonIndex) * (BattleBonk.instance.getFontRenderer().getFontSize() / 2)) + (1 * (buttonIndex));

        return position;
    }

    public Module getModule() {
        return module;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            this.mouseX = mouseX;
            this.mouseY = mouseY;
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (!this.enabled) {
                j = 10526880;
            } else if (this.hovered) {
                if (module.isToggled())
                    j = new Color(80, 255, 0).getRGB();
                else
                    j = 16777120;
            } else if (module.isToggled()) {
                j = new Color(0, 240, 0).getRGB();
            }

            RenderUtils.drawString(this.displayString, this.xPosition, this.yPosition, j);

            if (moduleSettingsBoolean) {
                if (getFilteredSettingsList().isEmpty())
                    return;
                int count = 0;
                int setting_count = 0;
                ScaledResolution sr = RenderUtils.getScaledResolution();
                int pos = fr.getStringWidth(getLongestModuleInCategory(module.getCategory()).getName()) + 8;
                int longestSettingWidth = fr.getStringWidth(getLongestSettingInModule(module)) + 8;

                for (String setting : getFilteredSettingsList()) {
                    if (setting.contains("Current ")) continue;
                    setting_count++;
                }
                RenderUtils.drawRect(this.xPosition + pos - 2, this.yPosition, this.xPosition + pos - 2 + longestSettingWidth, this.yPosition + 1 + setting_count * 10, new Color(0, 0, 0, 200).getRGB());


                for (String setting : getFilteredSettingsList()) {
                    if (setting.contains("Current ")) continue;
                    boolean hovered = mouseX >= this.xPosition + pos && mouseY >= this.yPosition + count * 10 && mouseX < this.xPosition + pos + 3 + fr.getStringWidth(setting) && mouseY < this.yPosition + count * 10 + this.height;
                    RenderUtils.drawString(setting.substring(0, 1).toUpperCase() + setting.substring(1), this.xPosition + pos, this.yPosition + 1 + count * 10, hovered ? new Color(80, 255, 0).getRGB() : -1);

                    count++;

                }


            }

        }


    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        return false;
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY, int mouseButton) {

        boolean isPressed = super.mousePressed(mc, mouseX, mouseY);
        if ((mouseButton == 0 || mouseButton == 1) && moduleSettingsBoolean) {

            int count = 0;
            List<String> setconfig = getFilteredSettingsList();
            for (int i = 0; i < setconfig.size(); i++) {
                if (setconfig.get(i).contains("Current ")) continue;
                int pos = fr.getStringWidth(getLongestModuleInCategory(module.getCategory()).getName());
                boolean SettingPos = mouseX >= this.xPosition + pos && mouseY >= this.yPosition + count * 10 && mouseX < this.xPosition + pos + 3 + fr.getStringWidth(setconfig.get(i)) && mouseY < this.yPosition + count * 10 + this.height;
                if (SettingPos) {
                    String[] split = setconfig.get(i).split(": ");
                    String sec = StringUtils.capitalize(split[0]);
                    String value2 = StringUtils.capitalize(split[1]);
                    //Double
                    double direction = 0;
                    ModuleSettings c = module.getModuleSettings();
                    if (parseBoolean(split[1])) {
                        boolean Current = c.getBoolean(split[0]);
                        boolean New = !Current;
                        getModule().getModuleSettings().set(sec, New);

                    } else if (split[0].equalsIgnoreCase("Mode")) {
                        //Mode
                        Mode[] allowedModes = module.getallowedModes();
                        String value = split[1];
                        int currentMode = Arrays.asList(allowedModes).indexOf(Mode.valueOf(value.toUpperCase()));
                        int newMode = (direction == 0 ? (currentMode == 0 ? allowedModes.length - 1 : currentMode - 1) : (currentMode + 1 >= allowedModes.length ? 0 : currentMode + 1));
                        module.setMode(allowedModes[newMode]);
                    } else if (Integers.isInteger(split[1])) {
                        int n = mouseButton;
                        int value = 0;
                        if (n == 0) {
                            value = 1;
                        }
                        if (n == 1) {
                            value = -1;
                        }
                        int no = Integer.parseInt(split[1]) + value;
                        getModule().getModuleSettings().set(sec, no);
                    } else if (parseDouble(split[1])) {
                        double n = mouseButton;
                        if (n == 0) {
                            n = 0.1;
                        }
                        if (n == 1) {
                            n = -0.1;
                        }
                        double no = Double.parseDouble(split[1]) + n;
                        getModule().getModuleSettings().set(sec, Math.round(no * 100.0) / 100.0);
                    } else {
                        List<String> list = new ArrayList<>(Arrays.asList(split[1].split(", ")));
                        for (int n = 0; n < list.size(); n++) {
                            if (list.get(n).charAt(0) == '[' || list.get(n).endsWith("]")) {
                                list.set(n, list.get(n).replace(']', " ".charAt(0)).replace('[', " ".charAt(0)));

                                list.set(n, list.get(n).replace(']', " ".charAt(0)).replace('[', " ".charAt(0)).replace(" ", ""));
                            }
                        }
                        if (c.getString("Current " + split[0]) == null) {
                            c.set("Current " + split[0], list.get(0));
                        }

                    }
                    if (bb.getConfigYaml().getConfigurationSection(module.getName()).getString("Current " + split[0]) != null) {
                        List<String> list = bb.getConfigYaml().getConfigurationSection(module.getName()).getStringList(split[0]);
                        for (int n = 0; n < list.size(); n++) {
                            if (list.get(n).charAt(0) == '[' || list.get(n).endsWith("]")) {
                                list.set(n, list.get(n).replace(']', " ".charAt(0)).replace('[', " ".charAt(0)));

                                list.set(n, list.get(n).replace(']', " ".charAt(0)).replace('[', " ".charAt(0)).replace(" ", ""));
                            }

                        }
                        String current = bb.getConfigYaml().getConfigurationSection(module.getName()).getString("Current " + split[0]);
                        String next = list.indexOf(current) + 1 > list.size() - 1 ? list.get(0) : list.get(list.indexOf(current) + 1);
                        System.out.println(next);
                        c.set("Current " + split[0], next);

                    }
                    //End
                }
                count++;

            }
        }

        if (!isPressed)
            return false;

        if (mouseButton == 1) {
            if (module.getModuleSettings() != null) {
                moduleSettingsBoolean = !moduleSettingsBoolean;
            }
        }
        if (mouseButton == 2) {
            mc.displayGuiScreen(new GuiBind(module, clickGui));
        }

        return true;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (hovered)
            module.toggle();
    }

    private List<String> getFilteredSettingsList() {
        List<String> config = new ArrayList<>();
        for (Object s : bb.getConfigYaml().getConfigurationSection(module.getName()).getKeys(true)) {
            String first = s.toString() + ": ";
            String second = bb.getConfigYaml().getConfigurationSection(module.getName()).getString(s.toString());
            if (second.contains(",")) {
                String ch = bb.getConfigYaml().getConfigurationSection(module.getName()).getString(StringUtils.chop("Current " + first.replace(":", "")));
                second = ch;
            }
            config.add(first + second);
        }
        for (int i = 0; i < config.size(); i++) {
            if (config.get(i) == null)
                return config;
            if (config.get(i).contains("NONE")) {
                config.remove(i);
            }
            if (config.get(i).contains("toggled")) {
                config.remove(config.get(i));
            }
            if (config.get(i).contains("key")) {
                config.remove(config.get(i));
            }
        }
        return config;
    }

    private Module getLongestModuleInCategory(Category category) {
        Module longest = null;
        for (Module m : bb.getModuleManager().getModules()) {
            if (longest == null)
                longest = m;

            if (m.getCategory() == category && fr.getStringWidth(m.getName()) > fr.getStringWidth(longest.getName())) {
                longest = m;
            }
        }
        return longest;
    }

    private String getLongestSettingInModule(Module module) {
        String longest = null;
        for (String s : getFilteredSettingsList()) {
            if (longest == null)
                longest = s;
            if (fr.getStringWidth(longest) < fr.getStringWidth(s))
                longest = s;
        }
        return longest;
    }

    private boolean containsMode(String e) {
        boolean found = false;
        for (Mode m : module.getallowedModes()) {
            if (e.equalsIgnoreCase(m.toString())) {
                found = true;
            }
        }
        return found;
    }

    private boolean parseDouble(String s) {
        boolean yes = true;
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException e) {
            // TODO: handle NumberFormatException
            yes = false;
        }
        return yes;
    }

    private boolean parseBoolean(String s) {
        boolean yes = s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false");
        return yes;
    }

    public enum UpdateAction {
        NONE, UPDATE_POSITION, REPOPULATE
    }


}