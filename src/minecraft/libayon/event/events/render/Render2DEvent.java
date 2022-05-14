package libayon.event.events.render;

import libayon.event.Event;

public class Render2DEvent extends Event {

	private final int width;
	private final int height;

	public Render2DEvent(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}