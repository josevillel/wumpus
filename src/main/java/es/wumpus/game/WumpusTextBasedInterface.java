package es.wumpus.game;

import java.util.List;
import java.util.Scanner;

import es.wumpus.board.Cell;
import es.wumpus.board.GameBoard;
import es.wumpus.board.GameBoardException;
import es.wumpus.game.WumpusRules.*;

public class WumpusTextBasedInterface {
	
	private static final String WRONG_VALUE_TEXT = "Wrong value.";
	private static final String WRONG_OPTION_TEXT = "Wrong option.";
	private static final String CHOOSE_OPTION_TEXT = "Choose the option number and press ENTER:";
	private static final String SEPARATOR_PAD = "################################################";
	private static final String SEPARATOR_STAR = "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *";
	
	private static boolean begginerMode=false;
	
	private WumpusTextBasedInterface(){}


	public static boolean isBegginerMode() {
		return begginerMode;
	}


	public static void setBegginerMode(boolean begginerMode) {
		WumpusTextBasedInterface.begginerMode = begginerMode;
	}


	/**
	 * Starts the game 
	 * @param wumpusPlayer {@link WumpusPlayer}
	 * @throws Exception 
	 */
	public static void startGame() {
	
		Scanner reader = new Scanner (System.in);
		WumpusPlayer wumpusPlayer = new WumpusPlayer();
		boolean goOn = false;
		
		do {
			try {
				
				printWelcome();
				printWelcomeSettings();
				goOn = handleConfigOptions(reader, wumpusPlayer);
				
			} catch (GameBoardException gbex) {
				System.err.println(gbex.getMessage());
				goOn = false;
			}
		} while (!goOn);
		
		do {
			try {
				goOn = handleGameOptions(reader, wumpusPlayer);
			} catch (GameBoardException gbex) {
				System.err.println(gbex.getMessage());
				goOn = false;
			}
		} while (!goOn);
		
		reader.close();		
		System.out.println("Thanks for playing. See you soon");


	}
	
	private static boolean handleConfigOptions(Scanner reader, WumpusPlayer player) throws GameBoardException {

		
		String option;
		int optionNumber = 0;

		do {
			System.out.println(CHOOSE_OPTION_TEXT);
			System.out.println("1.- Default settings (Board size 4x4 | Number of pits: 3 | Number of arrows: 1)");
			System.out.println("2.- Custom settings");
			printLines(1);
			
			option = reader.next();
			try {
				optionNumber = Integer.parseInt(option);
			} catch(NumberFormatException e) {
				optionNumber = 0;
			}
			
			if(optionNumber <= 0 || optionNumber > 2) {
				System.out.println(WRONG_OPTION_TEXT);
			}

		} while (optionNumber <= 0 || optionNumber > 2);
		
		if(optionNumber==2) {
			handleCustomConfigOptions(reader, player);
		}
		
		player.init();
		
		return true;
		
	}

	private static boolean handleGameOptions(Scanner reader, WumpusPlayer wumpusPlayer) throws GameBoardException {
		
		Results result;
		int optionNumber = 0;
		
		do {
			
			do {
				printLines(50);
				printHeader();
				result = playGame(reader, wumpusPlayer);
				
			} while (Results.GO_ON.equals(result));
			
			System.out.println(result.getResultText());
			
			wumpusPlayer.init();

			optionNumber = handleOptionsEnd(reader);	
			
		} while (optionNumber == 1);
		
		return true;
		
	}

	private static Results playGame(Scanner reader, WumpusPlayer player) {

		String option;
		int optionNumber = 0;
		Results result = player.checkTheGoal();
		
		if(Results.GO_ON.equals(result)) {
			
			showBoard(player);
			showPerceptions(player.whatAboutPerceptions());
			
			do {
				
				System.out.println(CHOOSE_OPTION_TEXT);
				System.out.println("1.- Go forward");
				System.out.println("2.- Turn right");
				System.out.println("3.- Turn left");
				System.out.println("4.- Shoot (" + player.getArrows() +")");
				printLines(1);
				option= reader.next();
				try {
					optionNumber = Integer.parseInt(option);
				} catch(NumberFormatException e) {
					optionNumber = 0;
				}
				if(optionNumber <= 0 || optionNumber > Actions.values().length) {
					System.out.println(WRONG_OPTION_TEXT);
				}
			} while (optionNumber <= 0 || optionNumber > Actions.values().length);
			
			player.doAction(Actions.values()[optionNumber-1]);
		}
		
		return result;
	}


	
	private static void handleCustomConfigOptions(Scanner reader,  WumpusPlayer player) {

		handleCustomBoardSize(reader, player);
		handleCustomNumberOfPits(reader, player);
		handleCustomNumberOfArrows(reader,player);
		handleCustomBegginerMode(reader);
		
	}

