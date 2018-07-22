package es.wumpus.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import es.wumpus.board.Cell;
import es.wumpus.board.GameBoard;
import es.wumpus.board.GameBoardException;
import es.wumpus.game.WumpusRules.Actions;
import es.wumpus.game.WumpusRules.Courses;
import es.wumpus.game.WumpusRules.Perceptions;
import es.wumpus.game.WumpusRules.Results;
import es.wumpus.game.WumpusRules.WumpusElements;

/**
 * 
 * Represents the player of the game, and it has the implementation of the rules of the game.
 * It needs a {@link GameBoard} and a {@link WumpusRules} to run.
 * It has information about the position of the player, number of arrows the player has, the current course, 
 * and if the player found the gold, kill the wumpus or shouted an arrow.
 * 
 * @author Jose Villel
 *
 */

public class WumpusPlayer {
	
	private GameBoard gameBoard;
	private WumpusRules rules;
	private int positionX;
	private int positionY;
	private int arrows;
	private Courses currentCourse; 
	private boolean goldCarrier;
	private boolean deadWumpus;
	private boolean arrowShooted;
	
	
	public WumpusPlayer() {

		this(new GameBoard(), new WumpusRules());	
	}

	public WumpusPlayer(GameBoard gameBoard, WumpusRules rules)  {
		
		setGameBoard(gameBoard);
		setRules(rules);
	}

	public int getPositionX() {
		return positionX;
	}
	public GameBoard getGameBoard() {
		return gameBoard;
	}

	public void setGameBoard(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
	}

	public WumpusRules getRules() {
		return rules;
	}

	public void setRules(WumpusRules rules) {
		this.rules = rules;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}
	public int getPositionY() {
		return positionY;
	}
	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
	public int getArrows() {
		return arrows;
	}
	public void setArrows(int arrows) {
		this.arrows = arrows;
	}
	public Courses getCurrentCourse() {
		return currentCourse;
	}
	public void setCurrentCourse(Courses currentCourse) {
		this.currentCourse = currentCourse;
	}
	
	public boolean isGoldCarrier() {
		return goldCarrier;
	}

	public void setGoldCarrier(boolean goldCarrier) {
		this.goldCarrier = goldCarrier;
	}


	public boolean isDeadWumpus() {
		return deadWumpus;
	}

	public void setDeadWumpus(boolean deadWumpus) {
		this.deadWumpus = deadWumpus;
	}

	public boolean isArrowShooted() {
		return arrowShooted;
	}

	public void setArrowShooted(boolean arrowShooted) {
		this.arrowShooted = arrowShooted;
	}
	
	/**
	 * Initialize the player
	 * @throws GameBoardException 
	 */
	public void init() throws GameBoardException {
		
		setPositionX(getGameBoard().getInitialPositionX());
		setPositionY(getGameBoard().getInitialPositionY());
		setArrows(rules.getNumberOfArrows());
		setCurrentCourse(rules.getInitialCourse());
		setGoldCarrier(false);
		setDeadWumpus(false);
		setArrowShooted(false);
		getGameBoard().create();
		putElementsOnBoard();
	}
	
	/**
	 * Put game elements on the board.
	 * @throws GameBoardException 
	 */
	public void putElementsOnBoard() throws GameBoardException {
		
		getGameBoard().putElementOnRandomCell(WumpusElements.WUMPUS);
		getGameBoard().putElementOnRandomCell(WumpusElements.GOLD);
		for (int i = 1; i <= rules.getNumberOfPits(); i++) {
			getGameBoard().putElementOnRandomCell(WumpusElements.PIT);
		}
	}

	/**
	 * Sets the player position in x,y
	 * @param x coordinates
	 * @param y coordinates
	 */
	public void moveTo(int x, int y) {
		setPositionX(x);
		setPositionY(y);
	}
	
	/**
	 * Analyzes perceptions based on the current position and connected cells to this position.
	 * @return List<{@link Perceptions}>
	 */
	public List<Perceptions> whatAboutPerceptions() {
		
		List<WumpusRules.Perceptions> perceptions = new ArrayList<>();
		Cell currentCell = gameBoard.getCellByPosition(getPositionX(), getPositionY());
		
		addPerceptionsFromConnectedCells(perceptions, currentCell);
		addPerceptionsFromCurrentCell(perceptions, currentCell);
		
		return perceptions;
	}
	
	/**
	 * Adds perceptions from one connected cell to the list of perceptions received
	 * @param perceptions {@link Perceptions}
	 * @param cell {@link Cell}
	 */
	private void addPerceptionsFromConnectedCells(List<Perceptions> perceptions, Cell currentCell) {
		
		List<Cell> connectedCells = gameBoard.getConnectedCells(currentCell);
		
		for(Cell cell : connectedCells) {
			
			if(cell.getContent().isPresent()) {
				
				switch ((WumpusElements) cell.getContent().get()) {
				
					case PIT:
						perceptions.add(Perceptions.BREEZE);
						break;
						
					case WUMPUS:
						perceptions.add(Perceptions.STENCH);
						break;
						
					default:
						break;
				}
			}
			
		}
		
		
		
	}
	
