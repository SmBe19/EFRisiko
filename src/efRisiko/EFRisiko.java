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
		GameCore.init();
		System.out.println("Done!");
		
		System.out.println("Initialize GameInterface...");
		GameInterface gameInterface = new GameInterface();
		gameInterface.init();
		gameInterface.show();
		
		for(long i = 0; i < 1000000000L; i++)
			gameInterface.repaint();
		
		System.out.println("Done!");
	}

}
