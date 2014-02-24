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
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;

import efRisiko.Player.PlayerControlType;

public class GameCore {
	public enum GameState { REINFORCE, ATTACK, BACK, MOVE };
	
	public static ArrayList<Region> regions;
	public static ArrayList<Continent> continents;
	public static ArrayList<Player> players;
	
	public static BufferedImage backgroundImage;
	public static Dimension fieldSize;
	
	public static boolean isPreparation;
	public static int activePlayer;
	public static GameState activeState;
	
	public static int unitsLeft;
	
	public static int attackResA1, attackResA2, attackResA3, attackResD1, attackResD2;
	public static boolean attackSuccess;
	public static int attackSource, attackDrain;
	
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
		rnd = new Random(System.currentTimeMillis());
		
		if(!loadMap("content/maps/Risiko.txt"))
			return false;
		
		for(int i = 0; i < Consts.PLAYERCOUNT; i++)
		{
			Player p = new Player();
			p.controlType = PlayerControlType.LOCAL;
			players.add(p);
		}
		
		isPreparation = true;
		activePlayer = rnd.nextInt(Consts.PLAYERCOUNT);
		activeState = GameState.REINFORCE;
		
		unitsLeft = 50 - Consts.PLAYERCOUNT * 5;
		unitsLeft *= Consts.PLAYERCOUNT;
		
