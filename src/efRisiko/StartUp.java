package efRisiko;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StartUp extends JPanel {
	
	public boolean finished;
	
	boolean inited = false;
	JFrame frame;
	
	JTextField tfPlayerCount;
	JComboBox cbMapName;
	JButton bStart;
	
	public void init()
	{
		inited = true;
		
		frame = new JFrame(Consts.GAMENAME);
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocation(100, 100);
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		this.add(new JLabel(Consts.GAMENAME));
		this.add(new JLabel("© 2014 Benjamin Schmid"));
		this.add(Box.createRigidArea(new Dimension(0, 20)));

		this.add(new JLabel("Playercount"));
		this.add(Box.createRigidArea(new Dimension(0, 4)));
		tfPlayerCount = new JTextField();
		tfPlayerCount.setText("" + Consts.PLAYERCOUNT);
		this.add(tfPlayerCount);
		this.add(Box.createRigidArea(new Dimension(0, 10)));

		this.add(new JLabel("Map"));
		this.add(Box.createRigidArea(new Dimension(0, 4)));
		File mapDir = new File(Consts.CONTENTFOLDER + Consts.MAPSFOLDER);
		cbMapName = new JComboBox(mapDir.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith(".txt");
			}
		}));
		this.add(cbMapName);
		this.add(Box.createRigidArea(new Dimension(0, 20)));
		
		// Players
		// [...]
		
		bStart = new JButton("Play");
		bStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					Consts.PLAYERCOUNT = Integer.parseInt(tfPlayerCount.getText());
					Consts.MAPNAME = cbMapName.getSelectedItem().toString();
					frame.setVisible(false);
					finished = true;
				}
				catch(NumberFormatException e)
				{
				}
			}
		});
		this.add(bStart);

		frame.pack();
	}
	
	public void show()
	{
		if(!inited)
			init();
		
		finished = false;
		frame.setVisible(true);
	}

}
