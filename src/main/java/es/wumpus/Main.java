package es.wumpus;

import es.wumpus.board.GameBoardException;
import es.wumpus.game.WumpusPlayer;
import es.wumpus.game.WumpusTextBasedInterface;

public class Main {

	public static void main(String[] args) {
		
		try {		
			
			WumpusTextBasedInterface.startGame(new WumpusPlayer());
			
		} catch (GameBoardException e) {
			System.err.println(e.getMessage());
		}
		
	}

}
