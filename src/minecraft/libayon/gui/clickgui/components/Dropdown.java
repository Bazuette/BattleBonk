package libayon.gui.clickgui.components;

import libayon.BattleBonk;
import libayon.gui.clickgui.ClickGui;
import libayon.module.Category;
import libayon.module.Module;
import libayon.utils.Strings;
import net.minecraft.client.gui.GuiButton;

import java.util.ArrayList;
import java.util.List;

public class Dropdown {
    private final ClickGui clickGui;
    private final int fontSize;
    private final List<ModuleButton> moduleButtons;
    private Category category;
    private int x;
    private int y;
    private int width;
    private int height;
    private int headerHeight;
    private boolean dragging;
    private boolean extended;
    private List<Module> modules;

    public Dropdown(ClickGui clickGui, Category category, int x, int y, boolean extended) {
        this.clickGui = clickGui;
        this.category = category;
        this.x = x;
        this.y = y;
        this.extended = extended;
        this.modules = BattleBonk.instance.getModuleManager().getModules(category);
        this.moduleButtons = new ArrayList<>();
        this.modules.sort((module1, module2) -> Strings.getStringWidthCFR(Strings.capitalizeFirstLetter(module2.getName())) - Strings.getStringWidthCFR(Strings.capitalizeFirstLetter(module1.getName())));
        this.fontSize = BattleBonk.instance.getFontRenderer().getFontSize() / 2;
        this.width = Strings.getStringWidthCFR(category.name()) - 5;
        updateHeight();
        this.width += 0;
        updateButtons(ModuleButton.UpdateAction.REPOPULATE);
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
        updateHeight();
        updateButtons(ModuleButton.UpdateAction.UPDATE_POSITION);
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
        updateHeight();
        updateButtons(ModuleButton.UpdateAction.UPDATE_POSITION);
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeaderHeight() {
        return this.headerHeight;
    }

    public boolean isDragging() {
        return this.dragging;
    }

    public boolean isExtended() {
        return this.extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
        updateHeight();
    }

    public void toggleExtend() {
        this.extended = !this.extended;
        updateHeight();
        updateButtons();
    }

    private void updateHeight() {
        this.height = this.fontSize * (this.extended ? this.modules.size() : 0) + 6;
        this.headerHeight = this.fontSize + 6;
    }

    private void updateButtons() {
        updateButtons(ModuleButton.UpdateAction.NONE);
    }

    private void updateButtons(ModuleButton.UpdateAction action) {
        if (action.equals(ModuleButton.UpdateAction.REPOPULATE)) {
            this.moduleButtons.clear();
            for (int i = 0; i < this.modules.size(); i++) {
                Module module = this.modules.get(i);
                int moduleWidth = Strings.getStringWidthCFR(Strings.capitalizeFirstLetter(module.getName()));
                moduleWidth += 9;
                if (moduleWidth > this.width)
                    this.width = moduleWidth;
                int[] position = ModuleButton.getPosition(this, i);
                this.moduleButtons.add(new ModuleButton(i, position[0], position[1], moduleWidth, this.fontSize, module, this.clickGui));
            }
        } else if (action.equals(ModuleButton.UpdateAction.UPDATE_POSITION)) {
            for (int i = 0; i < this.moduleButtons.size(); i++) {
                ModuleButton button = this.moduleButtons.get(i);
                int[] position = ModuleButton.getPosition(this, i);
                button.xPosition = position[0];
                button.yPosition = position[1];
            }
        }
        List<GuiButton> buttonList = new ArrayList<>(this.clickGui.getButtonList());
        buttonList.removeAll(this.moduleButtons);
        if (this.extended)
            buttonList.addAll(this.moduleButtons);
        this.clickGui.setButtonList(buttonList);
    }

    public List<Module> getModules() {
        return this.modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public List<ModuleButton> getModuleButtons() {
        return this.moduleButtons;
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isHovered(mouseX, mouseY)) {
            this.dragging = true;
            return true;
        }
        if (mouseButton == 1 && isHovered(mouseX, mouseY)) {
            if (this.modules.size() > 0)
                toggleExtend();
            return true;
        }
        return false;
    }

    public boolean mouseReleased(int mouseX, int mouseY, int state) {
        if (this.dragging)
            return !(this.dragging = false);
        return false;
    }

    public boolean mouseClickMove(int mouseX, int mouseY, int mouseButton, long timeSinceLastClick) {
        if (mouseButton == 0 && this.dragging) {
            this.x = mouseX - this.width / 2;
            this.y = mouseY - this.headerHeight / 2;
            updateButtons(ModuleButton.UpdateAction.UPDATE_POSITION);
            return true;
        }
        return false;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.fontSize + 5);
    }
}
