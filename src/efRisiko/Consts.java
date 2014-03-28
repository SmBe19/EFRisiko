/**
 * Consts.java
 * (c) 2014 Benjamin Schmid
 * Created 13.02.2014
 * 
 * Contains all the important constants
 */

package efRisiko;

import java.awt.Color;
import java.awt.event.KeyEvent;

public class Consts {
	public static boolean ISDEBUG = false;
	public static boolean SHOWSTARTUP = false;
	public static int DEBUGSTARTUNITS = 5;
	public static boolean AUTOPLACEUNITS = true;
	
	public static int SCREENWIDTH = 800;
	public static int SCREENHEIGHT = 800;
	
	public static int PLAYERCOUNT = 2;
	public static String MAPNAME = "Risiko.txt";
	
	public static String CONTENTFOLDER = "content/";
	public static String MAPSFOLDER = "maps/";
	
	// Interface-Settings
	
	public static final int EXITGAMEKEY = KeyEvent.VK_ESCAPE;
	
	// Titlebar
	public static String GAMENAME = "Risiko";
	public static float TITLEBARFONTSIZE = 32f;
	public static int TITLEBARHEIGHT = 40;
	public static Color TITLEBARBACKGROUND = Color.blue;
	public static Color TITLEBARFOREGROUND = Color.green;
	public static int NEXTPHASEBUTTONOFFSET = 180;
	
	public static final int NEXTPHASEKEY = KeyEvent.VK_F5;
	
	// Graph
	public static Color NEUTRALPLAYERCOLOR = Color.gray;
	public static Color[] PLAYERCOLORS = new Color[] {Color.red, Color.blue, Color.green, Color.orange, Color.cyan, Color.magenta, Color.pink, new Color(128, 125, 255)};
	public static int MAXPLAYERCOUNT = PLAYERCOLORS.length;
	public static int VERTEXSIZE = 50;
	public static float VERTEXFONTSIZE = 20f;
	public static int VERTEXUNITSTRINGOFFSET = 17;
	public static Color VERTEXBACKGROUND = new Color(1f, 1f, 1f, 0.9f);
	public static Color ACTIVEVERTEXCOLOR = Color.yellow;
	public static Color ACTIVEVERTEXBACKGROUND = new Color(0f, 0f, 0f, 0.7f);
	
	// Info-Field
	public static boolean INFOFIELDTITLEBAR = true;
	public static int INFOFIELDTBX = 200;
	public static int INFOFIELDTBWIDTH = 620;
	public static Color INFOFIELDTBBACKGROUND = new Color(1f, 1f, 1f, 1f);
	public static float INFOFIELDTBFONTSIZE = 20f;
	public static int INFOFIELDX = 60;
	public static int INFOFIELDY = 40;
	public static int INFOFIELDWIDTH = 220;
	public static Color INFOFIELDBACKGROUND = new Color(1f, 1f, 1f, 0.9f);
	public static float INFOFIELDFONTSIZE = 12f;
	public static int INFOFIELDMARGIN = 10;
	public static final int INFOFIELDCHANGEKEY = KeyEvent.VK_F11;
	
	// Help-Field
	public static int HELPFIELDX = 50;
	public static int HELPFIELDY = 40;
	public static int HELPFIELDWIDTH = 200;
	public static Color HELPFIELDBACKGROUND = new Color(0f, 0f, 0f, 0.7f);
	public static Color HELPFIELDFOREGROUND = Color.white;
	public static float HELPFIELDFONTSIZE = 12f;
	public static int HELPFIELDMARGIN = 10;
	public static boolean HELPFIELDVISIBLE = false;
	public static final int HELPFIELDVISIBLEKEY = KeyEvent.VK_F1;
	public static String HELPFIELDTEXTFILE = "IngameHelp.txt";
	public static String HELPFIELDTEXT = "EFRisiko Hilfe";
	
	// ContinentInfo-Field
	public static int CONTINENTINFOFIELDX = 60;
	public static int CONTINENTINFOFIELDY = 40;
	public static int CONTINENTINFOFIELDWIDTH = 160;
	public static Color CONTINENTINFOFIELDBACKGROUND = new Color(0f, 0f, 0f, 0.7f);
	public static Color CONTINENTINFOFIELDFOREGROUND = Color.white;
	public static float CONTINENTINFOFIELDFONTSIZE = 12f;
	public static int CONTINENTINFOFIELDMARGIN = 10;
	public static boolean CONTINENTINFOFIELDVISIBLE = true;
	public static final int CONTINENTINFOFIELDVISIBLEKEY = KeyEvent.VK_F9;
	
	// Count-Dialog
	public static int COUNTDIALOGX = 60;
	public static int COUNTDIALOGY = 200;
	public static int COUNTDIALOGWIDTH = 150;
	public static int COUNTDIALOGHEIGHT = 60;
	public static Color COUNTDIALOGBACKGROUND = new Color(0f, 0f, 0f, 0.7f);
	public static Color COUNTDIALOGFOREGROUND = Color.white;
	public static float COUNTDIALOGFONTSIZE = 30f;
	public static int COUNTDIALOGMARGIN = COUNTDIALOGWIDTH / 3;
	public static boolean COUNTDIALOGVISIBLE = false;
}
