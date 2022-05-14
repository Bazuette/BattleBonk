package org.newdawn.slick.util.pathfinding;

/**
 * The context describing the current path finding state
 * 
 * @author kevin
 */
public interface PathFindingContext {
	/**
	 * Get the object being moved along the path if any
	 * 
	 * @return The object being moved along the path
	 */
    Mover getMover();
	
	/**
	 * Get the x coordinate of the source location
	 * 
	 * @return The x coordinate of the source location
	 */
    int getSourceX();

	/**
	 * Get the y coordinate of the source location
	 * 
	 * @return The y coordinate of the source location
	 */
    int getSourceY();
	
	/**
	 * Get the distance that has been searched to reach this point
	 * 
	 * @return The distance that has been search to reach this point
	 */
    int getSearchDistance();
}
