package org.newdawn.slick.geom;

import java.io.Serializable;

/**
 * A collection of triangles
 *
 * @author kevin
 */
public interface Triangulator extends Serializable {

	/**
	 * Get a count of the number of triangles produced
	 * 
	 * @return The number of triangles produced
	 */
    int getTriangleCount();
	
	/**
	 * Get a point on a specified generated triangle
	 * 
	 * @param tri The index of the triangle to interegate
	 * @param i The index of the point within the triangle to retrieve
	 * (0 - 2)
	 * @return The x,y coordinate pair for the point
	 */
    float[] getTrianglePoint(int tri, int i);
	
	/**
	 * Add a point that forms part of the outer polygon
	 * 
	 * @param x The x coordinate of the point
	 * @param y The y coordiante of the point
	 */
    void addPolyPoint(float x, float y);
	
	/**
	 * Start a hole in the polygon
	 */
    void startHole();
	
	/**
	 * Run the triangulation
	 * 
	 * @return True if successful
	 */
    boolean triangulate();
}
