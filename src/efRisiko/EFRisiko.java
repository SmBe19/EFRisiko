/**
 * EFRisiko.java
 * (c) 2014 Benjamin Schmid
 * Created: 12.02.2014
 * 
 * Main class for the simulation.
 * Initializes the GameCore and starts the GameInterface.
 */

package efRisiko;

import efRisiko.Player.PlayerControlType;

public class EFRisiko {

	public static void main(String[] args) {
		System.out.println("EFRisiko\n© 2014 Benjamin Schmid");

		StartUp startUp = new StartUp();
		if(Consts.SHOWSTARTUP)
		{
			startUp.show();
			
			while(!startUp.finished)
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
		}
		
		System.out.println("Initialize GameCore...");
		
		if(!GameCore.init())
		{
			System.out.println("Init failed");
			if(Consts.SHOWSTARTUP)
				startUp.close();
			return;
		}
		
		// Debug
		if(Consts.ISDEBUG)
			GameCore.unitsLeft = Consts.PLAYERCOUNT * Consts.DEBUGSTARTUNITS;

		if(Consts.SHOWSTARTUP)
			for(int i = 0; i < Consts.PLAYERCOUNT; i++)
			{
				GameCore.players.get(i).controlType = startUp.playertypes[i];
				GameCore.players.get(i).connectionString = startUp.playertypesConnection[i];
				if(startUp.playertypes[i] == PlayerControlType.AI)
				{
					GameCore.players.get(i).ai = new AI(startUp.playertypesConnection[i], i);
				}
			}
		else
		{
			GameCore.players.get(0).controlType = PlayerControlType.AI;
			GameCore.players.get(0).connectionString = "C:\\Users\\Benjamin\\Documents\\workspace\\EFRisiko\\ai\\DemoAI\\bin\\Debug\\DemoAI.exe";
			GameCore.players.get(0).ai = new AI(GameCore.players.get(0).connectionString, 0);
		}

		if(Consts.AUTOPLACEUNITS)
		{
			while(GameCore.isPreparation)
			{
				GameCore.placeUnits(GameCore.rnd.nextInt(GameCore.regions.size()), 1);
			}
		}
		
		System.out.println("Done!");
		
		System.out.println("Initialize GameInterface...");
		GameInterface gameInterface = new GameInterface();
		gameInterface.init();
		gameInterface.show();
		
		System.out.println("Done!");
	}

}
