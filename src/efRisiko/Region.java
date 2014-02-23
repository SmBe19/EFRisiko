/**
 * Region.java
 * (c) 2014 Benjamin Schmid
 * Created 19.02.2014
 * 
 * Represents a region
 */

package efRisiko;

import java.awt.Point;
import java.util.ArrayList;

public class Region {
	public ArrayList<Integer> connections;
	public Point location;
	public int units;
	public int player;
	
	public Region()
	{
		connections = new ArrayList<Integer>();
		location = new Point();
		units = 0;
		player = -1;
	}
}
