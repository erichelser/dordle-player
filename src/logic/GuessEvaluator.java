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

  public WordResponse evaluateGuess(String answer, String guess) {
    LetterResponse[] letterResponses = new LetterResponse[answer.length()];
    boolean[] answerLettersMatched = new boolean[answer.length()];

    markCorrectLetters(letterResponses, answerLettersMatched, answer, guess);
    markPresentLetters(letterResponses, answerLettersMatched, answer, guess);
    markMissingLetters(letterResponses);

    return new WordResponse(letterResponses);
  }

  private void markCorrectLetters(
          LetterResponse[] letterResponses, boolean[] answerLettersMatched, String answer, String guess) {
    for (int i = 0; i < guess.length() && i < answer.length(); i++) {
      if (guess.charAt(i) == answer.charAt(i)) {
        letterResponses[i] = LetterResponse.GREEN;
        answerLettersMatched[i] = true;
      }
    }
  }

  private void markPresentLetters(
          LetterResponse[] letterResponses, boolean[] answerLettersMatched, String answer, String guess) {
    for (int guessIndex = 0; guessIndex < guess.length(); guessIndex++) {
      if (letterResponses[guessIndex] != null) {
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

  private void markMissingLetters(LetterResponse[] letterResponses) {
    for (int responseIndex = 0; responseIndex < letterResponses.length; responseIndex++) {
      if (letterResponses[responseIndex] == null) {
        letterResponses[responseIndex] = LetterResponse.GRAY;
      }
    }
  }

  // Lirdle rules: For each player guess, the response will contain one lie. The letter
  // lied about will be chosen randomly, and has a fifty-fifty chance of being one of the
  // other two response possibilities.
  public List<WordResponse> generateLies(WordResponse trueResponse) {
    List<WordResponse> falseResponses = new ArrayList<>();
    for(int i=0; i<trueResponse.size(); i++){
      for(LetterResponse unusedResponse:LetterResponse.values()) {
        if(unusedResponse == trueResponse.get(i)){
          continue;
        }
        falseResponses.add(trueResponse.clone().set(i,unusedResponse));
      }
    }
    return falseResponses;
  }
}