	private static void handleCustomBegginerMode(Scanner reader) {
		
		String input;
		
		char result='-';
		
		do {
			System.out.println("GAME MODE");	
			System.out.print("Do you want to activate the BEGGINER mode? S/N and press ENTER");	
			input = reader.next();
			if(input != null) {
				result = input.charAt(0);
			}
			
			if(result != 'S'  && result != 'N' ) {
				System.out.println(WRONG_VALUE_TEXT);
			}

		} while (result != 'S'  && result != 'N');
		
		if(result=='S') {
			setBegginerMode(true);
		}
	}

	private static void handleCustomBoardSize(Scanner reader, WumpusPlayer player) {
		
		String input;
		int x=-1;
		int y=-1;
		boolean ok=false;
		
		do {
			System.out.println("BOARD SIZE");	
			System.out.print("Give me two numbers separated by comma (x,y) and press ENTER:");	
			input = reader.next();
			try {
				if(input.indexOf(',') > -1) {
					String[] strings = input.split(",");
					if(strings.length == 2) {
						x = Integer.parseInt(strings[0]);
						y = Integer.parseInt(strings[1]);
						ok=true;
					}
				}

			} catch(NumberFormatException e) {
				ok = false;
			}
			
			if(!ok || x < 0 || y < 0) {
				System.out.println(WRONG_VALUE_TEXT);
			}

		} while (!ok || x < 0 || y < 0);
		
		player.setGameBoard(new GameBoard(x, y));
		
	}
	
	private static void handleCustomNumberOfPits(Scanner reader, WumpusPlayer player) {
		
		String input;
		int numberOfPits=-1;
		boolean ok= false;
		
		do {
			System.out.println("NUMBER OF PITS");	
			System.out.print("Give me a number and press ENTER:");	
			input = reader.next();
			try {

				numberOfPits = Integer.parseInt(input);
				ok = true;
				
			} catch(NumberFormatException e) {
				ok = false;
			}
			
			if(!ok || numberOfPits < 0) {
				System.out.println(WRONG_VALUE_TEXT);
			}

		} while (!ok || numberOfPits < 0);
		
		player.getRules().setNumberOfPits(numberOfPits);
		
	}
	
	private static void handleCustomNumberOfArrows(Scanner reader, WumpusPlayer player) {
		
		String input;
		int numberOfArrows=-1;
		boolean ok= false;
		
		do {
			System.out.println("NUMBER OF ARROWS");	
			System.out.print("Give me a number and press ENTER:");	
			input = reader.next();
			try {

				numberOfArrows = Integer.parseInt(input);
				ok = true;
				
			} catch(NumberFormatException e) {
				ok = false;
			}
			
			if(!ok || numberOfArrows < 0) {
				System.out.println(WRONG_VALUE_TEXT);
			}

		} while (!ok || numberOfArrows < 0);
		
		player.getRules().setNumberOfArrows(numberOfArrows);
		
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
			printLines(1);
			System.out.print("|");
		  for (int y = 0; y <  wumpusPlayer.getGameBoard().getDimensionY(); y++) {
			  Cell cell = wumpusPlayer.getGameBoard().getCellByPosition(x, y);
			  char elementForPrint = ' ';
			  char cursorForPrint = ' ';
			  
			  if(wumpusPlayer.getPositionX() == x && wumpusPlayer.getPositionY() == y) {
				  cursorForPrint = wumpusPlayer.getCurrentCourse().getRepresentativeChar();
			  }

			  if(cell.getContent().isPresent()) {
				  WumpusElements element = (WumpusElements) cell.getContent().get();
				  if(isBegginerMode()) {
					  elementForPrint = element.getRepresentativeChar();
				  }
			  }

			  System.out.print(""+cursorForPrint + elementForPrint);
			  System.out.print("|");
		  }
		}
		printLines(2);
	}
	
	private static int handleOptionsEnd(Scanner reader) {
		
		String option;
		int optionNumber = 0;

		do {
			
			System.out.println(CHOOSE_OPTION_TEXT);
			System.out.println("1.- Play again");
			System.out.println("2.- Exit");

			printLines(1);
			option = reader.next();
			
			try {
				optionNumber = Integer.parseInt(option);
			} catch(NumberFormatException e) {
				optionNumber = 0;
			}
			
			if(optionNumber <= 0 || optionNumber > 2) {
				System.out.println(WRONG_OPTION_TEXT);
			}

		} while (optionNumber <= 0 || optionNumber > 2);
		
		return optionNumber;
		
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
	}
	
	private static void printWelcomeSettings() {
		System.out.println(SEPARATOR_STAR);
		System.out.println("*                                 ----- GAME SETTINGS ------                                  *");
		System.out.println(SEPARATOR_STAR);
	}
	
	private static void printLines(int times) {
		
		do {
			System.out.println("");
			times--;
		} while (times > 0);
	}
}
