package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import controller.ChessController;

class ChessModelTest {

	@Test
	void testCreateNewGame1() {
		System.out.println("Creating New Game:");
		ChessController controller = new ChessController();
		String cmprString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
		System.out.println(controller.getFenString());
		assertEquals(cmprString, controller.getFenString());
		System.out.println();
	}
	
	@Test
	void testCreateExistingGame1() {
		System.out.println("Creating Existing Game 1:");
		ChessController controller = new ChessController("rnbqkbnr/ppppppp1/7p/8/8/8/PPPPPPPP/RNBQKBNR");
		String cmprString = "rnbqkbnr/ppppppp1/7p/8/8/8/PPPPPPPP/RNBQKBNR";
		System.out.println(controller.getFenString());
		assertEquals(cmprString, controller.getFenString());
		System.out.println();
	}
	
	@Test
	void testCreateExistingGame2() {
		System.out.println("Creating Existing Game 2:");
		ChessController controller = new ChessController("rnbqkbnr/1ppppppp/p7/8/8/8/PPPPPPPP/RNBQKBNR");
		String cmprString = "rnbqkbnr/1ppppppp/p7/8/8/8/PPPPPPPP/RNBQKBNR";
		System.out.println(controller.getFenString());
		assertEquals(cmprString, controller.getFenString());
		System.out.println();
	}
	
	@Test
	void testCreateExistingGame3() {
		System.out.println("Creating Existing Game 3:");
		ChessController controller = new ChessController("rnbqkbnr/ppp1pppp/3p4/8/8/8/PPPPPPPP/RNBQKBNR");
		String cmprString = "rnbqkbnr/ppp1pppp/3p4/8/8/8/PPPPPPPP/RNBQKBNR";
		System.out.println(controller.getFenString());
		assertEquals(cmprString, controller.getFenString());
		System.out.println();
	}
	
	@Test
	void testCreateNewGame2() {
		System.out.println("Creating New Game With One Move:");
		ChessController controller = new ChessController();
		String cmprString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
		System.out.println("Before Move: ");
		System.out.println(controller.getFenString());
		assertEquals(cmprString, controller.getFenString());
		controller.makePlayerMove(new Move(1, 0), new Move(2, 0));
		System.out.println("After Move (h7 -> h6): ");
		System.out.println(controller.getFenString());
		System.out.println();
		
	}
	
	@Test
	void testCreateNewGame3() {
		System.out.println("Creating New Game With More Than One Move:");
		ChessController controller = new ChessController();
		String cmprString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
		System.out.println("Before Move: ");
		System.out.println(controller.getFenString());
		assertEquals(cmprString, controller.getFenString());
		controller.makePlayerMove(new Move(6, 1), new Move(4, 1));
		System.out.println("After Move (g2 -> g4): ");
		System.out.println(controller.getFenString());
		controller.makePlayerMove(new Move(1, 0), new Move(3, 0));
		System.out.println("After Move (h7 -> h5): ");
		System.out.println(controller.getFenString());
		System.out.println();
	}
	
	@Test
	void testCreateNewGame4() {
		System.out.println("Creating New Game With More Than One Move:");
		ChessController controller = new ChessController();
		String cmprString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
		System.out.println("Before Move: ");
		System.out.println(controller.getFenString());
		assertEquals(cmprString, controller.getFenString());
		controller.makePlayerMove(new Move(6, 1), new Move(4, 1));
		System.out.println("After Move (g2 -> g4): ");
		System.out.println(controller.getFenString());
		controller.makePlayerMove(new Move(1, 0), new Move(3, 0));
		System.out.println("After Move (h7 -> h5): ");
		System.out.println(controller.getFenString());
		controller.makePlayerMove(new Move(4, 1), new Move(3, 0));
		System.out.println("After Move (g4 -> h5): ");
		System.out.println(controller.getFenString());
		System.out.println();
	}

}
