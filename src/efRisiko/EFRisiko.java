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
		System.out.println("EFRisiko\n(c) 2014 Benjamin Schmid");
		
		// ToDo: Startmenu
		
		System.out.println("Initialize GameCore...");
		
		// Set Playercount
		
		if(!GameCore.init())
		{
			System.out.println("Init failed");
			return;
		}
		
		// Debug
		GameCore.unitsLeft = Consts.PLAYERCOUNT * 4;
		
		// Set Playertype
		
		System.out.println("Done!");
		
		System.out.println("Initialize GameInterface...");
		GameInterface gameInterface = new GameInterface();
		gameInterface.init();
		gameInterface.show();
		
		System.out.println("Done!");
	}

}
