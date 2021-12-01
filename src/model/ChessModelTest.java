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

}
