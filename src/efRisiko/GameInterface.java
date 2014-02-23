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

import javax.swing.JFrame;
import javax.swing.JPanel;


public class GameInterface extends JPanel {
	
	JFrame frame;
	
	public void init()
	{
	}
	
	public void show()
	{
		frame = new JFrame("EFRisiko");
		frame.add(this);
		frame.setSize(Consts.SCREENWIDTH, Consts.SCREENHEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
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
		
		// Content
		
		// Background
		g.fillRect(0, Consts.TITLEBARHEIGHT, Consts.SCREENWIDTH, Consts.SCREENHEIGHT - Consts.TITLEBARHEIGHT);
		g.drawImage(GameCore.backgroundImage, 0, Consts.TITLEBARHEIGHT, Consts.SCREENWIDTH, Consts.SCREENHEIGHT - Consts.TITLEBARHEIGHT, this);
		
		// Graph
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(5));
		for(int i = 0; i < GameCore.regions.size(); i++)
			for(int j = 0; j < GameCore.regions.get(i).connections.size(); j++)
				g.drawLine((int)(GameCore.regions.get(i).location.x * scaleX), (int)(GameCore.regions.get(i).location.y * scaleY + Consts.TITLEBARHEIGHT), (int)(GameCore.regions.get(GameCore.regions.get(i).connections.get(j)).location.x * scaleX), (int)(GameCore.regions.get(GameCore.regions.get(i).connections.get(j)).location.y * scaleY + Consts.TITLEBARHEIGHT));
		
		g.setFont(g.getFont().deriveFont(Consts.VERTEXFONTSIZE));
		
		for(int i = 0; i < GameCore.regions.size(); i++)
		{
			g.setColor(Color.white);
			g.fillOval((int)(GameCore.regions.get(i).location.x * scaleX - Consts.VERTEXSIZE / 2), (int)(GameCore.regions.get(i).location.y * scaleY + Consts.TITLEBARHEIGHT - Consts.VERTEXSIZE / 2), Consts.VERTEXSIZE, Consts.VERTEXSIZE);
			if(GameCore.regions.get(i).player >= 0)
				g.setColor(Consts.PLAYERCOLORS[GameCore.regions.get(i).player % Consts.MAXPLAYERCOUNT]);
			else
				g.setColor(Consts.NEUTRALPLAYERCOLOR);
			g.drawOval((int)(GameCore.regions.get(i).location.x * scaleX - Consts.VERTEXSIZE / 2), (int)(GameCore.regions.get(i).location.y * scaleY + Consts.TITLEBARHEIGHT - Consts.VERTEXSIZE / 2), Consts.VERTEXSIZE, Consts.VERTEXSIZE);

			g.drawString(String.format("%3d", GameCore.regions.get(i).units), (int)(GameCore.regions.get(i).location.x * scaleX - Consts.VERTEXUNITSTRINGOFFSET), (int)(GameCore.regions.get(i).location.y * scaleY + Consts.TITLEBARHEIGHT + Consts.VERTEXFONTSIZE / 2 - 2));
		}
		
		g.finalize();
	}
}
