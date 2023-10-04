package logic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GuessEvaluatorTest {
  @Test
  public void testEvaluateGuess() {
    assertEquals(GuessEvaluator.evaluateGuess("WHILE","CODES"),"---Y-");
    assertEquals(GuessEvaluator.evaluateGuess("WHILE","PLANT"),"-Y---");
    assertEquals(GuessEvaluator.evaluateGuess("WHILE","GELLY"),"-Y-G-");
    assertEquals(GuessEvaluator.evaluateGuess("WHILE","WHILE"),"GGGGG");
  }
}