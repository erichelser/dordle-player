package logic;

import java.util.ArrayList;
import java.util.List;

public class GuessEvaluator {

  private static GuessEvaluator singleton;

  public static GuessEvaluator getSingleton() {
    if (singleton == null) {
      singleton = new GuessEvaluator();
    }
    return singleton;
  }

  public String evaluateGuess(String answer, String guess) {
    char[] letterResponses = new char[answer.length()];
    boolean[] answerLettersMatched = new boolean[answer.length()];

    markCorrectLetters(letterResponses, answerLettersMatched, answer, guess);
    markPresentLetters(letterResponses, answerLettersMatched, answer, guess);
    markMissingLetters(letterResponses);

    return new String(letterResponses);
  }

  private void markCorrectLetters(
          char[] letterResponses, boolean[] answerLettersMatched, String answer, String guess) {
    for (int i = 0; i < guess.length() && i < answer.length(); i++) {
      if (guess.charAt(i) == answer.charAt(i)) {
        letterResponses[i] = LetterResponse.GREEN;
        answerLettersMatched[i] = true;
      }
    }
  }

  private void markPresentLetters(
          char[] letterResponses, boolean[] answerLettersMatched, String answer, String guess) {
    for (int guessIndex = 0; guessIndex < guess.length(); guessIndex++) {
      if (letterResponses[guessIndex] != 0) {
        continue;
      }
      for (int answerIndex = 0; answerIndex < answer.length(); answerIndex++) {
        if (answerLettersMatched[answerIndex]) {
          continue;
        }
        if (guess.charAt(guessIndex) == answer.charAt(answerIndex)) {
          answerLettersMatched[answerIndex] = true;
          letterResponses[guessIndex] = LetterResponse.YELLOW;
          break;
        }
      }
    }
  }

  private void markMissingLetters(char[] letterResponses) {
    for (int responseIndex = 0; responseIndex < letterResponses.length; responseIndex++) {
      if (letterResponses[responseIndex] == 0) {
        letterResponses[responseIndex] = LetterResponse.GRAY;
      }
    }
  }
}
