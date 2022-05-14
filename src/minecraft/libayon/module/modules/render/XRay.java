package libayon.module.modules.render;

import libayon.BattleBonk;
import libayon.event.events.render.BlockBrightnessRequestEvent;
import libayon.event.events.render.BlockRenderEvent;
import libayon.event.events.render.FluidRenderEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class XRay extends Module {

    private List<String> exceptions;

    public XRay() {
        super("XRay", "See only specific blocks", Keyboard.KEY_X, Category.RENDER, Mode.NONE);
    }

    @Override
    public void setup() {
        moduleSettings.addDefault("blocks", Arrays.asList(Blocks.iron_ore.getLocalizedName().toUpperCase().replace(" ", "_")));

        Function<String, String> consumer = line -> line = line.toUpperCase().replace(" ", "_"); // Replaces every line with an uppercase version that has spaces replaced with underscores
        this.exceptions = moduleSettings.getStringList("blocks");
    }

    @Override
    public void onEnable() {
        mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onBlockBrightnessRequest(BlockBrightnessRequestEvent event) {
        if (isInExceptions(event.getBlock()))
            event.setBlockBrightness(15);
    }

    @Override
    public void onBlockRender(BlockRenderEvent event) {
        if (!(isInExceptions(event.getBlock())))
            event.setCancelled(true);
        else
            event.setForceDraw(true);
    }

    @Override
    public void onFluidRender(FluidRenderEvent event) {
        if (!(isInExceptions(event.getBlock())))
            event.setCancelled(true);
        else
            event.setForceDraw(true);
    }

    private boolean isInExceptions(Block block) {
        if (exceptions == null)
            return false;

        String unlocalizedName = BattleBonk.instance.getEnglishLocale().formatMessage(block.getUnlocalizedName() + ".name", null).toUpperCase().replace(" ", "_");

        return exceptions.contains(unlocalizedName) || exceptions.contains(Integer.toString(Block.getIdFromBlock(block)));
    }

    public List<String> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<String> exceptions) {
        this.exceptions = exceptions;
        moduleSettings.set("blocks", exceptions);

        if (isToggled())
            mc.renderGlobal.loadRenderers();
    }

}
