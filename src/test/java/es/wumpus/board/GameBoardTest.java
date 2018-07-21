package es.wumpus.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import es.wumpus.board.Cell;
import es.wumpus.board.GameBoard;
import es.wumpus.board.GameBoardException;

public class GameBoardTest {
	
	
	
	@Test(expected = GameBoardException.class)
	public void gameBoardThrowExceptionWhenHasWrongDimensions() throws GameBoardException {
		
		new GameBoard(0, 2);
	}
	
	@Test
	public void gameBoardWithDimensionNMHasNxMCells() throws GameBoardException {
		
		
		GameBoard gameBoard = new GameBoard(5,6);
		
		int totalCells = 5 * 6 ;
		
		assertEquals(totalCells, gameBoard.getTotalCells());
	}
	
	@Test
	public void gameBoardHasCells() throws GameBoardException {
		
		GameBoard gameBoard = new GameBoard();
		
		for (int x = 0; x < gameBoard.getDimensionX(); x++) {
			  for (int y = 0; y <  gameBoard.getDimensionY(); y++) {
				  assertNotNull(gameBoard.getCellByPosition(x,y));
			  }
		}
	}
	
	@Test
	public void gameBoardReturnConnectedCellsFromOneCell() throws GameBoardException {
		
		GameBoard gameBoard = new GameBoard(3,3);
		
		int j,k;
		
		for (j = 0; j < gameBoard.getDimensionX(); j++) {
			for (k = 0; k <  gameBoard.getDimensionY(); k++) {
				int numberOfConnectedCells=4;
				int x= j;
				int y = k;
				Cell oneCell = gameBoard.getCellByPosition(x,y);
				List<Cell> connectedCells = gameBoard.getConnectedCells(oneCell);
				
				if (oneCell.getPositionX() == 0 && (oneCell.getPositionY() ==0 || oneCell.getPositionY() ==gameBoard.getDimensionY()-1 )
					|| oneCell.getPositionX() == gameBoard.getDimensionX()-1 && (oneCell.getPositionY() == 0 || oneCell.getPositionY() ==gameBoard.getDimensionY()-1 )) {
					numberOfConnectedCells = 2;
				} else if(oneCell.getPositionX() == 0 || oneCell.getPositionX() == gameBoard.getDimensionX()-1
						|| oneCell.getPositionY() == 0 || oneCell.getPositionY() == gameBoard.getDimensionY()-1 ) {
					
					numberOfConnectedCells = 3;
				}
				
				assertTrue("GameBoard returns null/empty list or too many cells", connectedCells != null && connectedCells.size() == numberOfConnectedCells);		
				assertTrue("GameBoard returns cells not connected", connectedCells.stream().allMatch(cell -> cell.getPositionX() >=x-1 && cell.getPositionX() <=x+1 && cell.getPositionY() >=y-1 && cell.getPositionY() <=y+1));
			}
		}
	}

}
