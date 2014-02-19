/**
 * GameCore.java
 * (c) 2014 Benjamin Schmid
 * Created 13.02.2014
 * 
 * Simulates the game
 */

package efRisiko;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class GameCore {
	
	public static ArrayList<Region> regions;
	public static ArrayList<Continent> continents;
	
	public static BufferedImage backgroundImage;
	public static Dimension fieldSize;
	
	public static boolean init()
	{
		regions = new ArrayList<Region>();
		continents = new ArrayList<Continent>();
		
		if(!loadMap("content/maps/map1.txt"))
			return false;
		
		return true;
	}
	
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
				}

				if(aSection.equals("[REGIONS]"))
				{
					System.out.println("Active Section: " + aSection);
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
					
					aSection = "";
				}
				else if(aSection.equals("[CONTINENTS]"))
				{
					System.out.println("Active Section: " + aSection);
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
					System.out.println("Active Section: " + aSection);
					aLine = getNextLine(reader);
					
					backgroundImage = ImageIO.read(new File(aLine));
					
					aSection = "";
				}
				else if(aSection.equals("[SIZE]"))
				{
					System.out.println("Active Section: " + aSection);
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
