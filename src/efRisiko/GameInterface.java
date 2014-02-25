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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import efRisiko.GameCore.GameState;


public class GameInterface extends JPanel {
	
	JFrame frame;
	
	int activeRegion;
	
	boolean countDialogVisible;
	int countDialogValue;
	int countDialogAMax;
	
	/**
	 * Initialisiert das GameInterface
	 */
	public void init()
	{
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(countDialogVisible && arg0.getX() > Consts.COUNTDIALOGX && arg0.getX() < Consts.COUNTDIALOGX + Consts.COUNTDIALOGWIDTH
						&& arg0.getY() > Consts.COUNTDIALOGY + Consts.TITLEBARHEIGHT && arg0.getY() < Consts.COUNTDIALOGY + Consts.COUNTDIALOGHEIGHT + Consts.TITLEBARHEIGHT)
				{
					if(arg0.getX() < Consts.COUNTDIALOGX + Consts.COUNTDIALOGWIDTH / 2)
					{
						if(arg0.getButton() == MouseEvent.BUTTON1)
							countDialogValue--;
						else if (arg0.getButton() == MouseEvent.BUTTON3)
							countDialogValue -= 5;
					}
					else
					{
						if(arg0.getButton() == MouseEvent.BUTTON1)
							countDialogValue++;
						else if (arg0.getButton() == MouseEvent.BUTTON3)
							countDialogValue += 5;
					}
					if(countDialogValue < 0)
						countDialogValue = 0;
					if(countDialogValue > countDialogAMax)
						countDialogValue = countDialogAMax;
				}
				else if(!GameCore.isPreparation && GameCore.activeState != GameState.REINFORCE
						&& arg0.getX() > Consts.SCREENWIDTH - Consts.NEXTPHASEBUTTONOFFSET && arg0.getX() < Consts.SCREENWIDTH
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
							GameCore.placeUnit(reg);
					}
					else
					{
						switch (GameCore.activeState) {
						case REINFORCE:
							if(reg >= 0)
								GameCore.placeUnit(reg);
							break;
						case ATTACK:
							if(activeRegion < 0)
							{
								if(GameCore.regions.get(reg).player == GameCore.activePlayer)
									activeRegion = reg;
							}
							else
							{
								if(reg >= 0 && GameCore.regions.get(reg).player != GameCore.activePlayer)
									GameCore.attack(activeRegion, reg);
								else
									activeRegion = -1;
							}
							if(GameCore.activeState == GameState.BACK)
							{
								countDialogValue = 0;
								countDialogAMax = GameCore.regions.get(activeRegion).units - 1;
								countDialogVisible = true;
								
								activeRegion = reg;
							}
							break;
						case BACK:
							if(reg == GameCore.attackDrain)
							{
								GameCore.backRegion(countDialogValue);
								countDialogVisible = false;
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
									countDialogVisible = true;
								}
							}
							else
							{
								if(reg >= 0 && GameCore.regions.get(reg).player == GameCore.activePlayer)
									GameCore.move(activeRegion, reg, countDialogValue);
								activeRegion = -1;
								countDialogVisible = false;
							}
							break;
						}
					}
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
		frame = new JFrame("EFRisiko");
		frame.add(this);
		frame.setSize(Consts.SCREENWIDTH, Consts.SCREENHEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		countDialogValue = 0;
		countDialogVisible = false;
		
		activeRegion = -1;
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
			g.setColor(Color.white);
			if(activeRegion == i)
				g.setColor(Color.black);
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
		g.setFont(g.getFont().deriveFont(Consts.INFOFIELDFONTSIZE));
		g.setColor(Consts.INFOFIELDBACKGROUND);
		g.fillRect(Consts.INFOFIELDX, Consts.INFOFIELDY + Consts.TITLEBARHEIGHT, Consts.INFOFIELDWIDTH, Consts.INFOFIELDHEIGHT);
		
		g.setColor(Consts.PLAYERCOLORS[GameCore.activePlayer % Consts.MAXPLAYERCOUNT]);
		g.drawString("Active player: " + GameCore.activePlayer, Consts.INFOFIELDX + Consts.INFOFIELDMARGIN, Consts.INFOFIELDY + Consts.TITLEBARHEIGHT + (int)(Consts.INFOFIELDFONTSIZE * 1.5));
		if(GameCore.isPreparation)
			g.drawString("Active phase: PREP", Consts.INFOFIELDX + Consts.INFOFIELDMARGIN, Consts.INFOFIELDY + Consts.TITLEBARHEIGHT + (int)(Consts.INFOFIELDFONTSIZE * 3));
		else
			g.drawString("Active phase: " + GameCore.activeState, Consts.INFOFIELDX + Consts.INFOFIELDMARGIN, Consts.INFOFIELDY + Consts.TITLEBARHEIGHT + (int)(Consts.INFOFIELDFONTSIZE * 3));
		
		if(GameCore.isPreparation || GameCore.activeState == GameState.REINFORCE)
			g.drawString("units left: " + (int)Math.ceil(GameCore.unitsLeft * (GameCore.isPreparation ? 1f/Consts.PLAYERCOUNT : 1)), Consts.INFOFIELDX + 10, Consts.INFOFIELDY + Consts.TITLEBARHEIGHT + (int)(Consts.INFOFIELDFONTSIZE * 4.5));
		else if ((GameCore.activeState == GameState.ATTACK || GameCore.activeState == GameState.BACK) && GameCore.attackResA1 > 0)
			g.drawString("Result: " + GameCore.attackResA1 + (GameCore.attackResA1 > GameCore.attackResD1 ? ">" : "<=") + GameCore.attackResD1 +
					(GameCore.attackResA2 > 0 && GameCore.attackResD2 > 0 ? (" & " + GameCore.attackResA2 + (GameCore.attackResA2 > GameCore.attackResD2 ? ">" : "<=") + GameCore.attackResD2) : "") +
					"; A: " + (GameCore.attackResA1 > 0 ? GameCore.attackResA1 : "") + (GameCore.attackResA2 > 0 ? ", " + GameCore.attackResA2 : "")  + (GameCore.attackResA3 > 0 ? ", " + GameCore.attackResA3 : "") +
					"; D: " + (GameCore.attackResD1 > 0 ? GameCore.attackResD1 : "")  + (GameCore.attackResD2 > 0 ? ", " + GameCore.attackResD2 : ""),
					Consts.INFOFIELDX + 10, Consts.INFOFIELDY + Consts.TITLEBARHEIGHT + (int)(Consts.INFOFIELDFONTSIZE * 4.5));
		
		// ContinentInfo Field
		g.setFont(g.getFont().deriveFont(Consts.CONTINENTINFOFIELDFONTSIZE));
		g.setColor(Consts.CONTINENTINFOFIELDBACKGROUND);
		g.fillRect(Consts.CONTINENTINFOFIELDX, Consts.SCREENHEIGHT - (int)(Consts.CONTINENTINFOFIELDY + Consts.CONTINENTINFOFIELDFONTSIZE * 1.3 * GameCore.continents.size()), Consts.CONTINENTINFOFIELDWIDTH, (int)(Consts.CONTINENTINFOFIELDFONTSIZE * 1.3 * (GameCore.continents.size() + 1)));
		g.setColor(Consts.CONTINENTINFOFIELDFOREGROUND);
		for(int i = 0; i < GameCore.continents.size(); i++)
			g.drawString(GameCore.continents.get(i).units + ": " + GameCore.continents.get(i).name, Consts.CONTINENTINFOFIELDX + Consts.CONTINENTINFOFIELDMARGIN, Consts.SCREENHEIGHT - (int)(Consts.CONTINENTINFOFIELDY + Consts.CONTINENTINFOFIELDFONTSIZE * 1.3 * i - Consts.CONTINENTINFOFIELDFONTSIZE * 0.3));
		
		// CountDialog
		if(countDialogVisible)
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
