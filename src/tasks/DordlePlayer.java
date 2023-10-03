package tasks;

import logic.Dictionary;
import logic.ResponseMatrix;
import misc.DordleDictionary;

import java.util.*;

public class DordlePlayer extends BasicPlayer {

  private DordlePlayer() {
  }

  public static void main(String[] args) {
    new DordlePlayer().run();
  }

  private void run() {
    Dictionary allowedGuesses = new Dictionary()
            .addWordsFromDictionary(DordleDictionary.getAllowed())
            .addWordsFromDictionary(DordleDictionary.getAnswers());

    Dictionary solutionWords = new Dictionary()
            .addWordsFromDictionary(DordleDictionary.getAnswers());
    int solutionWordsSize = solutionWords.size();

    ResponseMatrix responseMatrix = new ResponseMatrix(allowedGuesses, solutionWords);

    String bestOpener = determineBestSinglePlay(allowedGuesses, solutionWords, responseMatrix);


    int[] scoreTally = new int[10];
    for (int game = 1; game <= 100000; game++) {
      int indexA;
      do {
        indexA = (new Random()).nextInt() % solutionWordsSize;
      } while (indexA < 0);
      int indexB;
      do {
        indexB = (new Random()).nextInt() % solutionWordsSize;
      } while (indexB < 0 || indexA == indexB);
      String solutionA = solutionWords.getWord(indexA);
      String solutionB = solutionWords.getWord(indexB);
      int score = simulateDoublePlay(allowedGuesses, solutionWords, responseMatrix, solutionA, solutionB, bestOpener);
      scoreTally[score]++;
      System.out.print("Games: " + game + " [ ");
      for (int scoreEntry : scoreTally) {
        System.out.print(scoreEntry + " ");
      }
      System.out.println("] {" + average(scoreTally) + "}");
    }
  }
}
