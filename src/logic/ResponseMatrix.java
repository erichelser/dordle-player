package logic;

import java.util.HashMap;
import java.util.Map;

public class ResponseMatrix {
  private Map<String, Map<String, String>> responseLookup;

  public ResponseMatrix(Dictionary allowedGuesses, Dictionary solutionWords) {
    responseLookup = new HashMap<>();
    for (String allowedGuess : allowedGuesses.getWordList()) {
      Map<String, String> lookupRow = new HashMap<>();
      for (String solutionWord : solutionWords.getWordList()) {
        lookupRow.put(solutionWord, GuessEvaluator.evaluateGuess(solutionWord, allowedGuess));
      }
      responseLookup.put(allowedGuess, lookupRow);
    }
  }

  public String lookup(String guess, String solution) {
    return responseLookup.get(guess).get(solution);
  }
}
