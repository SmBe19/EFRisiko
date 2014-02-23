/**
 * Consts.java
 * (c) 2014 Benjamin Schmid
 * Created 13.02.2014
 * 
 * Contains all the important constants
 */

package efRisiko;

import java.awt.Color;

public class Consts {
	public static boolean ISDEBUG = true;
	
	public static int SCREENWIDTH = 400;
	public static int SCREENHEIGHT = 400;
	
	public static int PLAYERCOUNT = 4;
	
	// Interface-Settings
	
	// Titlebar
	public static String GAMENAME = "Risiko";
	public static float TITLEBARFONTSIZE = 32f;
	public static int TITLEBARHEIGHT = 40;
	public static Color TITLEBARBACKGROUND = Color.blue;
	public static Color TITLEBARFOREGROUND = Color.green;
	
	// Graph
	public static Color NEUTRALPLAYERCOLOR = Color.gray;
	public static Color[] PLAYERCOLORS = new Color[] {Color.red, Color.blue, Color.green, Color.orange, Color.cyan, Color.magenta};
	public static int MAXPLAYERCOUNT = PLAYERCOLORS.length;
	public static int VERTEXSIZE = 50;
	public static float VERTEXFONTSIZE = 20f;
	public static int VERTEXUNITSTRINGOFFSET = 17;
	
	// Info-Field
	public static int INFOFIELDX = 40;
	public static int INFOFIELDY = 40;
	public static int INFOFIELDWIDTH = 150;
	public static int INFOFIELDHEIGHT = 50;
	public static Color INFOFIELDBACKGROUND = Color.black;
	public static Color INFOFIELDFOREGROUND = Color.white;
	public static float INFOFIELDFONTSIZE = 12f;
}
