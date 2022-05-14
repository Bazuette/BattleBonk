package libayon.module;

import libayon.BattleBonk;
import libayon.event.EventListener;
import libayon.event.events.client.KeyPressedEvent;
import libayon.event.events.client.MouseClickEvent;
import libayon.module.modules.combat.*;
import libayon.module.modules.exploit.*;
import libayon.module.modules.misc.AntiDesync;
import libayon.module.modules.misc.ClientSpoofer;
import libayon.module.modules.movement.*;
import libayon.module.modules.player.*;
import libayon.module.modules.render.Fullbright;
import libayon.module.modules.render.SimsESP;
import libayon.module.modules.render.Tracers;
import libayon.module.modules.semi_hidden.ClickGui;
import libayon.module.modules.world.AutoFeed;
import libayon.module.modules.world.Scaffold;
import libayon.module.modules.world.Spammer;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager extends EventListener {
    private final List<Module> modules;

    public ModuleManager() {
        this.modules = new ArrayList<>();
        BattleBonk.instance.getEventManager().registerListener(this);
        registerModules();
    }

    public void registerModule(Module module) {
        this.modules.add(module);
    }

    public void registerModules() {
        registerModule(new TargetStrafe());
        registerModule(new Jesus());
        registerModule(new SimsESP());
        registerModule(new ClickTP());
        registerModule(new Crasher());
        registerModule(new Criticals());
        registerModule(new Fly());
        registerModule(new TestModule());
        registerModule(new ClickGui());
        registerModule(new Longjump());
        registerModule(new KillAura());
        registerModule(new Step());
        registerModule(new Speed());
        registerModule(new Fullbright());
        registerModule(new AntiVoid());
        registerModule(new Scaffold());
        registerModule(new Reach());
        registerModule(new NoSlow());
        registerModule(new NoFall());
        registerModule(new Phase());
        registerModule(new Velocity());
        registerModule(new ChestStealer());
        registerModule(new InvManager());
        registerModule(new ClientSpoofer());
        registerModule(new Disabler());
        registerModule(new Tracers());
        registerModule(new AntiDesync());
        registerModule(new Spammer());
        registerModule(new AutoSoup());
        registerModule(new AutoFeed());
        registerModule(new PacketDelayer());
        registerModule(new Blink());
        registerModule(new AntiBan());
    }

    public Module getModule(Class<? extends Module> clasz) {
        for (int i = 0; i < this.modules.size(); i++) {
            if (this.modules.get(i).getClass().equals(clasz))
                return this.modules.get(i);
        }
        return null;
    }

    public Module getModule(String name) {
        for (int i = 0; i < this.modules.size(); i++) {
            if (this.modules.get(i).getName().equalsIgnoreCase(name))
                return this.modules.get(i);
        }
        return null;
    }

    public List<Module> getModules(int key) {
        List<Module> modules = new ArrayList<>();
        for (int i = 0; i < this.modules.size(); i++) {
            if (this.modules.get(i).getKey() == key)
                modules.add(this.modules.get(i));
        }
        return modules;
    }

    public List<Module> getModules(Category category) {
        List<Module> modules = new ArrayList<>();
        for (int i = 0; i < this.modules.size(); i++) {
            Module module = this.modules.get(i);
            if (module.getCategory().equals(category))
                modules.add(module);
        }
        return modules;
    }

    public Module getModule(int key) {
        List<Module> modules = getModules(key);
        if (modules.size() == 0)
            return null;
        return modules.get(0);
    }

    public List<Module> getModules() {
        return this.modules;
    }

    public List<Module> getToggledModules() {
        List<Module> modules = new ArrayList<>();
        for (int i = 0; i < this.modules.size(); i++) {
            Module module = this.modules.get(i);
            if (module.isToggled())
                modules.add(module);
        }
        return modules;
    }

    public void onMouseClick(MouseClickEvent event) {
        List<Module> modules = new ArrayList<>(getModules(-event.getButton() - 1));
        for (int i = 0; i < modules.size(); i++)
            modules.get(i).toggle();
    }

    public void onKeyPressed(KeyPressedEvent event) {
        List<Module> modules = new ArrayList<>(getModules(event.getKey()));
        for (int i = 0; i < modules.size(); i++)
            modules.get(i).toggle();
    }
}
