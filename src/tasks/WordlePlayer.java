package tasks;

import logic.Dictionary;
import logic.ResponseMatrix;

import java.util.*;

public class WordlePlayer {


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
    long start = System.currentTimeMillis();
    String bestOpener = determineBestPlay(allowedGuesses, solutionWords, responseMatrix);
    long stop = System.currentTimeMillis();
    System.out.println("Completed in " + (stop - start) + " ms");

    String solution = "SLYLY";
    System.out.println("Let's play: the answer is " + solution);

    start = System.currentTimeMillis();
    int score = simulatePlay(allowedGuesses, solutionWords, responseMatrix, solution, bestOpener);
    stop = System.currentTimeMillis();

    System.out.println("I got it in " + score + " attempts!");
    System.out.println("Completed in " + (stop - start) + " ms");

  }

  private int simulatePlay(Dictionary allowedGuesses, Dictionary solutionWords,
                           ResponseMatrix responseMatrix, String solution, String attempt) {
    String response = responseMatrix.lookup(attempt, solution);
    int score = 1;
    Dictionary newSolutionWords = solutionWords;

    while (!solution.equals(attempt)) {
      newSolutionWords = filterPotentialSolutions(newSolutionWords, responseMatrix, attempt, response);
      attempt = determineBestPlay(allowedGuesses, newSolutionWords, responseMatrix);
      response = responseMatrix.lookup(attempt, solution);
      score++;
    }

    return score;
  }

  private String determineBestPlay(Dictionary allowedGuesses,
                                   Dictionary potentialSolutions, ResponseMatrix responseMatrix) {
    if (potentialSolutions.size() == 1) {
      return potentialSolutions.getWordList().stream().findFirst().get();
    }
    int i = 0;
    String bestGuess = "";
    double bestGuessRating = -9999;

    for (String guess : allowedGuesses.getWordList()) {
      Map<String, Integer> wordResponseDistributions = buildWordResponseDistributions(potentialSolutions, guess, responseMatrix);
      double guessRating = getAverageInformationGained(potentialSolutions, wordResponseDistributions);
      if (bestGuessRating == -9999 || guessRating < bestGuessRating) {
        bestGuess = guess;
        bestGuessRating = guessRating;
      }

//      System.out.println(++i + ": " + guess + " -> " + guessRating
//              + " (best so far: " + bestGuess + "/" + bestGuessRating + ")");
    }
    return bestGuess;
  }

  private Map<String, Integer> buildWordResponseDistributions(Dictionary potentialSolutions,
                                                              String testGuess,
                                                              ResponseMatrix responseMatrix) {
    Map<String, Integer> wordResponseDistributions = new HashMap<>();

    potentialSolutions.getWordList().forEach(potentialSolution -> {
      String response = responseMatrix.lookup(testGuess, potentialSolution);
      int count = 0;
      if (wordResponseDistributions.containsKey(response)) {
        count = wordResponseDistributions.get(response);
      }
      wordResponseDistributions.put(response, count + 1);
    });
    return wordResponseDistributions;
  }

  private double getAverageInformationGained(
          Dictionary potentialSolutions, Map<String, Integer> wordResponseDistributions) {
    double totalBitsOfInformation = 0;
    for (String response : wordResponseDistributions.keySet()) {
      double remainingPotentialSolutions = wordResponseDistributions.get(response);
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
    for (String response : wordResponseDistributions.keySet()) {
      double remainingPotentialSolutionsCount = wordResponseDistributions.get(response);
      totalRemainingPotentialSolutions += remainingPotentialSolutionsCount * wordResponseDistributions.get(response);
    }
    return totalRemainingPotentialSolutions / potentialSolutions.size();
  }

  private Dictionary filterPotentialSolutions(Dictionary potentialSolutions,
                                              ResponseMatrix responseMatrix,
                                              String testGuess,
                                              String response) {
    Set<String> solutionSubset = new HashSet<>();
    potentialSolutions.getWordList().forEach(potentialSolution -> {
      if (responseMatrix.lookup(testGuess, potentialSolution).equals(response)) {
        solutionSubset.add(potentialSolution);
      }
    });
    return new Dictionary(solutionSubset);
  }

  private static double log2(double x) {
    return Math.log(x) / log2;
  }

  private static final double log2 = Math.log(2);
}
