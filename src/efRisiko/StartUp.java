/**
 * StartUp.java
 * (c) 2014 Benjamin Schmid
 * Created 19.03.2014
 * 
 * StartUp Menu
 */

package efRisiko;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import efRisiko.Player.PlayerControlType;

public class StartUp extends JPanel {
	
	public boolean finished;
	
	public PlayerControlType[] playertypes;
	public String[] playertypesConnection;
	public String[] playerNames;
	
	boolean inited = false;
	JFrame frame;
	
	JComboBox cbPlayerCount;
	JComboBox cbMapName;
	JComboBox cbAPlayer;
	JComboBox cbPlayerType;
	JTextField tfPlayerName;
	JTextField tfPlayerTypeConnection;
	JButton bStart;
	
	/**
	 * Initialisirt das Fenster und platziert alle Steuerelemente
	 */
	public void init()
	{
		playertypes = new PlayerControlType[Consts.MAXPLAYERCOUNT];
		playertypesConnection = new String[Consts.MAXPLAYERCOUNT];
		playerNames = new String[Consts.MAXPLAYERCOUNT];
		for(int i = 0; i < Consts.MAXPLAYERCOUNT; i++)
		{
			playertypes[i] = PlayerControlType.LOCAL;
			playertypesConnection[i] = "";
			playerNames[i] = "" + (i+1);
		}
		readSettings();
		
		inited = true;
		
		frame = new JFrame(Consts.GAMENAME);
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setResizable(false);
		frame.setLocation(100, 100);
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		this.add(new JLabel(Consts.GAMENAME));
		this.add(new JLabel("© 2014 Benjamin Schmid"));
		this.add(Box.createRigidArea(new Dimension(0, 20)));

		// Playercount
		this.add(new JLabel("Playercount"));
		this.add(Box.createRigidArea(new Dimension(0, 4)));
		cbPlayerCount = new JComboBox();
		for(int i = 0; i < Consts.MAXPLAYERCOUNT; i++)
		{
			cbPlayerCount.addItem((i+1));
		}
		cbPlayerCount.setSelectedIndex(Consts.PLAYERCOUNT-1);
		cbPlayerCount.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				cbAPlayer.removeAllItems();
				Consts.PLAYERCOUNT = cbPlayerCount.getSelectedIndex() + 1;
				for(int i = 0; i < Consts.PLAYERCOUNT; i++)
				{
					cbAPlayer.addItem("Player " + (i+1));
				}
			}
		});
		cbPlayerCount.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
		this.add(cbPlayerCount);
		this.add(Box.createRigidArea(new Dimension(0, 30)));

		// Map
		this.add(new JLabel("Map"));
		this.add(Box.createRigidArea(new Dimension(0, 4)));
		File mapDir = new File(Consts.CONTENTFOLDER + Consts.MAPSFOLDER);
		cbMapName = new JComboBox(mapDir.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith(".txt");
			}
		}));
		cbMapName.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
		cbMapName.setSelectedItem(Consts.MAPNAME);
		this.add(cbMapName);
		this.add(Box.createRigidArea(new Dimension(0, 30)));
		
		// Players
		this.add(new JLabel("Player"));
		this.add(Box.createRigidArea(new Dimension(0, 4)));
		
		cbAPlayer = new JComboBox();
		for(int i = 0; i < Consts.PLAYERCOUNT; i++)
		{
			cbAPlayer.addItem("Player " + (i+1));
		}
		cbAPlayer.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
		cbAPlayer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(cbAPlayer.getSelectedIndex() < 0)
					return;
				cbPlayerType.setSelectedItem(playertypes[cbAPlayer.getSelectedIndex()]);
				tfPlayerTypeConnection.setText(playertypesConnection[cbAPlayer.getSelectedIndex()]);
				tfPlayerName.setText(playerNames[cbAPlayer.getSelectedIndex()]);
			}
		});
		this.add(cbAPlayer);
		this.add(Box.createRigidArea(new Dimension(0, 10)));
		
		this.add(new JLabel("Playername"));
		this.add(Box.createRigidArea(new Dimension(0, 4)));
		tfPlayerName = new JTextField();
		tfPlayerName.setAlignmentX(JTextField.LEFT_ALIGNMENT);
		tfPlayerName.setText(playerNames[0]);
		tfPlayerName.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				update();
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				update();
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
			}
			
			void update()
			{
				playerNames[cbAPlayer.getSelectedIndex()] = tfPlayerName.getText();
			}
		});
		this.add(tfPlayerName);
		this.add(Box.createRigidArea(new Dimension(0, 10)));

		this.add(new JLabel("Playertype"));
		this.add(Box.createRigidArea(new Dimension(0, 4)));
		cbPlayerType = new JComboBox(Player.PlayerControlType.values());
		cbPlayerType.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
		cbPlayerType.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				playertypes[cbAPlayer.getSelectedIndex()] = (PlayerControlType) cbPlayerType.getSelectedItem();
				if(playertypes[cbAPlayer.getSelectedIndex()] == PlayerControlType.LOCAL)
				{
					tfPlayerTypeConnection.setEditable(false);
				}
				else
				{
					tfPlayerTypeConnection.setEditable(true);
				}
			}
		});
		this.add(cbPlayerType);
		this.add(Box.createRigidArea(new Dimension(0, 10)));

		this.add(new JLabel("Connection string"));
		this.add(Box.createRigidArea(new Dimension(0, 4)));
		tfPlayerTypeConnection = new JTextField();
		tfPlayerTypeConnection.setAlignmentX(JTextField.LEFT_ALIGNMENT);
		tfPlayerTypeConnection.setText(playertypesConnection[0]);
		tfPlayerTypeConnection.setEditable(false);
		tfPlayerTypeConnection.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				update();
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				update();
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
			}
			
			void update()
			{
				playertypesConnection[cbAPlayer.getSelectedIndex()] = tfPlayerTypeConnection.getText();
			}
		});
		this.add(tfPlayerTypeConnection);
		this.add(Box.createRigidArea(new Dimension(0, 30)));
		
		// Play
		bStart = new JButton("Play");
		bStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					Consts.PLAYERCOUNT = cbPlayerCount.getSelectedIndex() + 1;
					Consts.MAPNAME = cbMapName.getSelectedItem().toString();
					frame.setVisible(false);
					finished = true;
					saveSettings();
				}
				catch(NumberFormatException e)
				{
				}
			}
		});
		this.add(bStart);
		
		cbPlayerType.setSelectedItem(playertypes[0]);

		frame.pack();
	}
	
	/**
	 * Zeigt das Fenster an
	 */
	public void show()
	{
		if(!inited)
			init();
		
		finished = false;
		frame.setVisible(true);
	}
	
	/**
	 * schliesst das Fenster
	 */
	public void close()
	{
		frame.dispose();
	}

	void readSettings()
	{
		BufferedReader reader = null;
		if(!new File(Consts.SAVEFOLDER + Consts.STARTUPSAVEFILE).exists())
			return;
		try {
			reader = new BufferedReader(new FileReader(Consts.SAVEFOLDER + Consts.STARTUPSAVEFILE));
			Consts.MAPNAME = reader.readLine();
			Consts.PLAYERCOUNT = Integer.parseInt(reader.readLine());
			for(int i = 0; i < Consts.PLAYERCOUNT; i++)
			{
				playerNames[i] = reader.readLine();
				playertypes[i] = PlayerControlType.valueOf(reader.readLine());
				playertypesConnection[i] = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void saveSettings()
	{
		PrintStream writer = null;
		try {
			if(!new File(Consts.SAVEFOLDER + Consts.STARTUPSAVEFILE).exists())
				new File(Consts.SAVEFOLDER + Consts.STARTUPSAVEFILE).createNewFile();
			writer = new PrintStream(new File(Consts.SAVEFOLDER + Consts.STARTUPSAVEFILE));
			writer.println(Consts.MAPNAME);
			writer.println(""+Consts.PLAYERCOUNT);
			for(int i = 0; i < Consts.PLAYERCOUNT; i++)
			{
				writer.println(playerNames[i]);
				writer.println(playertypes[i].toString());
				writer.println(playertypesConnection[i]);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			if(writer != null)
				writer.close();
		}
	}
}
