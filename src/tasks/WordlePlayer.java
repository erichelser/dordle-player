package tasks;

import clue.Clue;
import logic.ClueFactory;
import logic.Dictionary;
import logic.GuessEvaluator;
import logic.ResponseBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordlePlayer {

  private final GuessEvaluator guessEvaluator;
  private final ClueFactory clueFactory;

  private WordlePlayer() {
    guessEvaluator = GuessEvaluator.getSingleton();
    clueFactory = ClueFactory.getSingleton();
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

    long start = System.currentTimeMillis();
    determineBestOpener(allowedGuesses, solutionWords);
    long stop = System.currentTimeMillis();
    System.out.println("Completed in " + (stop - start) + " ms");
  }

  private String determineBestOpener(Dictionary allowedGuesses,
                                     Dictionary potentialSolutions) {
    int i = 0;
    String bestGuess = "";
    double bestGuessRating = -1;

    for (String guess : allowedGuesses.getWordList()) {
      Map<String, Integer> wordResponseDistributions = buildWordResponseDistributions(potentialSolutions, guess);
      double guessRating = getAverageInformationGained(potentialSolutions, wordResponseDistributions, guess);
      if (bestGuessRating == -1 || guessRating < bestGuessRating) {
        bestGuess = guess;
        bestGuessRating = guessRating;
      }

      System.out.println(++i + ": " + guess + " -> " + guessRating
              + " (best so far: " + bestGuess + "/" + bestGuessRating + ")");
    }
    return bestGuess;
  }

  private Map<String, Integer> buildWordResponseDistributions(Dictionary potentialSolutions,
                                                              String testGuess) {
    Map<String, Integer> wordResponseDistributions = new HashMap<>();

    potentialSolutions.getWordList().forEach(potentialSolution -> {
      String response = guessEvaluator.evaluateGuess(potentialSolution, testGuess);
      int count = 0;
      if (wordResponseDistributions.containsKey(response)) {
        count = wordResponseDistributions.get(response);
      }
      wordResponseDistributions.put(response, count + 1);
    });
    return wordResponseDistributions;
  }

  private double getAverageInformationGained(
          Dictionary potentialSolutions, Map<String, Integer> wordResponseDistributions,
          String testGuess) {
    double totalBitsOfInformation = 0;
    for (String response : wordResponseDistributions.keySet()) {
      List<Clue> solutionClues = clueFactory.buildClues(testGuess, response);
      double remainingPotentialSolutions = potentialSolutions.filterByClues(solutionClues).size();
      if (remainingPotentialSolutions != 0) {
        double bitsOfInformation = log2(remainingPotentialSolutions / potentialSolutions.size());
        totalBitsOfInformation += bitsOfInformation * wordResponseDistributions.get(response);
      }
    }
    return totalBitsOfInformation / potentialSolutions.size();
  }

  private double getAverageRemainingPotentialSolutions(
          Dictionary potentialSolutions, Map<String, Integer> wordResponseDistributions,
          List<String> possibleResponses, String testGuess) {
    double totalRemainingPotentialSolutions = 0;
    for (String response : possibleResponses) {
      if (wordResponseDistributions.get(response) == 0) {
        continue;
      }
      Set<String> remainingPotentialSolutions = filterPotentialSolutions(potentialSolutions, testGuess, response);
      double remainingPotentialSolutionsCount = remainingPotentialSolutions.size();
      totalRemainingPotentialSolutions += remainingPotentialSolutionsCount * wordResponseDistributions.get(response);
    }
    return totalRemainingPotentialSolutions / potentialSolutions.size();
  }

  private Set<String> filterPotentialSolutions(Dictionary potentialSolutions,
                                               String testGuess,
                                               String response) {
    List<Clue> solutionClues = clueFactory.buildClues(testGuess, response);
    return potentialSolutions.filterByClues(solutionClues);
  }

  private static double log2(double x) {
    return Math.log(x) / log2;
  }

  private static final double log2 = Math.log(2);
}
