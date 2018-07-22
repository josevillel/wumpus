package es.wumpus.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * 
 * 
 * Represents a game board in the form of a set of cells. Its cells are accessed through its x, y coordinates. 
 * Through the constructors it is possible to create the board with the default size, 
 * or configure its size and position from where it starts, as needed
 * 
 * @author Jose Villel
 *
 */
public class GameBoard {
	
	
	private static final int DEFAULT_DIMENSION_X = 4; 
	private static final int DEFAULT_DIMENSION_Y = 4; 
	
	private static final int RANDOM_CELL_SEARCH_MAX_ATTEMPS = 200;

	private int dimensionX;
	private int dimensionY;	
	private int initialPositionX;
	private int initialPositionY;
	private Cell[][] board;
	private int totalCells;
	private int totalElements;


	public GameBoard() throws GameBoardException  {
		
		this(DEFAULT_DIMENSION_X, DEFAULT_DIMENSION_Y);
		
	}
	public GameBoard(int dimensionX, int dimensionY) throws GameBoardException {

		this(dimensionX, dimensionY, dimensionX-1, 0);		
	}
	
	public GameBoard(int dimensionX, int dimensionY, int initialPositionX, int initialPositionY) throws GameBoardException {
		
		setDimensionX(dimensionX);
		setDimensionY(dimensionY);
		setInitialPositionX(initialPositionX);
		setInitialPositionY(initialPositionY);
		setTotalCells(0);
		setTotalElements(0);
		
		this.create();
		this.clear();
		
	}

	public int getDimensionX() {
		return dimensionX;
	}
	public void setDimensionX(int dimensionX) {
		this.dimensionX = dimensionX;
	}
	public int getDimensionY() {
		return dimensionY;
	}
	public void setDimensionY(int dimensionY) {
		this.dimensionY = dimensionY;
	}
	public int getInitialPositionX() {
		return initialPositionX;
	}
	public void setInitialPositionX(int initialPositionX) {
		this.initialPositionX = initialPositionX;
	}
	public int getInitialPositionY() {
		return initialPositionY;
	}
	public void setInitialPositionY(int initialPositionY) {
		this.initialPositionY = initialPositionY;
	}
	public Cell[][] getBoard() {
		return board;
	}
	public void setBoard(Cell[][] board) {
		this.board = board;
	}
	public int getTotalCells() {
		return totalCells;
	}
	public void setTotalCells(int totalCells) {
		this.totalCells = totalCells;
	}
	
	public int getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}
	/**
	 * Create a board as a matrix, and insert a {@link Cell}  in each element.
	 * @throws GameBoardException 
	 */
	public void create() throws GameBoardException {
		
		if(getDimensionX() < 3 || getDimensionY() < 3) {
			throw new GameBoardException(GameBoardException.DIMENSION_WRONG_MSG);
		}
		
		this.setBoard(new Cell[getDimensionX()][getDimensionY()]);
	
	}

	/**
	 * Look for a random cell that is empty, and puts a {@link BoardElement}  inside.
	 * @param element {@link BoardElement}  to insert.
	 * @throws GameBoardException 
	 */
	public void putElementOnRandomCell(BoardElement element) throws GameBoardException {
		
		Random r = new Random();
		int x;
		int y;
		int cont = 0;
		Cell cell;
		
		if(getTotalElements() == getTotalCells()-1) {
			throw new GameBoardException(GameBoardException.ELEMENTS_EXCEED_CELLS_MSG);
		}
			
		do {
			x = r.nextInt(this.getDimensionX());
			y = r.nextInt(this.getDimensionY());
			
			cell = this.getCellByPosition(x,y);
			cont++;

		} while (cont < RANDOM_CELL_SEARCH_MAX_ATTEMPS && (cell.getContent().isPresent() || (x == getInitialPositionX() && y==getInitialPositionY())));
		
		if(cont==RANDOM_CELL_SEARCH_MAX_ATTEMPS) {
			throw new GameBoardException(GameBoardException.RANDOM_CELL_SEARCH_EXCEED_LIMIT_MSG);
		}
		putElementOnCell(element, x, y);
			
	}
	
	/**
	 * Puts a {@link BoardElement} in a cell with coordinates x,y
	 * @param element {@link BoardElement} to insert.
	 * @param x coordinate
	 * @param y coordinate
	 */
	public void putElementOnCell(BoardElement element, int x, int y) {
		
		this.getCellByPosition(x,y).setContent(Optional.of(element));
		setTotalElements(getTotalElements()+1);
		
	}

	/**
	 * Returns the cell in the position x,y
	 * @param x coordinate
	 * @param y coordinate
	 * @return {@link Cell}
	 */
	public Cell getCellByPosition(int x, int y) {
		return this.getBoard()[x][y];
	}
	
	/**
	 * Returns a cell with the content equal to the received {@link BoardElement}	 
	 * @param element  {@link BoardElement}  to search
	 * @return  {@link Cell} found or null Cell
	 */
	public Cell getCellByContent(BoardElement element) {
		
		Cell cell = null;		
		int x = 0;
		int y = 0;
		Boolean found = false;
		
        while (x < getDimensionX() && !found) {
        	
        	Cell candidate = getCellByPosition(x, y);
        	
        	found = (candidate.getContent().isPresent() && candidate.getContent().get().equals(element));
            
        	if (y == getDimensionY() - 1) {
                x++;
                y = 0;
            } else {
                y++;
            }
        	
        	if (found) cell = candidate;
        }
        
        return cell;

	}
	
	/**
	 * Sets a cell in the board with their coordinates.;
	 * @param {@link Cell}
	 */
	public void setCell(Cell cell) {
		
		getBoard()[cell.getPositionX()][cell.getPositionY()] = cell;
	}
	
	/**
	 * Returns a list with the connected cells. 
	 * Connected cells are the cells above, down, left or down, if there are
	 * @param {@link Cell} look for the connected cells from this cell
	 * @return List<Cell> list of connected cells
	 */
	public List<Cell> getConnectedCells(Cell cell) {

		List<Cell> connectedCells = new ArrayList<>();
		
		int x = cell.getPositionX();
		int y = cell.getPositionY();
		
		if (x!=0) {
			connectedCells.add(getCellByPosition(x-1,y));
		}
		
		if (x!=getDimensionX()-1) {
			connectedCells.add(getCellByPosition(x+1,y));
		}
		
		if (y!=0) {
			connectedCells.add(getCellByPosition(x,y-1));
		}
		
		if (y!=getDimensionY()-1) {
			connectedCells.add(getCellByPosition(x,y+1));
		}
		
		return connectedCells;
		
	}
	
	public void clear() {
		
		int cont =0;
		
		for (int x = 0; x < getDimensionX(); x++) {
			  for (int y = 0; y <  getDimensionY(); y++) {
				  boolean isInTheOutline = (x==0 || x== dimensionX-1 || y==0 || y == dimensionY-1);
				  setCell(new Cell(x,y, isInTheOutline));
				  cont++; 
			  }
		}
		
		setTotalCells(cont);
		
	}

}
