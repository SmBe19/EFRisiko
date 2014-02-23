/**
 * GameCore.java
 * (c) 2014 Benjamin Schmid
 * Created 13.02.2014
 * 
 * Simulates the game
 */

package efRisiko;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import efRisiko.Player.PlayerControlType;

public class GameCore {
	public enum GameState { BACKING, ATTACK, MOVE };
	
	public static ArrayList<Region> regions;
	public static ArrayList<Continent> continents;
	public static ArrayList<Player> players;
	
	public static BufferedImage backgroundImage;
	public static Dimension fieldSize;
	
	public static boolean isPreparation;
	public static int activePlayer;
	public static GameState activeState;
	
	public static int unitsPerPlayerLeft;
	
	public static Random rnd;
	
	/**
	 * Initialisiert den Gamecore
	 * @return ob die Initialisierung erfolgreich war
	 */
	public static boolean init()
	{
		regions = new ArrayList<Region>();
		continents = new ArrayList<Continent>();
		players = new ArrayList<Player>();
		rnd = new Random();
		
		if(!loadMap("content/maps/map1.txt"))
			return false;
		
		for(int i = 0; i < Consts.PLAYERCOUNT; i++)
		{
			Player p = new Player();
			p.controlType = PlayerControlType.LOCAL;
			players.add(p);
		}
		
		isPreparation = true;
		activePlayer = rnd.nextInt(Consts.PLAYERCOUNT);
		activeState = GameState.BACKING;
		
		unitsPerPlayerLeft = 50 - Consts.PLAYERCOUNT * 5;
		
		return true;
	}
	
	/**
	 * Wechselt zur nächsten Phase für den aktuellen Spieler
	 */
	public static void nextPhase()
	{
		
	}
	
	/**
	 * Wechselt zum nächsten Spieler
	 */
	public static void nextPlayer()
	{
		
	}
	
	/**
	 * Setzt Verstärkung in einer Region
	 * @param region die zu verstärkende Region
	 * @param count Anzahl Einheiten
	 * @return ob die Aktion erfolgreich war
	 */
	public static boolean back(int region, int count)
	{
		return true;
	}
	
	/**
	 * Greift eine Region an
	 * @param source Von wo aus angegriffen wird
	 * @param drain Wo angegriffen wird
	 * @return ob die Aktion erfolgreich war (d.h. kein Fehler auftrat, sagt nichts über den Ausgang des Angriffes aus)
	 */
	public static boolean attack(int source, int drain)
	{
		return true;
	}
	
	/**
	 * Verschiebt Einheiten
	 * @param source Von wo verschoben wird
	 * @param drain Wohin verschoben wird
	 * @return ob die Aktion erfolgreich war
	 */
	public static boolean move(int source, int drain)
	{
		return true;
	}
	
	/**
	 * Lädt eine Karte aus einer Datei
	 * @param name Name der Datei
	 * @return Ob das Laden erfolgreich war
	 */
	public static boolean loadMap(String name)
	{
		System.out.println("Load " + name);
		
		File file = new File(name);
		if(!file.exists() || file.isDirectory())
		{
			System.out.println("File " + name + " not found!");
			return false;
		}
		
		try
		{
			DataInputStream instr = new DataInputStream(new FileInputStream(file));
			BufferedReader reader = new BufferedReader(new InputStreamReader(instr));

			regions = new ArrayList<Region>();
			continents = new ArrayList<Continent>();
			
			String aSection = "";
			String aLine = "";
			while((aLine = getNextLine(reader)) != null)
			{
				if(aLine.startsWith("[") && aLine.endsWith("]"))
				{
					aSection = aLine;
					if(Consts.ISDEBUG)
						System.out.println("Active Section: " + aSection);
				}

				if(aSection.equals("[REGIONS]"))
				{
					aLine = getNextLine(reader);
					int n, m;
					n = Integer.parseInt(aLine.split(" ")[0]);
					m = Integer.parseInt(aLine.split(" ")[1]);
					
					for(int i = 0; i < n; i++)
						regions.add(new Region());
					
					for(int i = 0; i < m; i++)
					{
						aLine = getNextLine(reader);
						int a, b;
						a = Integer.parseInt(aLine.split(" ")[0]);
						b = Integer.parseInt(aLine.split(" ")[1]);
						regions.get(a).connections.add(b);
						regions.get(b).connections.add(a);
					}
					for(int i = 0; i < n; i++)
					{
						aLine = getNextLine(reader);
						int a, b;
						a = Integer.parseInt(aLine.split(" ")[0]);
						b = Integer.parseInt(aLine.split(" ")[1]);
						regions.get(i).location = new Point(a, b);
					}
					
					aSection = "";
				}
				else if(aSection.equals("[CONTINENTS]"))
				{
					aLine = getNextLine(reader);
					int n;
					n = Integer.parseInt(aLine);
					
					for(int i = 0; i < n; i++)
						continents.add(new Continent());
					
					for(int i = 0; i < n; i++)
					{
						aLine = getNextLine(reader);
						continents.get(i).name = aLine;
						
						aLine = getNextLine(reader);
						continents.get(i).units = Integer.parseInt(aLine);
						
						aLine = getNextLine(reader);
						int m = Integer.parseInt(aLine);
						aLine = getNextLine(reader);
						String[] aLineS = aLine.split(" ");
						for(int j = 0; j < m; j++)
						{
							continents.get(i).regions.add(Integer.parseInt(aLineS[j]));
						}
					}
					
					aSection = "";
				}
				else if(aSection.equals("[GRAPHIC]"))
				{
					aLine = getNextLine(reader);
					
					backgroundImage = ImageIO.read(new File(aLine));
					
					aSection = "";
				}
				else if(aSection.equals("[SIZE]"))
				{
					aLine = getNextLine(reader);
					
					fieldSize = new Dimension(Integer.parseInt(aLine.split(" ")[0]), Integer.parseInt(aLine.split(" ")[1]));
				}
			}
			
			reader.close();
			instr.close();
		}
		catch(IOException e)
		{
			System.err.println("Error: " + e.getMessage());
			return false;
		}
		catch(NumberFormatException e)
		{
			System.err.println("Error: " + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	/**
	 * Liest die nächste Nichtkommentarzeile aus einem BufferedReader
	 * @param reader der BufferedReader, aus dem gelesen werden soll
	 * @return die nächste Zeile
	 * @throws IOException
	 */
	static String getNextLine(BufferedReader reader) throws IOException
	{
		String res = null;
		
		while((res = reader.readLine()) != null)
		{
			res = res.trim();
			if(res.length() > 0 && !res.startsWith(";"))
				break;
		}

		return res;
	}

}
