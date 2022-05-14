package libayon.module.modules.movement;

import libayon.event.events.client.UpdateEvent;
import libayon.event.events.packets.PacketReceivedEvent;
import libayon.event.events.player.StrafeEvent;
import libayon.event.events.render.Render2DEvent;
import libayon.event.events.render.Render3DEvent;
import libayon.module.Category;
import libayon.module.Mode;
import libayon.module.Module;

public class TestModule extends Module {

    public TestModule() {
        super("TestModule", "This is a test module...", 0, Category.MOVEMENT, Mode.AAC);
    }

    @Override
    public void setup() {
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onPacketReceived(PacketReceivedEvent event) {
    }

    @Override
    public void onUpdate(UpdateEvent e) {
    }

    @Override
    public void onRender2D(Render2DEvent e) {

    }

    @Override
    public void onRender3D(Render3DEvent e) {

    }

    @Override
    public void onStrafeEvent(StrafeEvent e) {

    }

}