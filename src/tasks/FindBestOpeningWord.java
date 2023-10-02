package tasks;

import clue.Clue;
import logic.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindBestOpeningWord {
  public static void main(String[] args) {
    Dictionary allowedGuesses = new Dictionary()
            .addWordsFromFile("data/wordle_allowed.csv")
            .addWordsFromFile("data/wordle_solutions.csv");

    Dictionary solutionWords = new Dictionary()
            .addWordsFromFile("data/wordle_solutions.csv");

    System.out.println(allowedGuesses.getWordList().size());

    determineBestGuess(allowedGuesses,
            solutionWords,
            true,
            false,
            ClueFactory.getSingleton());
  }

  private static String determineBestGuess(Dictionary allowedGuesses,
                                           Dictionary potentialSolutions,
                                           boolean isVerbose,
                                           boolean isLogScale,
                                           ClueFactory clueFactory) {
    int i = 0;
    String bestSolution = "";
    double bestScore = -1;

    final Map<String, Integer> wordResponseDistributions = new HashMap<>();
    final List<String> possibleResponses = ResponseBuilder.buildAllPossibleResponses();
    for (String testGuess : allowedGuesses.getWordList()) {

      possibleResponses.forEach(possibleResponse -> wordResponseDistributions.put(possibleResponse, 0));

      double testScore = 0;
      for (String testSolution : potentialSolutions.getWordList()) {
        String response = GuessEvaluator.evaluateGuess(testSolution, testGuess);
        wordResponseDistributions.put(response, wordResponseDistributions.get(response) + 1);
      }

      for (String response : possibleResponses) {
        List<Clue> solutionClues = clueFactory.buildClues(testGuess, response);
        int remain = allowedGuesses.countRemainingPossibleWords(solutionClues);
        if (isLogScale) {
          if (remain > 0) {
            testScore += log2(12972.0 / remain) * wordResponseDistributions.get(response);
          }
        } else {
          testScore += remain * wordResponseDistributions.get(response);
        }
      }

      if (bestScore == -1
              || (isLogScale && testScore > bestScore)
              || (!isLogScale && testScore < bestScore)) {
        bestSolution = testGuess;
        bestScore = testScore;
      }

      if (isVerbose) {
        System.out.println(++i + ": " + testGuess +
                " -> " + (testScore / potentialSolutions.size())
                + " (best so far: " + bestSolution + "/" + (bestScore / potentialSolutions.size()) + ")");
      }
    }
    if (isVerbose) {
      System.out.println("Next best guess: " + bestSolution + " (" + (bestScore / potentialSolutions.size()) + ")");
    }
    return bestSolution;
  }

  private static double log2(double x) {
    return Math.log(x) / log2;
  }
  private static final double log2 = Math.log(2);
}
