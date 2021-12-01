package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ChessModelTest {

	@Test
	void test() {
		String sendIn = null;
		ChessModel sim1 = new ChessModel(sendIn);
		String cmprString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
		assertEquals(cmprString, sim1.getFenString());
	}

}
