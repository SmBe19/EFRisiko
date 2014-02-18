/**
 * GameInterface.java
 * (c) 2014 Benjamin Schmid
 * Created 13.02.2014
 * 
 * Displays the Simulation and interacts with the user
 */

package efRisiko;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class GameInterface extends JPanel {
	
	JFrame frame;
	
	public void init()
	{
		rnd = new Random();
	}
	
	public void show()
	{
		frame = new JFrame("EFRisiko");
		frame.add(this);
		frame.setSize(Consts.SCREENWIDTH, Consts.SCREENHEIGHT);
		frame.setVisible(true);
	}
	
	// DEBUG
	int aColor = 0;
	Random rnd;
	
	@Override
	public void paintComponent(Graphics gr)
	{
		Consts.SCREENWIDTH = getSize().width;
		Consts.SCREENHEIGHT = getSize().height;
		
		super.paintComponent(gr);
		
		Graphics2D g = (Graphics2D)gr;

		// DEBUG
		Color cols[] = {Color.red,Color.BLUE, Color.cyan, Color.black, Color.darkGray, Color.orange, Color.MAGENTA, Color.YELLOW};
		
		g.setBackground(Color.black);
		
		// DEBUG
		aColor++;
		aColor %= cols.length;
		g.setBackground(cols[aColor]);
		
		g.clearRect(0, 0, Consts.SCREENWIDTH, Consts.SCREENHEIGHT);
		
		// Titlebar
		g.setColor(Consts.TITLEBARBACKGROUND);
		
		// DEBUG
		aColor++;
		aColor %= cols.length;
		g.setColor(cols[aColor]);
		
		g.fillRect(0, 0, Consts.SCREENWIDTH, Consts.TITLEBARHEIGHT);
		g.setColor(Consts.TITLEBARFOREGROUND);
		
		// DEBUG
		aColor++;
		aColor %= cols.length;
		g.setColor(cols[aColor]);
		
		g.setFont(g.getFont().deriveFont(Consts.TITLEBARSIZE));
		g.drawString(Consts.GAMENAME, 10, Consts.TITLEBARHEIGHT - 10);
		
		// DEBUG
		aColor++;
		aColor %= cols.length;
		g.setColor(cols[aColor]);
		for(int i = 0; i < 10; i++)
		{
			g.fillRect(rnd.nextInt(Consts.SCREENWIDTH), rnd.nextInt(Consts.SCREENHEIGHT), rnd.nextInt(Consts.SCREENWIDTH / 2), rnd.nextInt(Consts.SCREENHEIGHT / 2));
		}
		aColor++;
		aColor %= cols.length;
		g.setColor(cols[aColor]);
		for(int i = 0; i < 10; i++)
		{
			g.fillRect(rnd.nextInt(Consts.SCREENWIDTH), rnd.nextInt(Consts.SCREENHEIGHT), rnd.nextInt(Consts.SCREENWIDTH / 2), rnd.nextInt(Consts.SCREENHEIGHT / 2));
		}
		aColor++;
		aColor %= cols.length;
		g.setColor(cols[aColor]);
		for(int i = 0; i < 10; i++)
		{
			g.fillOval(rnd.nextInt(Consts.SCREENWIDTH), rnd.nextInt(Consts.SCREENHEIGHT), rnd.nextInt(Consts.SCREENWIDTH / 2), rnd.nextInt(Consts.SCREENHEIGHT / 2));
		}
		aColor++;
		aColor %= cols.length;
		g.setColor(cols[aColor]);
		g.drawString("Wer das liest ist doof!", rnd.nextInt(Consts.SCREENWIDTH/2), rnd.nextInt(Consts.SCREENHEIGHT));
	}
}
