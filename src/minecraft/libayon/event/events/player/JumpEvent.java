package libayon.event.events.player;

import libayon.event.CancellableEvent;

public class JumpEvent extends CancellableEvent {
	public float jumpMotion;
	public JumpEvent(float jumpMotion) {
		this.jumpMotion = jumpMotion;
	}
	public double getJumpMotion() {
		return jumpMotion;
	}
	public void setJumpMotion(float jumpMotion) {
		this.jumpMotion = jumpMotion;
	}
	public float getJumpUpwardsMotion() {
		return 0.42F;
	}


}