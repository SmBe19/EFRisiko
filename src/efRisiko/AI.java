/**
 * AI.java
 * (c) 2014 Benjamin Schmid
 * Created 28.03.2014
 * 
 * Represents an AI
 */

package efRisiko;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class AI {
	
	String connectionString;
	int playerNumber;
	Process process;
	BufferedReader in;
	BufferedWriter out;
	
	int activeOrder;
	boolean handleInputActive;
	
	String preWriteMsg;
	
	String line;
	boolean debug;
	
	/**
	 * Constructor, stellt die Verbindung zur AI her und übermittelt Gameinfos
	 * @param ai Connectionstring zur AI
	 * @param num Die Spielernummer der AI
	 */
	public AI(String ai, int num)
	{
		connectionString = ai;
		playerNumber = num;
		
		try {
			process = new ProcessBuilder(ai).start();
			in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

			write("GDay");
			line = "";
			
			while(!(line = read()).equals("WazUp") && line != null)
				processInput(line);
			
			processInput("#40");
			processInput("#41");
			processInput("#42");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * übermittelt eine Nachricht zur AI
	 * @param s die Nachricht
	 * @param noFlush falls true, wird flush() nicht aufgerufen. So können grosse Datenmengen schneller übertragen werden
	 */
	public void write(Object s, boolean noFlush)
	{
		try {
			if(preWriteMsg != null)
				writePreWrite();
			if(debug)
				System.out.println(playerNumber + "_GC: " + s);
			if(s.toString().startsWith("#2"))
				System.out.println("-----------------------");
			out.write(s.toString() + "\n");
			if(!noFlush)
				out.flush();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
	/**
	 * übermittelt einer Nachricht zur AI
	 * @param s die Nachricht
	 */
	public void write(Object s)
	{
		write(s, false);
	}
	
	/**
	 * Speichert eine Nachricht, die beim nächsten Aufruf von write geschrieben wird.
	 * @param s die Nachricht. null übergeben, um zu wiederrufen
	 */
	public void preWrite(Object s)
	{
		if(s != null)
			preWriteMsg = s.toString();
		else
			preWriteMsg = null;
	}
	
	/**
	 * Schreibt die zwischengespeicherte Nachricht
	 */
	public void writePreWrite()
	{
		if(preWriteMsg == null)
			return;
		String tmp = preWriteMsg;
		preWriteMsg = null;
		write(tmp);
	}
	
	/**
	 * ruft flush() auf dem Nachrichtenstream auf
	 */
	public void flush()
	{
		try {
			out.flush();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
	/**
	 * Empfängt Nachrichten der AI
	 * @return die Nachricht
	 */
	public String read()
	{
		String res;
		try {
			res = in.readLine();
			if(debug)
				System.out.println(playerNumber + "_AI: " + res);
			return res;
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Übermittelt einen Befehl an die AI und kümmert sich um die Antworten
	 * @param code Code des Befehls
	 */
	public void placeOrder(int code)
	{
		int oldActiveOrder = activeOrder;
		activeOrder = code;
		write("#" + code);
		switch(code)
		{
		case 60:
			if(GameCore.isPreparation)
				write("1");
			else
				write("" + GameCore.unitsLeft);
			break;
		case 62:
			write(GameCore.attackSource + " " + GameCore.attackDrain);
			break;
		}
		handleInput();
		activeOrder = oldActiveOrder;
	}
	
	public void repeatOrder()
	{
		if(activeOrder == 0)
			return;
		if(!handleInputActive)
		{
			placeOrder(activeOrder);
			return;
		}
		write("#" + activeOrder);
		switch(activeOrder)
		{
		case 60:
			if(GameCore.isPreparation)
				write("1");
			else
				write("" + GameCore.unitsLeft);
			break;
		case 62:
			write(GameCore.attackSource + " " + GameCore.attackDrain);
			break;
		}
	}
	
	/**
	 * Liest Nachrichten der AI und gibt sie zur Verarbeitung weiter
	 */
	public void handleInput()
	{
		handleInputActive = true;
		while(handleInputActive && (line = read()) != null)
			processInput(line);
	}
	
	/**
	 * Sendet Spielinformationen
	 */
	void send30()
	{
		write("#30");
		write(Consts.PLAYERCOUNT);
		write(playerNumber);
		repeatOrder();
	}
	
	/**
	 * Sendet die Karte
	 */
	void send31()
	{
		write("#31");
		write(GameCore.regions.size() + " " + GameCore.edgeCount);
		for(int i = 0; i < GameCore.regions.size(); i++)
			for(int j = 0; j < GameCore.regions.get(i).connections.size(); j++)
				if(GameCore.regions.get(i).connections.get(j) > i)
					write(i + " " + GameCore.regions.get(i).connections.get(j), true);
		flush();
		repeatOrder();
	}
	
	/**
	 * Sendet Kontinenteninformationen
	 */
	void send32()
	{
		write("#32");
		write("" + GameCore.continents.size());
		for(int i = 0; i < GameCore.continents.size(); i++)
		{
			write(GameCore.continents.get(i).units + " " + GameCore.continents.get(i).regions.size());
			String s = "" + GameCore.continents.get(i).regions.get(0);
			for(int j = 1; j < GameCore.continents.get(i).regions.size(); j++)
				s += " " + GameCore.continents.get(i).regions.get(j);
			write(s);
		}
		repeatOrder();
	}
	
	/**
	 * Sendet den Spielzustand
	 */
	void send33()
	{
		write("#33");
		for(int i = 0; i < GameCore.regions.size(); i++)
		{
			write(GameCore.regions.get(i).player + " " + GameCore.regions.get(i).units, true);
		}
		flush();
		repeatOrder();
	}
	
	/**
	 * Sendet die aktuelle Aufgabe erneut
	 */
	void handle44()
	{
		switch(GameCore.activeState)
		{
		case REINFORCE:
			write("#60");
			write("" + GameCore.unitsLeft);
			break;
		case ATTACK:
			write("#61");
			break;
		case BACK:
			write("#62");
			break;
		case MOVE:
			write("#63");
			break;
		}
	}
	
	/**
	 * Platziert Einheiten
	 */
	void handle50()
	{
		line = read();
		preWrite("#10");
		if(GameCore.placeUnits(Integer.parseInt(line.split(" ")[0]), Integer.parseInt(line.split(" ")[1])))
		{
			if(GameCore.unitsLeft <= 0)
				handleInputActive = false;
			writePreWrite();
		}
		else
		{
			preWrite(null);
			write("#" + GameCore.errorCode);
		}
	}
	
	/**
	 * Greift an
	 */
	void handle51()
	{
		line = read();
		if(GameCore.attack(Integer.parseInt(line.split(" ")[0]), Integer.parseInt(line.split(" ")[1])))
		{
			handleInputActive = false;
			if(GameCore.attackSuccess)
			{
				write("#11");
				write(GameCore.regions.get(GameCore.attackSource).units + " " + GameCore.regions.get(GameCore.attackDrain).units);
				placeOrder(62);
			}
			else
			{
				write("#12");
				write(GameCore.regions.get(GameCore.attackSource).units + " " + GameCore.regions.get(GameCore.attackDrain).units);
			}
		}
		else
		{
			write("#" + GameCore.errorCode);
		}
		
		placeOrder(61);
	}
	
	/**
	 * Verstärkung nach Eroberung
	 */
	void handle52()
	{
		line = read();
		if(GameCore.backRegion(Integer.parseInt(line)))
		{
			handleInputActive = false;
			write("#10");
		}
		else
			write("#" + GameCore.errorCode);
	}
	
	/**
	 * Verschiebt Einheiten
	 */
	void handle53()
	{
		line = read();
		if(GameCore.move(Integer.parseInt(line.split(" ")[0]), Integer.parseInt(line.split(" ")[1]), Integer.parseInt(line.split(" ")[2])))
		{
			handleInputActive = false;
			write("#10");
		}
		else
			write("#" + GameCore.errorCode);
	}
	
	/**
	 * Wechselt zur nächsten Phase
	 */
	void handle54()
	{
		handleInputActive = false;
		if(!GameCore.nextPhase())
		{
			write("#" + GameCore.errorCode);
			handleInputActive = true;
		}
	}
	
	/**
	 * Verarbeitet Statuscodes
	 * @param s Statuscode
	 */
	public void processInput(String s)
	{
		if(s.startsWith("#"))
		{
			if(s.equals("#40"))
			{
				send30();
			}
			if(s.equals("#41"))
			{
				send31();
			}
			if(s.equals("#42"))
			{
				send32();
			}
			if(s.equals("#43"))
			{
				send33();
			}
			if(s.equals("#44"))
			{
				handle44();
			}
			if(s.equals("#50"))
			{
				handle50();
			}
			if(s.equals("#51"))
			{
				handle51();
			}
			if(s.equals("#52"))
			{
				handle52();
			}
			if(s.equals("#53"))
			{
				handle53();
			}
			if(s.equals("#54"))
			{
				handle54();
			}
			if(s.equals("#70"))
			{
				debug = true;
			}
			if(s.equals("#71"))
			{
				debug = false;
			}
			if(s.equals("#72"))
			{
				System.out.println(read());
			}
		}
	}
}
