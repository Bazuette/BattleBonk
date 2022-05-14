package libayon.overlay;

import libayon.BattleBonk;
import libayon.event.EventListener;
import libayon.event.events.render.Render2DEvent;
import libayon.font.UnicodeFontRenderer;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;
import libayon.utils.Strings;
import libayon.utils.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.List;

public class ToggledModules1 extends EventListener {
    int count = 0;

    public ToggledModules1() {
        BattleBonk.instance.getEventManager().registerListener(this);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        UnicodeFontRenderer fr = BattleBonk.instance.getRobotoFont().getFont(20);
        List<Module> modules = BattleBonk.instance.getModuleManager().getToggledModules();

        modules.sort((module1, module2) -> Strings.getStringWidthCFR(Strings.capitalizeFirstLetter(module2.getNameWithMode() + " ")) - Strings.getStringWidthCFR(Strings.capitalizeFirstLetter(module1.getNameWithMode() + " ")));
        int y = 1;


        for (int i = 0; i < modules.size(); i++) {
            ScaledResolution sr = RenderUtils.getScaledResolution();
            Module module = modules.get(i);
            int relativeYOffset = 0;
            int relativeXOffset = module.getMode() == Mode.NONE ? -4 : 27;

            int offset = BattleBonk.instance.getRobotoFont().getFontSize() / 2 + relativeYOffset;


            if (module.getCategory().equals(Category.HIDDEN) || !(module.isShownInModuleArrayList()))
                continue;

            String s = Strings.capitalizeFirstLetter(Strings.simpleTranslateColors(module.getNameWithMode()));
            int mWidth = fr.getStringWidth(s);
            int mode = module.getMode() != Mode.NONE ? 31 : 0;
            /** Draw the black background, EDITED THE ALPHA VALUE OF THE RGB CHANGE 0 to 255 TO SEE */
            RenderUtils.drawRect(event.getWidth() + 1 + mode - mWidth + -3 - 7 - relativeYOffset * 2, y - 1, event.getWidth() + -2, y + offset, new Color(0, 0, 0, 120).getRGB());
            float hue = ((System.currentTimeMillis() % 2000 + i * 90) / 2000f + i * 90);

            //int color = Color.HSBtoRGB(hue, 0.7f, 1f);

            int color = Color.HSBtoRGB(hue, 1f, 0.9f);


            RenderUtils.drawRect(event.getWidth() + 1 + mode - mWidth + -3 - 7 - relativeYOffset * 2, y - 1, event.getWidth() + -2 - mWidth - 7 + 1 + mode, y + offset, color);
            RenderUtils.drawRect(event.getWidth() + 4 + mode - relativeYOffset * 2, y - 1, event.getWidth() + -4 + 1, y + offset, color);
            fr.drawStringWithShadow(s, event.getWidth() - mWidth + relativeXOffset - 2, y, color);
            y += offset + 1;
        }
    }

}