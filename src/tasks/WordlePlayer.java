package tasks;

import logic.Dictionary;
import logic.ResponseMatrix;

public class WordlePlayer extends BasicPlayer {

  private WordlePlayer() {
  }

  public static void main(String[] args) {
    new WordlePlayer().run();
  }

  private void run() {
    Dictionary allowedGuesses = new Dictionary()
            .addWordsFromFile("data/wordle_allowed.csv")
            .addWordsFromFile("data/wordle_solutions.csv");

    Dictionary solutionWords = new Dictionary()
            .addWordsFromFile("data/wordle_solutions.csv");

    ResponseMatrix responseMatrix = new ResponseMatrix(allowedGuesses, solutionWords);
    String bestOpener = determineBestSinglePlay(allowedGuesses, solutionWords, responseMatrix);

    int[] scoreTally = new int[10];
    for (String solution : solutionWords.getWordList()) {
      int score = simulateSinglePlay(allowedGuesses, solutionWords, responseMatrix, solution, bestOpener);
      scoreTally[score]++;

    }

    for (int i = 1; i < scoreTally.length; i++) {
      System.out.println(i + ": " + scoreTally[i]);
    }
  }


}
