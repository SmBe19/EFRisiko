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
	public void init()
	{
		
	}
	
	public void show()
	{
		JFrame frame = new JFrame("EFRisiko");
		frame.add(this);
		frame.setSize(400, 400);
		frame.setVisible(true);
	}
	
	@Override
	public void paintComponent(Graphics gr)
	{
		super.paintComponent(gr);
		
		Graphics2D g = (Graphics2D)gr;
		g.setBackground(Color.black);
		g.clearRect(0, 0, getSize().width, getSize().height);
	}
}
