package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import controller.ChessController;

class ChessModelTest {

	@Test
	void testCreateNewGame() {
		ChessController controller = new ChessController();
		String cmprString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
		System.out.println(controller.getFenString());
		assertEquals(cmprString, controller.getFenString());
	}
	
	@Test
	void testCreateExistingGame1() {
		ChessController controller = new ChessController("rnbqkbnr/ppppppp1/7p/8/8/8/PPPPPPPP/RNBQKBNR");
		String cmprString = "rnbqkbnr/ppppppp1/7p/8/8/8/PPPPPPPP/RNBQKBNR";
		System.out.println(controller.getFenString());
		assertEquals(cmprString, controller.getFenString());
	}
	
	@Test
	void testCreateExistingGame2() {
		ChessController controller = new ChessController("rnbqkbnr/1ppppppp/p7/8/8/8/PPPPPPPP/RNBQKBNR");
		String cmprString = "rnbqkbnr/1ppppppp/p7/8/8/8/PPPPPPPP/RNBQKBNR";
		System.out.println(controller.getFenString());
		assertEquals(cmprString, controller.getFenString());
	}
	
	@Test
	void testCreateExistingGame3() {
		ChessController controller = new ChessController("rnbqkbnr/ppp1pppp/3p4/8/8/8/PPPPPPPP/RNBQKBNR");
		String cmprString = "rnbqkbnr/ppp1pppp/3p4/8/8/8/PPPPPPPP/RNBQKBNR";
		System.out.println(controller.getFenString());
		assertEquals(cmprString, controller.getFenString());
	}

}
