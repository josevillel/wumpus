package es.wumpus.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

public class GameBoardTest {
	
	public enum ElementsTest implements BoardElement {
		TEST
	}
	
	
	@Test(expected = GameBoardException.class)
	public void gameBoardThrowExceptionWhenHasWrongDimensions() throws GameBoardException {
		
		GameBoard gameBoard = new GameBoard(0, 2);
		gameBoard.create();
		
	}
	
	@Test(expected = GameBoardException.class)
	public void gameBoardThrowExceptionWhenSearchRandomCellExceedLimit() throws GameBoardException {
		
		GameBoard gameBoard = new GameBoard(3, 3);
		gameBoard.create();
		
		for (int x = 0; x < gameBoard.getDimensionX(); x++) {
			  for (int y = 0; y <  gameBoard.getDimensionY(); y++) {
				 Cell cell = gameBoard.getCellByPosition(x, y);
				 cell.setContent(Optional.of(ElementsTest.TEST));
			  }
		}
		
		gameBoard.putElementOnRandomCell(ElementsTest.TEST);
	}
	
	
	@Test
	public void gameBoardWithDimensionNMHasNxMCells() throws GameBoardException {
		
		
		GameBoard gameBoard = new GameBoard(5,6);
		gameBoard.create();
		
		int totalCells = 5 * 6 ;
		
		assertEquals(totalCells, gameBoard.getTotalCells());
	}
	
	@Test
	public void gameBoardHasCells() throws GameBoardException {
		
		GameBoard gameBoard = new GameBoard();
		gameBoard.create();
		
		for (int x = 0; x < gameBoard.getDimensionX(); x++) {
			  for (int y = 0; y <  gameBoard.getDimensionY(); y++) {
				  assertNotNull(gameBoard.getCellByPosition(x,y));
			  }
		}
	}
	
	@Test
	public void gameBoardReturnConnectedCellsFromOneCell() throws GameBoardException {
		
		GameBoard gameBoard = new GameBoard(3,3);
		gameBoard.create();
		
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
