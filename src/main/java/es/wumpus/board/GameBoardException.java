package es.wumpus.board;

public class GameBoardException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1375711398777173975L;
	
	public static final String DIMENSION_WRONG_MSG = "The stablished dimension is wrong";
	public static final String RANDOM_CELL_SEARCH_EXCEED_LIMIT_MSG = "The maximum number of attempts allowed to find an empty cell has been exceeded";
	
	public GameBoardException(String msg) {
        super(msg);
    }

}
