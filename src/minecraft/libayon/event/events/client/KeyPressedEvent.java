package libayon.event.events.client;

import libayon.event.CancellableEvent;

public class KeyPressedEvent extends CancellableEvent {

	private final int key;

	public KeyPressedEvent(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}

}