/**
 * Continent.java
 * (c) 2014 Benjamin Schmid
 * Created 19.02.2014
 * 
 * Represents a continent
 */

package efRisiko;

import java.util.ArrayList;

public class Continent {
	public int units;
	public int player;
	public ArrayList<Integer> regions;
	public String name;
	
	public Continent()
	{
		regions = new ArrayList<Integer>();
		player = -1;
		units = 0;
	}
}
