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
	public static boolean ISDEBUG = false;
	
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
	public static int NEXTPHASEBUTTONOFFSET = 180;
	
	// Graph
	public static Color NEUTRALPLAYERCOLOR = Color.gray;
	public static Color[] PLAYERCOLORS = new Color[] {Color.red, Color.blue, Color.green, Color.orange, Color.cyan, Color.magenta};
	public static int MAXPLAYERCOUNT = PLAYERCOLORS.length;
	public static int VERTEXSIZE = 50;
	public static float VERTEXFONTSIZE = 20f;
	public static int VERTEXUNITSTRINGOFFSET = 17;
	public static Color ACTIVEVERTEXCOLOR = Color.yellow;
	
	// Info-Field
	public static int INFOFIELDX = 40;
	public static int INFOFIELDY = 40;
	public static int INFOFIELDWIDTH = 200;
	public static int INFOFIELDHEIGHT = 65;
	public static Color INFOFIELDBACKGROUND = Color.black;
	public static float INFOFIELDFONTSIZE = 12f;
	public static int INFOFIELDMARGIN = 10;
	
	// ContinentInfo-Field
	public static int CONTINENTINFOFIELDX = 40;
	public static int CONTINENTINFOFIELDY = 40;
	public static int CONTINENTINFOFIELDWIDTH = 130;
	public static Color CONTINENTINFOFIELDBACKGROUND = Color.black;
	public static Color CONTINENTINFOFIELDFOREGROUND = Color.white;
	public static float CONTINENTINFOFIELDFONTSIZE = 12f;
	public static int CONTINENTINFOFIELDMARGIN = 10;
	
	// Count-Dialog
	public static int COUNTDIALOGX = 40;
	public static int COUNTDIALOGY = 200;
	public static int COUNTDIALOGWIDTH = 150;
	public static int COUNTDIALOGHEIGHT = 60;
	public static Color COUNTDIALOGBACKGROUND = Color.black;
	public static Color COUNTDIALOGFOREGROUND = Color.white;
	public static float COUNTDIALOGFONTSIZE = 30f;
	public static int COUNTDIALOGMARGIN = COUNTDIALOGWIDTH / 3;
}
