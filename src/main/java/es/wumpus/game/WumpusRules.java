package es.wumpus.game;

import es.wumpus.board.BoardElement;

public class WumpusRules {
	
	
	public static final int DEFAULT_NUMBER_OF_ARROWS = 1;
	
	public static final int DEFAULT_NUMBER_OF_PITS = 3;
	
	public static final Courses DEFAULT_INITIAL_COURSE = Courses.EAST;
	
	public enum WumpusElements implements BoardElement {
		GOLD ('$'),
		PIT ('O'),
		WUMPUS ('@');
		
		private char representativeChar; 
		private WumpusElements(char representativeChar) {
			this.representativeChar=representativeChar;
		}
		public char getRepresentativeChar() { return this.representativeChar; }
	}

	public enum Perceptions {
		STENCH("You smell STENCH. Wumpus is near..."),
		BREEZE("You feel a light BREEZE. Beware of pits... "),
		BRIGHTNESS("The BRIGHTNESS blinds you. Come back fast with gold..."), 
		SHOCK("SHOCK! You found a Wall. Change your course..."),  
		SCREAM("Someone has thrown a SCREAM. Your shot killed the WUMPUS..."), 
		LAUGH("You hear a LAUGH in the distance. Aim better next time...");
		
		private String perceptionText;
		
		private Perceptions(String perceptionText) {
			this.perceptionText=perceptionText;
		}
		
		public String getPerceptionText() {return this.perceptionText;}
	}

	public enum Actions {
	    GO_FORWARD, TURN_RIGHT, TURN_LEFT, SHOOT
	}
	
	public enum Courses {
		
		NORTH("^"), 
		EAST(">"), 
		SOUTH("v"), 
		WEST("<");
		
		private String representativeChar; 
		
		private Courses(String representativeChar) {

			this.representativeChar=representativeChar;
		}
		public String getRepresentativeChar() { return this.representativeChar; }
	}
	
	public enum Results {
		WIN("YOU WIN!!"),
		LOSE("You have fallen into a PIT. YOU LOSE!! "),
		LOSE_WUMPUS("You have found the WUMPUS... YOU LOSE!!"),
		GO_ON("You can continue...");
		
		private String resultText;
		
		private Results(String resultText) {
			this.resultText=resultText;
		}
		
		public String getResultText() {return this.resultText;}
	}
	
	public WumpusRules() {
		
		this.setNumberOfArrows(DEFAULT_NUMBER_OF_ARROWS);
		this.setNumberOfPits(DEFAULT_NUMBER_OF_PITS);
		this.setInitialCourse(DEFAULT_INITIAL_COURSE);
		
	}
	
	private int numberOfArrows;
	private int numberOfPits;
	private Courses initialCourse;
	
	

	public int getNumberOfArrows() {
		return numberOfArrows;
	}
	public void setNumberOfArrows(int numberOfArrows) {
		this.numberOfArrows = numberOfArrows;
	}
	public int getNumberOfPits() {
		return numberOfPits;
	}
	public void setNumberOfPits(int numberOfPits) {
		this.numberOfPits = numberOfPits;
	}
	public Courses getInitialCourse() {
		return initialCourse;
	}
	public void setInitialCourse(Courses initialCourse) {
		this.initialCourse = initialCourse;
	}

	
	
	
	
	
	
	
}
