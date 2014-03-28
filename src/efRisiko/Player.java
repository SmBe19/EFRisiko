/**
 * Player.java
 * (c) 2014 Benjamin Schmid
 * Created 24.02.2014
 * 
 * Represents a player
 */


package efRisiko;

public class Player {
	public enum PlayerControlType { LOCAL, /*REMOTE,*/ AI };
	public PlayerControlType controlType;
	public String connectionString;
	
	public AI ai;
}
