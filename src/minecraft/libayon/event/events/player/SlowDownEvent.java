package libayon.event.events.player;

import libayon.event.CancellableEvent;

public class SlowDownEvent extends CancellableEvent {

	private float slowDownMultiplier;

	public SlowDownEvent(float slowDownMultiplier) {
		this.slowDownMultiplier = slowDownMultiplier;
	}

	public float getSlowDownMultiplier() {
		return slowDownMultiplier;
	}

	public void setSlowDownMultiplier(float slowDownMultiplier) {
		this.slowDownMultiplier = slowDownMultiplier;
	}

}