		return true;
	}
	
	/**
	 * Wechselt zur nächsten Phase für den aktuellen Spieler
	 */
	public static void nextPhase()
	{
		switch(activeState)
		{
		case REINFORCE:
			activeState = GameState.ATTACK;
			break;
		case ATTACK:
			activeState = GameState.MOVE;
			break;
		case BACK:
			activeState = GameState.ATTACK;
			break;
		case MOVE:
			nextPlayer();
			break;
		}
	}
	
	/**
	 * Wechselt zum nächsten Spieler
	 */
	public static void nextPlayer()
	{
		do
		{
			activePlayer++;
			activePlayer %= Consts.PLAYERCOUNT;
		} while(countPlayerRegions(activePlayer) == 0 && !isPreparation);
		
		activeState = GameState.REINFORCE;
		
		attackResA1 = attackResA2 = attackResA3 = attackResD1 = attackResD2 = 0;
		
		if(isPreparation)
		{
			if(unitsLeft <= 0)
			{
				isPreparation = false;
				activePlayer = rnd.nextInt(Consts.PLAYERCOUNT);
				nextPlayer();
			}
		}
		else
		{
			unitsLeft = Math.max(3, countPlayerRegions(activePlayer)/3);
			for(int i = 0; i < continents.size(); i++)
			{
				boolean pos = true;
				for(int j = 0; j < continents.get(i).regions.size(); j++)
				{
					if(regions.get(continents.get(i).regions.get(j)).player != activePlayer)
					{
						pos = false;
						break;
					}
				}
				if(pos)
					unitsLeft += continents.get(i).units;
			}
		}
	}
	
	/**
	 * Zählt, wie viele Regionen ein Spieler hat
	 * @param player der Spieler
	 * @return Anzahl Gebiete
	 */
	public static int countPlayerRegions(int player)
	{
		int res = 0;
		for(int i = 0; i < regions.size(); i++)
			if(regions.get(i).player == player)
				res++;
		return res;
	}
	
	/**
	 * Überprüft, ob alle Regionen einem Spieler gehören
	 * @return Das Ergebnis der Überprüfung
	 */
	public static boolean allRegionsOccupied()
	{
		for(int i = 0; i < regions.size(); i++)
			if(regions.get(i).player < 0)
				return false;
		return true;
	}
	
	/**
	 * Platziert eine Einheit in einer Regioin
	 * @param region Die zu besetzende Region
	 * @return ob die Aktion erfolgreich war
	 */
	public static boolean placeUnit(int region)
	{
		if(!isPreparation && activeState != GameState.REINFORCE)
			return false;
		
		if(regions.get(region).player >= 0 && regions.get(region).player != activePlayer)
			return false;
		
		if(isPreparation && regions.get(region).player >= 0 && !allRegionsOccupied())
			return false;
		
		regions.get(region).player = activePlayer;
		regions.get(region).units++;
		
		unitsLeft--;
		
		if(isPreparation)
			nextPlayer();
		else if(unitsLeft <= 0)
			nextPhase();
		
		return true;
	}
	
	/**
	 * Verstärkt die soeben eroberte Region
	 * @param count Anzahl Einheiten
	 * @return ob die Aktion erfolgreich war
	 */
	public static boolean backRegion(int count)
	{
		if(activeState != GameState.BACK)
			return false;
		
		if(count < 0)
			return false;
		
		if(regions.get(attackSource).units <= count)
			return false;
		
		regions.get(attackSource).units -= count;
		regions.get(attackDrain).units += count;
		
		nextPhase();
		
		return true;
	}
	
	/**
	 * Greift eine Region an, jeweils mit maximaler Anzahl Einheiten
	 * @param source Von wo aus angegriffen wird
	 * @param drain Wo angegriffen wird
	 * @param count Anzahl Einheiten, die eingesetzt werden sollen
	 * @return ob die Aktion erfolgreich war (d.h. kein Fehler auftrat, sagt nichts über den Ausgang des Angriffes aus)
	 */
	public static boolean attack(int source, int drain)
	{
		if(activeState != GameState.ATTACK)
			return false;
		
		if(regions.get(source).player != activePlayer || regions.get(drain).player == activePlayer)
			return false;
		
		if(regions.get(source).units < 2)
			return false;
		
		if(!regions.get(source).connections.contains(drain))
			return false;
		
		attackResA1 = attackResA2 = attackResA3 = attackResD1 = attackResD2 = 0;
		attackSource = source;
		attackDrain = drain;
		attackSuccess = false;
		
		attackResA1 = rnd.nextInt(6) + 1;
		if(regions.get(source).units > 2)
			attackResA2 = rnd.nextInt(6) + 1;
		if(regions.get(source).units > 3)
			attackResA3 = rnd.nextInt(6) + 1;
		attackResD1 = rnd.nextInt(6) + 1;
		if(regions.get(drain).units > 1)
			attackResD2 = rnd.nextInt(6) + 1;
		
		int tmp;
		if(attackResA1 < attackResA2)
		{
			tmp = attackResA1;
			attackResA1 = attackResA2;
			attackResA2 = tmp;
		}
		if(attackResA2 < attackResA3)
		{
			tmp = attackResA2;
			attackResA2 = attackResA3;
			attackResA3 = tmp;
		}
		if(attackResA1 < attackResA2)
		{
			tmp = attackResA1;
			attackResA1 = attackResA2;
			attackResA2 = tmp;
		}
		if(attackResD1 < attackResD2)
		{
			tmp = attackResD1;
			attackResD1 = attackResD2;
			attackResD2 = tmp;
		}
		
		if(attackResA1 > attackResD1)
			regions.get(drain).units--;
		else
			regions.get(source).units--;
		
		if(regions.get(drain).units <= 0)
		{
			regions.get(drain).player = activePlayer;
			regions.get(drain).units = 1;
			regions.get(source).units--;
			attackSuccess = true;
			activeState = GameState.BACK;
			return true;
		}
		
		if(attackResA2 == 0 || attackResD2 == 0)
			return true;
		
		if(attackResA2 > attackResD2)
			regions.get(drain).units--;
		else
			regions.get(source).units--;
		
		if(regions.get(drain).units <= 0)
		{
			regions.get(drain).player = activePlayer;
			regions.get(drain).units = 1;
			regions.get(source).units--;
			attackSuccess = true;
			activeState = GameState.BACK;
		}
		
		return true;
	}
	
	/**
	 * Verschiebt Einheiten
	 * @param source Von wo verschoben wird
	 * @param drain Wohin verschoben wird
	 * @param count Anzahl Einheiten
	 * @return ob die Aktion erfolgreich war
	 */
	public static boolean move(int source, int drain, int count)
	{
		if(activeState != GameState.MOVE)
			return false;
		
		if(regions.get(source).player != activePlayer || regions.get(drain).player != activePlayer)
			return false;
		
		if(count <= 0)
			return false;
		
		if(regions.get(source).units <= count)
			return false;
		
		LinkedList<Integer> q = new LinkedList<Integer>();
		ArrayList<Integer> visited = new ArrayList<Integer>();
		q.add(source);
		
		while(!q.isEmpty())
		{
			int aEl = q.getFirst();
			q.remove();
			if(visited.contains(aEl))
				continue;
			visited.add(aEl);
			for(int i = 0; i < regions.get(aEl).connections.size(); i++)
				if(!visited.contains(regions.get(aEl).connections.get(i)) && regions.get(regions.get(aEl).connections.get(i)).player == activePlayer)
					q.add(regions.get(aEl).connections.get(i));
		}
		
		if(!visited.contains(drain))
			return false;
		
		regions.get(source).units -= count;
		regions.get(drain).units += count;
		
		nextPhase();
		
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

					if(!(new File(aLine)).exists() || (new File(aLine)).isDirectory())
					{
						System.out.println("File " + aLine + " not found!");
						return false;
					}
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
