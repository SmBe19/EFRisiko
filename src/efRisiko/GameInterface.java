/**
 * GameInterface.java
 * (c) 2014 Benjamin Schmid
 * Created 13.02.2014
 * 
 * Displays the Simulation and interacts with the user
 */

package efRisiko;

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
		frame.setVisible(true);
	}
	
	@Override
	public void paintComponent(Graphics gr)
	{
		Consts.SCREENWIDTH = getSize().width;
		Consts.SCREENHEIGHT = getSize().height;
		
		super.paintComponent(gr);
		
		Graphics2D g = (Graphics2D)gr;

		g.setBackground(Color.black);
		g.clearRect(0, 0, Consts.SCREENWIDTH, Consts.SCREENHEIGHT);
		
		// Titlebar
		g.setColor(Consts.TITLEBARBACKGROUND);
		g.fillRect(0, 0, Consts.SCREENWIDTH, Consts.TITLEBARHEIGHT);
		g.setColor(Consts.TITLEBARFOREGROUND);
		g.setFont(g.getFont().deriveFont(Consts.TITLEBARSIZE));
		g.drawString(Consts.GAMENAME, 10, Consts.TITLEBARHEIGHT - 10);
		
		// Content
		
		// Background
		g.fillRect(0, Consts.TITLEBARHEIGHT, Consts.SCREENWIDTH, Consts.SCREENHEIGHT - Consts.TITLEBARHEIGHT);
		g.drawImage(GameCore.backgroundImage, 0, Consts.TITLEBARHEIGHT, Consts.SCREENWIDTH, Consts.SCREENHEIGHT - Consts.TITLEBARHEIGHT, this);
		
		g.finalize();
	}
}
