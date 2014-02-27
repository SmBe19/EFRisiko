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
	public static int DEBUGSTARTUNITS = 5;
	public static boolean AUTOPLACEUNITS = true;
	
	public static int SCREENWIDTH = 400;
	public static int SCREENHEIGHT = 400;
	
	public static int PLAYERCOUNT = 4;
	public static String MAPNAME = "Schweiz.txt";
	
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
	public static int INFOFIELDX = 60;
	public static int INFOFIELDY = 40;
	public static int INFOFIELDWIDTH = 200;
	public static int INFOFIELDHEIGHT = 65;
	public static Color INFOFIELDBACKGROUND = new Color(1f, 1f, 1f, 0.7f);
	public static float INFOFIELDFONTSIZE = 12f;
	public static int INFOFIELDMARGIN = 10;
	
	// ContinentInfo-Field
	public static int CONTINENTINFOFIELDX = 60;
	public static int CONTINENTINFOFIELDY = 40;
	public static int CONTINENTINFOFIELDWIDTH = 160;
	public static Color CONTINENTINFOFIELDBACKGROUND = new Color(0f, 0f, 0f, 0.7f);
	public static Color CONTINENTINFOFIELDFOREGROUND = Color.white;
	public static float CONTINENTINFOFIELDFONTSIZE = 12f;
	public static int CONTINENTINFOFIELDMARGIN = 10;
	
	// Count-Dialog
	public static int COUNTDIALOGX = 60;
	public static int COUNTDIALOGY = 200;
	public static int COUNTDIALOGWIDTH = 150;
	public static int COUNTDIALOGHEIGHT = 60;
	public static Color COUNTDIALOGBACKGROUND = new Color(0f, 0f, 0f, 0.7f);
	public static Color COUNTDIALOGFOREGROUND = Color.white;
	public static float COUNTDIALOGFONTSIZE = 30f;
	public static int COUNTDIALOGMARGIN = COUNTDIALOGWIDTH / 3;
}
