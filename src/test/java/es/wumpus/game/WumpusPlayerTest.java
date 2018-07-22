package es.wumpus.game;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import es.wumpus.board.Cell;
import es.wumpus.board.GameBoard;
import es.wumpus.board.GameBoardException;
import es.wumpus.game.WumpusRules.Actions;
import es.wumpus.game.WumpusRules.Courses;
import es.wumpus.game.WumpusRules.Perceptions;
import es.wumpus.game.WumpusRules.Results;
import es.wumpus.game.WumpusRules.WumpusElements;

public class WumpusPlayerTest {
	
	private WumpusRules rules;
	private GameBoard board;
	private WumpusPlayer player;

	
	@Before
	public void onceExecutedBeforeAll() throws GameBoardException {
		rules = new WumpusRules();
		board = new GameBoard(4,4);
		player = new WumpusPlayer(board , rules);
		player.init();
		
	}
	
	@Test
	public void gameHasPlayerReady()  {
		
		assertTrue("Player does not have arrows", player.getArrows() > 0);
		assertTrue("Player is not located at the exit", board.getInitialPositionX() == player.getPositionX() && board.getInitialPositionY() == player.getPositionY());
		assertNotNull("Player does not have current course", player.getCurrentCourse());
	}
	
	@Test
	public void gameHasRules()  {
		
		WumpusRules rules = player.getRules();
		
		assertNotNull(rules);
	}
	
	@Test
	public void gameHasWumpus() throws GameBoardException {

		player.putElementsOnBoard();
		Cell cell = board.getCellByContent(WumpusElements.WUMPUS);
		
		assertTrue("WUMPUS is not on the board",WumpusElements.WUMPUS.equals(cell.getContent().orElse(null)));
	}
	
	@Test
	public void gameHasGold() throws GameBoardException {

		player.putElementsOnBoard();
		Cell cell = board.getCellByContent(WumpusElements.GOLD);
		
		assertTrue("GOLD is not on the board",WumpusElements.GOLD.equals(cell.getContent().orElse(null)));
	}
	
	
	@Test
	public void playerIsNearAPitAndPerceiveABreeze() {

		board.putElementOnCell(WumpusElements.PIT, 0,1);
		player.moveTo(0,0);
		List<Perceptions> perceptions = player.whatAboutPerceptions();
		
		assertTrue("Player does not perceive a breeze", perceptions.stream().anyMatch(perception -> Perceptions.BREEZE.equals(perception)));
	
	}
	
	@Test
	public void playerIsNearTheWumpusAndPerceiveAStench() {

		board.putElementOnCell(WumpusElements.WUMPUS, 0,1);
		player.moveTo(0,0);
		List<Perceptions> perceptions = player.whatAboutPerceptions();
		
		assertTrue("Player does not perceive a stench", perceptions.stream().anyMatch(perception -> Perceptions.STENCH.equals(perception)));
	
		
	}
	
	@Test
	public void playerFoundGoldAndPerceiveABrightness(){

		board.putElementOnCell(WumpusElements.GOLD, 0,0);
		player.moveTo(0,0);
		List<Perceptions> perceptions = player.whatAboutPerceptions();
		
		assertTrue("Player does not perceive a brightness", perceptions.stream().anyMatch(perception -> Perceptions.BRIGHTNESS.equals(perception)));
	
	}
	
	@Test
	public void playerKillWumpusAndHearAScream() {

		board.putElementOnCell(WumpusElements.WUMPUS, 0,3 );
		player.moveTo(0,0);
		player.setCurrentCourse(Courses.EAST);
		player.doShoot();
		
		List<Perceptions> perceptions = player.whatAboutPerceptions();
		
		assertTrue("Player does not hear a scream", perceptions.stream().anyMatch(perception -> Perceptions.SCREAM.equals(perception)));
	}
	
	@Test
	public void playerGoForwardToAWallAndPerceiveAShock() {

		player.moveTo(1,0);
		player.setCurrentCourse(Courses.NORTH);
		player.doAction(Actions.GO_FORWARD);
		
		List<Perceptions> perceptions = player.whatAboutPerceptions();
		
		assertTrue("Player does not perceive a shock", perceptions.stream().anyMatch(perception -> Perceptions.SHOCK.equals(perception)));
	}
	
	@Test
	public void playerFoundTheWumpusAndPlayerLoses() {

		board.putElementOnCell(WumpusElements.WUMPUS, 0,0);
		player.moveTo(1,0);
		player.setCurrentCourse(Courses.NORTH);
		player.doAction(Actions.GO_FORWARD);
		
		assertTrue("Player found WUMPUS and does not lose", Results.LOSE_WUMPUS.equals(player.checkTheGoal()));
		
	}
	
	@Test
	public void playerFoundAPitAndPlayerLoses() {

		board.putElementOnCell(WumpusElements.PIT, 0,0);
		player.moveTo(1,0);
		player.setCurrentCourse(Courses.NORTH);
		player.doAction(Actions.GO_FORWARD);
		
		assertTrue("Player found PIT and does not lose", Results.LOSE.equals(player.checkTheGoal()));
		
	}
	
	

	@Test
	public void playerReturnsWithGoldAndWin () {

		
		board.setInitialPositionX(0);
		board.setInitialPositionY(0);
		player.moveTo(0,0);
		board.putElementOnCell(WumpusElements.GOLD, 0,1);
		player.setCurrentCourse(Courses.EAST);
		
		player.doAction(Actions.GO_FORWARD);
		player.checkTheGoal();
		player.doAction(Actions.TURN_LEFT);
		player.doAction(Actions.TURN_LEFT);
		player.doAction(Actions.GO_FORWARD);
		
		assertTrue("Player returns with GOLD and does not win", player.isGoldCarrier() &&  Results.WIN.equals(player.checkTheGoal()));
		
		
	}

}
