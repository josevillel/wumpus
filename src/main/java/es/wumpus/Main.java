package es.wumpus;

import es.wumpus.game.WumpusPlayer;
import es.wumpus.game.WumpusTextBasedInterface;

public class Main {

	public static void main(String[] args) {
		
		try {		
			
			WumpusTextBasedInterface.startGame(new WumpusPlayer());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
