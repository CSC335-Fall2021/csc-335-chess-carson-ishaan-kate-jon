package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ChessModelTest {

	@Test
	void test() {
		String sendIn = null;
		ChessModel sim1 = new ChessModel(sendIn);
		String cmprString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
		System.out.println(sim1.getFenString());
		assertEquals(cmprString, sim1.getFenString());
	}

}
