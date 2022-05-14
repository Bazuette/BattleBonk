package libayon.event.events.player;

import libayon.event.CancellableEvent;

public class MessageSentEvent extends CancellableEvent {

	private String message;

	public MessageSentEvent(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String setMessage(String message) {
		return this.message = message;
	}

}