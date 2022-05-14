package libayon.event.events.client;

import libayon.event.CancellableEvent;

public class MouseScrollEvent extends CancellableEvent {

	private final int button;

	/**
	 * @formatter:off
	 * Directions:
	 * 0 = LEFT CLICK
	 * 1 = RIGHT CLICK
	 * 2 = WHEEL CLICK
	 */
	public MouseScrollEvent(int button) {
		this.button = button;
	}
	
	public int getDirection() {
		return button;
	}
	
}