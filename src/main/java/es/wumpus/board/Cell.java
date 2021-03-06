package es.wumpus.board;

import java.util.Optional;

/**
 * 
 * Represents a cell in a matrix. Their content must be extend from {@link BoardElement}.
 * It has information about their position and if it is in the outline of the matrix.
 * 
 * @author Jose Villel
 *
 */
public class Cell {
	
	private int positionX;
	private int positionY;
	private Optional<BoardElement> content;
	
	public Cell(int positionX, int positionY) {
		
		setPositionX(positionX);
		setPositionY(positionY);
		setContent(Optional.empty());
		
	}



	public int getPositionX() {
		return positionX;
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



	public  Optional<BoardElement> getContent() {
		return content;
	}

	public void setContent( Optional<BoardElement> content) {
		this.content = content;
	}


}
