package libayon.event.events.player;

import libayon.event.CancellableEvent;
import net.minecraft.client.Minecraft;

public class StrafeEvent extends CancellableEvent {
    Minecraft mc = Minecraft.getMinecraft();
    private float forward;

    private float strafe;

    private float friction;

    public StrafeEvent(float forward, float strafe, float friction) {
        this.forward = forward;
        this.strafe = strafe;
        this.friction = friction;
    }

    public float getForward() {
        return this.forward;
    }

    public void setForward(float forward) {
        this.forward = forward;
    }

    public float getStrafe() {
        return this.strafe;
    }

    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }

    public float getFriction() {
        return this.friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public void setSpeedPartialStrafe(float friction, float strafe) {
        float remainder = 1.0F - strafe;
        if (this.forward != 0.0F && this.strafe != 0.0F)
            friction = (float) (friction * 0.91D);
        if (mc.thePlayer.onGround) {
            setSpeed(friction);
        } else {
            mc.thePlayer.motionX *= strafe;
            mc.thePlayer.motionZ *= strafe;
            setFriction(friction * remainder);
        }
    }

    public void setSpeed(float speed, double motionMultiplier) {
        setFriction((getForward() != 0.0F && getStrafe() != 0.0F) ? (speed * 0.99F) : speed);
        mc.thePlayer.motionX *= motionMultiplier;
        mc.thePlayer.motionZ *= motionMultiplier;
    }

    public void setSpeed(float speed) {
        setFriction((getForward() != 0.0F && getStrafe() != 0.0F) ? (speed * 0.99F) : speed);
        mc.thePlayer.motionX = mc.thePlayer.motionZ = 0.0D;
    }
}
