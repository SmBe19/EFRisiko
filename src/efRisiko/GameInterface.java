/**
 * GameInterface.java
 * (c) 2014 Benjamin Schmid
 * Created 13.02.2014
 * 
 * Displays the Simulation and interacts with the user
 */

package efRisiko;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import efRisiko.GameCore.GameState;


public class GameInterface extends JPanel {
	
	JFrame frame;
	
	int activeRegion;
	
	int countDialogValue;
	int countDialogAMax;
	
	/**
	 * Initialisiert das GameInterface
	 */
	public void init()
	{
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(Consts.COUNTDIALOGVISIBLE && arg0.getX() > Consts.COUNTDIALOGX && arg0.getX() < Consts.COUNTDIALOGX + Consts.COUNTDIALOGWIDTH
						&& arg0.getY() > Consts.COUNTDIALOGY + Consts.TITLEBARHEIGHT && arg0.getY() < Consts.COUNTDIALOGY + Consts.COUNTDIALOGHEIGHT + Consts.TITLEBARHEIGHT)
				{
					if(arg0.getX() < Consts.COUNTDIALOGX + Consts.COUNTDIALOGWIDTH / 2)
					{
						if(arg0.getButton() == MouseEvent.BUTTON1)
							countDialogValue--;
						else if (arg0.getButton() == MouseEvent.BUTTON3)
							countDialogValue -= 5;
						else if(arg0.getButton() == MouseEvent.BUTTON2)
							countDialogValue -= 20;
					}
					else
					{
						if(arg0.getButton() == MouseEvent.BUTTON1)
							countDialogValue++;
						else if (arg0.getButton() == MouseEvent.BUTTON3)
							countDialogValue += 5;
						else if(arg0.getButton() == MouseEvent.BUTTON2)
							countDialogValue += 20;
					}
					if(countDialogValue < 0)
						countDialogValue = 0;
					if(countDialogValue > countDialogAMax)
						countDialogValue = countDialogAMax;
				}
				else if(arg0.getX() > Consts.SCREENWIDTH - Consts.NEXTPHASEBUTTONOFFSET && arg0.getX() < Consts.SCREENWIDTH
						&& arg0.getY() > 0 && arg0.getY() < Consts.TITLEBARHEIGHT)
				{
					GameCore.nextPhase();
				}
				else
				{
					int reg = getRegion(arg0.getX(), arg0.getY() - Consts.TITLEBARHEIGHT);
					if(GameCore.isPreparation)
					{
						if(reg >= 0)
							GameCore.placeUnits(reg, 1);
					}
					else if (reg >= 0)
					{
						switch (GameCore.activeState) {
						case REINFORCE:
							if(activeRegion < 0)
							{
								if(GameCore.regions.get(reg).player == GameCore.activePlayer || GameCore.regions.get(reg).player < 0)
								{
									activeRegion = reg;
									Consts.COUNTDIALOGVISIBLE = true;
									countDialogValue = 1;
									countDialogAMax = GameCore.unitsLeft;
								}
							}
							else if(activeRegion == reg)
							{
								GameCore.placeUnits(reg, countDialogValue);
								Consts.COUNTDIALOGVISIBLE = false;
								activeRegion = -1;
							}
							else
							{
								if(GameCore.regions.get(reg).player == GameCore.activePlayer || GameCore.regions.get(reg).player < 0)
								{
									activeRegion = reg;
								}
							}
							break;
						case ATTACK:
							if(activeRegion < 0)
							{
								if(GameCore.regions.get(reg).player == GameCore.activePlayer)
									activeRegion = reg;
							}
							else
							{
								if(reg >= 0 && GameCore.regions.get(reg).player != GameCore.activePlayer && arg0.getButton() == MouseEvent.BUTTON3)
									GameCore.attack(activeRegion, reg);
								else
									activeRegion = -1;
							}
							if(GameCore.activeState == GameState.BACK)
							{
								countDialogValue = 0;
								countDialogAMax = GameCore.regions.get(activeRegion).units - 1;
								Consts.COUNTDIALOGVISIBLE = true;
								
								activeRegion = reg;
							}
							break;
						case BACK:
							if(reg == GameCore.attackDrain && arg0.getButton() == MouseEvent.BUTTON1)
							{
								GameCore.backRegion(countDialogValue);
								Consts.COUNTDIALOGVISIBLE = false;
							}
							break;
						case MOVE:
							if(activeRegion < 0)
							{
								if(GameCore.regions.get(reg).player == GameCore.activePlayer)
								{
									activeRegion = reg;

									countDialogValue = 0;
									countDialogAMax = GameCore.regions.get(activeRegion).units - 1;
									Consts.COUNTDIALOGVISIBLE = true;
								}
							}
							else
							{
								if(reg >= 0 && GameCore.regions.get(reg).player == GameCore.activePlayer)
									GameCore.move(activeRegion, reg, countDialogValue);
								activeRegion = -1;
								Consts.COUNTDIALOGVISIBLE = false;
							}
							break;
						}
					}
				}
				repaint();
			}
		});
	
		this.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {}
			
			@Override
			public void keyReleased(KeyEvent arg0) {}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				switch(arg0.getKeyCode())
				{
				case Consts.HELPFIELDVISIBLEKEY:
					Consts.HELPFIELDVISIBLE = !Consts.HELPFIELDVISIBLE;
					break;
				case Consts.NEXTPHASEKEY:
					GameCore.nextPhase();
					break;
				case Consts.CONTINENTINFOFIELDVISIBLEKEY:
					Consts.CONTINENTINFOFIELDVISIBLE = !Consts.CONTINENTINFOFIELDVISIBLE;
					break;
				case Consts.INFOFIELDCHANGEKEY:
					Consts.INFOFIELDTITLEBAR = !Consts.INFOFIELDTITLEBAR;
					break;
				case Consts.EXITGAMEKEY:
					frame.dispose();
				}
				
				repaint();
			}
		});
	}
	
	/**
	 * Zeigt das Fenster an
	 */
	public void show()
	{
		setFocusable(true);
		frame = new JFrame(Consts.GAMENAME);
		frame.add(this);
		frame.setSize(Consts.SCREENWIDTH, Consts.SCREENHEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {}
			
			@Override
			public void windowIconified(WindowEvent arg0) {}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				GameCore.desinit();
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {}
			
			@Override
			public void windowActivated(WindowEvent arg0) {}
		});
		requestFocusInWindow();
		
		readHelpFile();
		
		countDialogValue = 0;
		
		activeRegion = -1;
	}
	
	void readHelpFile()
	{
		File f = new File(Consts.CONTENTFOLDER + Consts.HELPFIELDTEXTFILE);
		if(f.exists() && !f.isDirectory())
		{
			FileInputStream fis;
			try {
				fis = new FileInputStream(f);
			    byte[] data = new byte[(int)f.length()];
			    fis.read(data);
			    fis.close();
			    Consts.HELPFIELDTEXT = new String(data, "UTF-8");
			} catch (FileNotFoundException e) {
				System.err.println("Error: " + e.getMessage());
			} catch (IOException e){
				System.err.println("Error: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Sucht die zu einer Position gehörenden Region
	 * @param x X-Koordinate
	 * @param y Y-Koordinate
	 * @return die Region oder -1, falls keine gefunden wird
	 */
	int getRegion(int x, int y)
	{
		float scaleX = (float)Consts.SCREENWIDTH / GameCore.fieldSize.width;
		float scaleY = (float)(Consts.SCREENHEIGHT - Consts.TITLEBARHEIGHT) / GameCore.fieldSize.height;
		for(int i = 0; i < GameCore.regions.size(); i++)
			if(Consts.VERTEXSIZE*Consts.VERTEXSIZE/4 >= (GameCore.regions.get(i).location.x * scaleX - x) * (GameCore.regions.get(i).location.x * scaleX - x) + (GameCore.regions.get(i).location.y * scaleY - y) * (GameCore.regions.get(i).location.y * scaleY - y))
				return i;
		return -1;
	}
	
	/**
	 * Zeichnet das Fenster neu
	 */
	@Override
	public void paintComponent(Graphics gr)
	{
		Consts.SCREENWIDTH = getSize().width;
		Consts.SCREENHEIGHT = getSize().height;
		
		float scaleX = (float)Consts.SCREENWIDTH / GameCore.fieldSize.width;
		float scaleY = (float)(Consts.SCREENHEIGHT - Consts.TITLEBARHEIGHT) / GameCore.fieldSize.height;
		
		super.paintComponent(gr);
		
		Graphics2D g = (Graphics2D)gr;

		g.setBackground(Color.black);
		g.clearRect(0, 0, Consts.SCREENWIDTH, Consts.SCREENHEIGHT);
		
		// Titlebar
		g.setColor(Consts.TITLEBARBACKGROUND);
		g.fillRect(0, 0, Consts.SCREENWIDTH, Consts.TITLEBARHEIGHT);
		g.setColor(Consts.TITLEBARFOREGROUND);
		g.setFont(g.getFont().deriveFont(Consts.TITLEBARFONTSIZE));
		g.drawString(Consts.GAMENAME, 10, Consts.TITLEBARHEIGHT - 10);
		if(!GameCore.isPreparation && GameCore.activeState != GameState.REINFORCE)
			g.drawString("Next phase", Consts.SCREENWIDTH - Consts.NEXTPHASEBUTTONOFFSET, Consts.TITLEBARHEIGHT - 10);
		
		// Content
		
		// Background
		g.fillRect(0, Consts.TITLEBARHEIGHT, Consts.SCREENWIDTH, Consts.SCREENHEIGHT - Consts.TITLEBARHEIGHT);
		g.drawImage(GameCore.backgroundImage, 0, Consts.TITLEBARHEIGHT, Consts.SCREENWIDTH, Consts.SCREENHEIGHT - Consts.TITLEBARHEIGHT, this);
		
		// Graph
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(5));
		if(Consts.ISDEBUG)
		{
			for(int i = 0; i < GameCore.regions.size(); i++)
				for(int j = 0; j < GameCore.regions.get(i).connections.size(); j++)
					g.drawLine((int)(GameCore.regions.get(i).location.x * scaleX), (int)(GameCore.regions.get(i).location.y * scaleY + Consts.TITLEBARHEIGHT), (int)(GameCore.regions.get(GameCore.regions.get(i).connections.get(j)).location.x * scaleX), (int)(GameCore.regions.get(GameCore.regions.get(i).connections.get(j)).location.y * scaleY + Consts.TITLEBARHEIGHT));
		}
		
		g.setFont(g.getFont().deriveFont(Consts.VERTEXFONTSIZE));
		
		for(int i = 0; i < GameCore.regions.size(); i++)
		{
			g.setColor(Consts.VERTEXBACKGROUND);
			if(activeRegion == i)
				g.setColor(Consts.ACTIVEVERTEXBACKGROUND);
			g.fillOval((int)(GameCore.regions.get(i).location.x * scaleX - Consts.VERTEXSIZE / 2), (int)(GameCore.regions.get(i).location.y * scaleY + Consts.TITLEBARHEIGHT - Consts.VERTEXSIZE / 2), Consts.VERTEXSIZE, Consts.VERTEXSIZE);
			if(GameCore.regions.get(i).player >= 0)
				g.setColor(Consts.PLAYERCOLORS[GameCore.regions.get(i).player % Consts.MAXPLAYERCOUNT]);
			else
				g.setColor(Consts.NEUTRALPLAYERCOLOR);
			if(activeRegion == i)
				g.setColor(Consts.ACTIVEVERTEXCOLOR);
			g.drawOval((int)(GameCore.regions.get(i).location.x * scaleX - Consts.VERTEXSIZE / 2), (int)(GameCore.regions.get(i).location.y * scaleY + Consts.TITLEBARHEIGHT - Consts.VERTEXSIZE / 2), Consts.VERTEXSIZE, Consts.VERTEXSIZE);

			g.drawString(String.format("%3d", GameCore.regions.get(i).units), (int)(GameCore.regions.get(i).location.x * scaleX - Consts.VERTEXUNITSTRINGOFFSET), (int)(GameCore.regions.get(i).location.y * scaleY + Consts.TITLEBARHEIGHT + Consts.VERTEXFONTSIZE / 2 - 2));
		}
		
		// InfoField
		String splitter = Consts.INFOFIELDTITLEBAR ? "; " : "\n";
		StringBuilder text = new StringBuilder();
		text.append("Player: " + (GameCore.activePlayer+1));
		text.append(splitter + "Phase: ");
		if(GameCore.isPreparation)
			text.append("PREP");
		else
			text.append(GameCore.activeState);
		
		if(GameCore.isPreparation || GameCore.activeState == GameState.REINFORCE)
			text.append(splitter + "Units left: " + (int)Math.ceil(GameCore.unitsLeft * (GameCore.isPreparation ? 1f/Consts.PLAYERCOUNT : 1)));
		else if((GameCore.activeState == GameState.ATTACK || GameCore.activeState == GameState.BACK) && GameCore.attackResA1 > 0)
			text.append(splitter + "Result: " + GameCore.attackResA1 + (GameCore.attackResA1 > GameCore.attackResD1 ? ">" : "<=") + GameCore.attackResD1 +
					(GameCore.attackResA2 > 0 && GameCore.attackResD2 > 0 ? (" & " + GameCore.attackResA2 + (GameCore.attackResA2 > GameCore.attackResD2 ? ">" : "<=") + GameCore.attackResD2) : "") +
					"; A: " + (GameCore.attackResA1 > 0 ? GameCore.attackResA1 : "") + (GameCore.attackResA2 > 0 ? ", " + GameCore.attackResA2 : "")  + (GameCore.attackResA3 > 0 ? ", " + GameCore.attackResA3 : "") +
					"; D: " + (GameCore.attackResD1 > 0 ? GameCore.attackResD1 : "")  + (GameCore.attackResD2 > 0 ? ", " + GameCore.attackResD2 : ""));
		
		if(Consts.INFOFIELDTITLEBAR)
		{
			g.setFont(g.getFont().deriveFont(Consts.INFOFIELDTBFONTSIZE));
			g.setColor(Consts.INFOFIELDTBBACKGROUND);
			g.fillRect(Consts.INFOFIELDTBX, 0, Consts.INFOFIELDTBWIDTH, Consts.TITLEBARHEIGHT);
			
			g.setColor(Consts.PLAYERCOLORS[GameCore.activePlayer % Consts.MAXPLAYERCOUNT]);
			
			g.drawString(text.toString(), Consts.INFOFIELDTBX + Consts.INFOFIELDMARGIN, Consts.TITLEBARHEIGHT - Consts.INFOFIELDMARGIN);
		}
		else
		{
			g.setFont(g.getFont().deriveFont(Consts.INFOFIELDFONTSIZE));
			g.setColor(Consts.INFOFIELDBACKGROUND);
			g.fillRect(Consts.INFOFIELDX, Consts.INFOFIELDY + Consts.TITLEBARHEIGHT, Consts.INFOFIELDWIDTH, (int)(text.toString().split("\n").length * Consts.INFOFIELDFONTSIZE * 1.5f + Consts.INFOFIELDFONTSIZE * 1));
			
			g.setColor(Consts.PLAYERCOLORS[GameCore.activePlayer % Consts.MAXPLAYERCOUNT]);
			

			int aLine = 1;
			for (String line : text.toString().split("\n"))
				g.drawString(line, Consts.INFOFIELDX + Consts.INFOFIELDMARGIN, Consts.INFOFIELDY + Consts.TITLEBARHEIGHT + (int)(Consts.INFOFIELDFONTSIZE * 1.5f * aLine++));
		}
		
		// HelpField
		if(Consts.HELPFIELDVISIBLE)
		{
			g.setFont(g.getFont().deriveFont(Consts.HELPFIELDFONTSIZE));
			g.setColor(Consts.HELPFIELDBACKGROUND);
			g.fillRect(Consts.SCREENWIDTH - Consts.HELPFIELDWIDTH - Consts.HELPFIELDX, Consts.TITLEBARHEIGHT + Consts.HELPFIELDY, Consts.HELPFIELDWIDTH, (int)(Consts.HELPFIELDTEXT.split("\n").length * Consts.HELPFIELDFONTSIZE * 1.5f + Consts.HELPFIELDFONTSIZE * 1));
			
			g.setColor(Consts.HELPFIELDFOREGROUND);
			int aLine = 1;
			for (String line : Consts.HELPFIELDTEXT.split("\n"))
				g.drawString(line, Consts.SCREENWIDTH - Consts.HELPFIELDWIDTH - Consts.HELPFIELDX + Consts.HELPFIELDMARGIN, Consts.TITLEBARHEIGHT + Consts.HELPFIELDY + (int)(Consts.HELPFIELDFONTSIZE * 1.5f * aLine++));
		}
		
		// ContinentInfo Field
		if(Consts.CONTINENTINFOFIELDVISIBLE)
		{
			g.setFont(g.getFont().deriveFont(Consts.CONTINENTINFOFIELDFONTSIZE));
			g.setColor(Consts.CONTINENTINFOFIELDBACKGROUND);
			g.fillRect(Consts.CONTINENTINFOFIELDX, Consts.SCREENHEIGHT - (int)(Consts.CONTINENTINFOFIELDY + Consts.CONTINENTINFOFIELDFONTSIZE * 1.3 * GameCore.continents.size()), Consts.CONTINENTINFOFIELDWIDTH, (int)(Consts.CONTINENTINFOFIELDFONTSIZE * 1.3 * (GameCore.continents.size() + 1)));
			g.setColor(Consts.CONTINENTINFOFIELDFOREGROUND);
			for(int i = 0; i < GameCore.continents.size(); i++)
			{
				if(GameCore.continentOccupied(i) >= 0)
					g.setColor(Consts.PLAYERCOLORS[GameCore.continentOccupied(i) % Consts.MAXPLAYERCOUNT]);
				else
					g.setColor(Consts.CONTINENTINFOFIELDFOREGROUND);
				g.drawString(GameCore.continents.get(i).units + ": " + GameCore.continents.get(i).name, Consts.CONTINENTINFOFIELDX + Consts.CONTINENTINFOFIELDMARGIN, Consts.SCREENHEIGHT - (int)(Consts.CONTINENTINFOFIELDY + Consts.CONTINENTINFOFIELDFONTSIZE * 1.3 * i - Consts.CONTINENTINFOFIELDFONTSIZE * 0.3));
			}
		}
		
		// CountDialog
		if(Consts.COUNTDIALOGVISIBLE)
		{
			g.setFont(g.getFont().deriveFont(Consts.COUNTDIALOGFONTSIZE));
			g.setColor(Consts.COUNTDIALOGBACKGROUND);
			g.fillRect(Consts.COUNTDIALOGX, Consts.COUNTDIALOGY + Consts.TITLEBARHEIGHT, Consts.COUNTDIALOGWIDTH, Consts.COUNTDIALOGHEIGHT);
			
			g.setColor(Consts.COUNTDIALOGFOREGROUND);
			g.drawString(String.format("%3d", countDialogValue), Consts.COUNTDIALOGX + Consts.COUNTDIALOGMARGIN, Consts.COUNTDIALOGY + Consts.TITLEBARHEIGHT + (int)(Consts.COUNTDIALOGFONTSIZE * 1.3));
		}
		g.finalize();
	}
}
