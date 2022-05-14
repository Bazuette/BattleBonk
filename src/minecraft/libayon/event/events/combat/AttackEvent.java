package libayon.event.events.combat;

import libayon.event.CancellableEvent;
import net.minecraft.entity.Entity;

public class AttackEvent extends CancellableEvent {
	public Entity target;
	
	public AttackEvent(Entity target) {
		this.target = target;
	}
	
	public Entity getTarget() {
		return target;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}
	


}