	/**
	 * Adds perceptions  to the list of perceptions received, from the cell where player is.
	 * @param perceptions {@link Perceptions}
	 * @param cell {@link Cell}
	 */
	private void addPerceptionsFromCurrentCell(List<Perceptions> perceptions, Cell cell) {
		
			
		if(cell.getContent().isPresent() && WumpusElements.GOLD.equals((WumpusElements) cell.getContent().get())) {
			
			perceptions.add(Perceptions.BRIGHTNESS);
			cell.setContent(Optional.empty());
		}
		
		
		if(cell.isInTheOutline()) {
			
			switch (getCurrentCourse()) {
			
				case NORTH:
					if(cell.getPositionX()==0) perceptions.add(Perceptions.SHOCK);
					break;
				case SOUTH:
					if(cell.getPositionX() == gameBoard.getDimensionX()-1) perceptions.add(Perceptions.SHOCK);
					break;
				case WEST:
					if(cell.getPositionY() == 0) perceptions.add(Perceptions.SHOCK);
					break;
				case EAST:
					if(cell.getPositionY() == gameBoard.getDimensionY()-1) perceptions.add(Perceptions.SHOCK);
					break;
			}
		}
		
		if(isDeadWumpus()) {
			setDeadWumpus(false);
			setArrowShooted(false);
			perceptions.add(Perceptions.SCREAM);
		} else if (isArrowShooted()) {
			setArrowShooted(false);
			perceptions.add(Perceptions.LAUGH);
		}
		
	}
	
	
	
	/**
	 * Does an action from {@link Actions}
	 * @param action
	 */
	public void doAction(Actions action) {
		
		Cell currentCell = getGameBoard().getCellByPosition(getPositionX(), getPositionY());
		
		switch (action) {
		
			case GO_FORWARD:
				
				doGoForward(currentCell);
				break;
			
			case TURN_LEFT:
				
				doTurnLeft();
				break;
			
			case TURN_RIGHT:
				
				doTurnRight();
				break;
			
			case SHOOT:
				doShoot();
				break;
				
			default:
				break;
		
		}
		
		
	}

	private void doGoForward(Cell currentCell) {
		switch (getCurrentCourse()) {
			case NORTH:
				if(currentCell.getPositionX()!= 0) {
					moveTo(currentCell.getPositionX()-1, currentCell.getPositionY());
				}
				break;
				
			case SOUTH:
				if(currentCell.getPositionX()!= getGameBoard().getDimensionX()-1 ) {
					moveTo(currentCell.getPositionX()+1, currentCell.getPositionY());
				}
				break;
				
			case WEST:
				if(currentCell.getPositionY()!= 0) {
					moveTo(currentCell.getPositionX(), currentCell.getPositionY()-1);
				}
				break;
				
			case EAST:
				if(currentCell.getPositionY()!= getGameBoard().getDimensionY()-1) {
					moveTo(currentCell.getPositionX(), currentCell.getPositionY()+1);
				}
				break;
		}
	}
	
	private void doTurnRight() {
		switch (getCurrentCourse()) {
			case NORTH:
				setCurrentCourse(Courses.EAST);
				break;
				
			case SOUTH:
				setCurrentCourse(Courses.WEST);
				break;
				
			case WEST:
				setCurrentCourse(Courses.NORTH);
				break;
				
			case EAST:
				setCurrentCourse(Courses.SOUTH);
				break;					
		}
	}

	private void doTurnLeft() {
		switch (getCurrentCourse()) {
			case NORTH:
				setCurrentCourse(Courses.WEST);
				break;
				
			case SOUTH:
				setCurrentCourse(Courses.EAST);
				break;
				
			case WEST:
				setCurrentCourse(Courses.SOUTH);
				break;
				
			case EAST:
				setCurrentCourse(Courses.NORTH);
				break;					
		}
	}


	/**
	 * Makes a shoot if player has arrows and determine if it has killed wumpus or not.
	 */
	public void doShoot() {
		
		Cell currentCell = getGameBoard().getCellByPosition(getPositionX(), getPositionY());
		Cell wumpusCell = getGameBoard().getCellByContent(WumpusElements.WUMPUS);
		boolean wumpusIsDead = false;
		
		if(getArrows()>0) {
			
			setArrows(getArrows()-1);
			setArrowShooted(true);
			
			if(wumpusCell != null) {

				switch (getCurrentCourse()) {
						
					case NORTH :
						wumpusIsDead = wumpusCell.getPositionY() == currentCell.getPositionY() && wumpusCell.getPositionX() < currentCell.getPositionX();
						break;
					case SOUTH :
						wumpusIsDead = wumpusCell.getPositionY() == currentCell.getPositionY() && wumpusCell.getPositionX() > currentCell.getPositionX();
						break;
					case EAST : 
						wumpusIsDead = wumpusCell.getPositionX() == currentCell.getPositionX() && wumpusCell.getPositionY() > currentCell.getPositionY();
						break;
					case WEST :
						wumpusIsDead = wumpusCell.getPositionX() == currentCell.getPositionX() && wumpusCell.getPositionY() < currentCell.getPositionY();
						break;
				}
				
				if(wumpusIsDead) {
					setDeadWumpus(true);
					wumpusCell.setContent(Optional.empty());
				}
			}
		}
		
	}
	
	/**
	 * Checks the goal and determine if the game can continue or if the player has lost or has won.
	 * @return {@link Result}
	 */
	public Results checkTheGoal() {
		
		Cell currentCell = getGameBoard().getCellByPosition(getPositionX(), getPositionY());
		Results result = Results.GO_ON;
		
		if(currentCell.getContent().isPresent()) {
			
			switch ( (WumpusElements) currentCell.getContent().get()) {
			
				case PIT:
					result = Results.LOSE;
					break;
				case WUMPUS:
					result = Results.LOSE_WUMPUS;
					break;
					
				case GOLD:
					setGoldCarrier(true);
			}
		}
		
		if (isGoldCarrier() 
			&& currentCell.getPositionX() == getGameBoard().getInitialPositionX() 
			&& currentCell.getPositionY() == getGameBoard().getInitialPositionY()) {
			
			result = Results.WIN;
		}
		
		return result;
	}

}
