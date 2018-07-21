package es.wumpus.game;

import java.util.List;
import java.util.Scanner;

import es.wumpus.board.Cell;
import es.wumpus.game.WumpusRules.Actions;
import es.wumpus.game.WumpusRules.Perceptions;
import es.wumpus.game.WumpusRules.Results;
import es.wumpus.game.WumpusRules.WumpusElements;

public class WumpusTextBasedInterface {
	
	private static final String SEPARATOR_PAD = "################################################";
	private static final String SEPARATOR_STAR = "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *";
	private static final boolean BEGGINER_MODE=false;
	
	private WumpusTextBasedInterface(){}

	/**
	 * Starts the game 
	 * @param wumpusPlayer {@link WumpusPlayer}
	 * @throws Exception 
	 */
	public static void startGame(WumpusPlayer wumpusPlayer) throws Exception {

		printWelcome();
		
		Scanner reader = new Scanner (System.in);
		reader.nextLine();
		
		Results result;
		String option;
		int optionNumber = 0;
		
		do {
			
			do {
				printLB(50);
				printHeader();
				result = wumpusPlayer.checkTheGoal();
				
				if(Results.GO_ON.equals(result)) {
					
					showBoard(wumpusPlayer);
					showPerceptions(wumpusPlayer.whatAboutPerceptions());
					showOptions(wumpusPlayer);
					
					do {
						option= reader.next();
						try {
							optionNumber = Integer.parseInt(option);
						} catch(NumberFormatException e) {
							optionNumber = 0;
						}
						if(optionNumber <= 0 || optionNumber > 5) {
							System.out.println("Opción incorrecta");
							showOptions(wumpusPlayer);
						}
					} while (optionNumber <= 0 || optionNumber > 5);
					
					wumpusPlayer.doAction(Actions.values()[optionNumber-1]);
				}
				
			} while (Results.GO_ON.equals(result));
			
			System.out.println(result.getResultText());
			
			wumpusPlayer.init();

			showOptionsEnd();
			
			do {
				option = reader.next();
				try {
					optionNumber = Integer.parseInt(option);
				} catch(NumberFormatException e) {
					optionNumber = 0;
				}
				
				if(optionNumber <= 0 || optionNumber > 2) {
					System.out.println("Opción incorrecta");
					showOptionsEnd();
				}

			} while (optionNumber <= 0 || optionNumber > 2);			
			
		} while (optionNumber == 1);
		
		reader.close();
		
		System.out.println("Thanks for playing. See you soon");
	}

	private static void printHeader() {
		
		System.out.println(SEPARATOR_STAR);
		System.out.println("*   _   _ _   _ _   _ _____   _____ _   _ _____  __        ___   _ __  __ ____  _   _ ____    *");
		System.out.println("*  | | | | | | | \\ | |_   _| |_   _| | | | ____| \\ \\      / | | | |  \\/  |  _ \\| | | / ___|   *");
		System.out.println("*  | |_| | | | |  \\| | | |     | | | |_| |  _|    \\ \\ /\\ / /| | | | |\\/| | |_) | | | \\___ \\   *"); 
		System.out.println("*  |  _  | |_| | |\\  | | |     | | |  _  | |___    \\ V  V / | |_| | |  | |  __/| |_| |___) |  *");
		System.out.println("*  |_| |_|\\___/|_| \\_| |_|     |_| |_| |_|_____|    \\_/\\_/   \\___/|_|  |_|_|    \\___/|____/   *");
		System.out.println("*                                                                                             *");
		System.out.println("*                                                                                     v.1.0   *");
		System.out.println("*                                                                            by Jose Villel   *");
		System.out.println(SEPARATOR_STAR);
	}
	
	private static void printWelcome() {
		printHeader();
		System.out.println("*                                  ----- PRESS ENTER ------                                   *");
		System.out.println(SEPARATOR_STAR);
	}
	
	private static void showPerceptions(List<Perceptions> perceptions) {
		
		System.out.println(SEPARATOR_PAD);
		if(perceptions == null || perceptions.isEmpty()) {
			System.out.println("You do not perceive anything interesting...");
		} else {
			
			perceptions.stream().forEach( perception->System.out.println(perception.getPerceptionText()));
		}
		System.out.println(SEPARATOR_PAD);		
	}

	private static void showBoard(WumpusPlayer wumpusPlayer) {
		
		for (int x = 0; x < wumpusPlayer.getGameBoard().getDimensionX(); x++) {
			printLB(1);
			System.out.print("|");
		  for (int y = 0; y <  wumpusPlayer.getGameBoard().getDimensionY(); y++) {
			  Cell cell = wumpusPlayer.getGameBoard().getCellByPosition(x, y);
			  if(wumpusPlayer.getPositionX() == x && wumpusPlayer.getPositionY() == y) {
				  System.out.print(wumpusPlayer.getCurrentCourse().getRepresentativeChar());
			  }else {
				  System.out.print(" ");
			  }
			  if(cell.getContent().isPresent()) {
				  WumpusElements element = (WumpusElements) cell.getContent().get();
				  if(BEGGINER_MODE) {
					  System.out.print(element.getRepresentativeChar());
				  } else {
					  System.out.print(" ");
				  }
			  } else {
				  System.out.print(" ");
			  }

			  System.out.print("|");
		  }
		}
		printLB(2);
	}

	private static  void showOptions(WumpusPlayer wumpusPlayer) {
		
		System.out.println("Choose the option number and press ENTER:");
		System.out.println("1.- Go forward");
		System.out.println("2.- Turn right");
		System.out.println("3.- Turn left");
		System.out.println("4.- Shoot (" + wumpusPlayer.getArrows() +")");
		printLB(1);
		
	}
	
	private static void showOptionsEnd() {
		
		System.out.println("Choose the option number and press ENTER:");
		System.out.println("1.- Play again");
		System.out.println("2.- Exit");
		printLB(1);
		
	}
	
	private static void printLB(int times) {
		
		do {
			System.out.println("");
			times--;
		} while (times > 0);
	}
}
