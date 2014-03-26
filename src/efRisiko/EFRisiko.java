/**
 * EFRisiko.java
 * (c) 2014 Benjamin Schmid
 * Created: 12.02.2014
 * 
 * Main class for the simulation.
 * Initializes the GameCore and starts the GameInterface.
 */

package efRisiko;

public class EFRisiko {

	public static void main(String[] args) {
		System.out.println("EFRisiko\n© 2014 Benjamin Schmid");
		
		StartUp startUp = new StartUp();
		startUp.show();
		
		while(!startUp.finished)
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		
		System.out.println("Initialize GameCore...");
		
		if(!GameCore.init())
		{
			System.out.println("Init failed");
			startUp.close();
			return;
		}
		
		// Debug
		if(Consts.ISDEBUG)
			GameCore.unitsLeft = Consts.PLAYERCOUNT * Consts.DEBUGSTARTUNITS;
		
		for(int i = 0; i < Consts.PLAYERCOUNT; i++)
		{
			GameCore.players.get(i).controlType = startUp.playertypes[i];
			GameCore.players.get(i).connectionString = startUp.playertypesConnection[i];
		}
		
		System.out.println("Done!");
		
		System.out.println("Initialize GameInterface...");
		GameInterface gameInterface = new GameInterface();
		gameInterface.init();
		gameInterface.show();
		
		System.out.println("Done!");
	}

}
