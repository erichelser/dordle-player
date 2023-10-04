package logic;

import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ResponseMatrixTest {
  @Test
  public void testLookup() {
    String[] guessList = {"LOREM", "IPSUM", "DOLOR", "SITAM", "ETETC"};
    String[] solutionList = {"PRIDE", "WRATH", "SLOTH", "GREED"};

    Dictionary guesses = new Dictionary(List.of(guessList));
    guesses.addWordsFromDictionary(Set.of(solutionList));

    Dictionary solutions = new Dictionary(List.of(solutionList));

    ResponseMatrix responseMatrix = new ResponseMatrix(guesses, solutions);

    for (String guess : guessList) {
      for (String solution : solutionList) {
        assertEquals(responseMatrix.lookup(guess, solution),
                GuessEvaluator.evaluateGuess(solution, guess));
      }
    }
  }
}