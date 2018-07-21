package es.wumpus.board;

public class GameBoardException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1375711398777173975L;
	
	public static final String DIMENSION_WRONG_MSG = "Dimension inferior a 2";
	
	public GameBoardException(String msg) {
        super(msg);
    }

}
