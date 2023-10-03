package tasks;

import logic.Dictionary;
import logic.ResponseMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BasicPlayer {
  public int simulateSinglePlay(Dictionary allowedGuesses, Dictionary solutionWords,
                                ResponseMatrix responseMatrix, String solution, String attempt) {
    String response = responseMatrix.lookup(attempt, solution);
    int score = 1;
    Dictionary newSolutionWords = solutionWords;

    while (!solution.equals(attempt)) {
      newSolutionWords = filterPotentialSolutions(newSolutionWords, responseMatrix, attempt, response);
      attempt = determineBestSinglePlay(allowedGuesses, newSolutionWords, responseMatrix);
      response = responseMatrix.lookup(attempt, solution);
      score++;
    }

    return score;
  }

  public int simulateDoublePlay(Dictionary allowedGuesses, Dictionary solutionWords,
                                ResponseMatrix responseMatrix, String solutionA, String solutionB, String attempt) {
    String responseA = responseMatrix.lookup(attempt, solutionA);
    String responseB = responseMatrix.lookup(attempt, solutionB);
    int score = 1;
    Dictionary newSolutionWordsA = solutionWords;
    Dictionary newSolutionWordsB = solutionWords;

    while (!solutionA.equals(attempt) && !solutionB.equals(attempt)) {
      newSolutionWordsA = filterPotentialSolutions(newSolutionWordsA, responseMatrix, attempt, responseA);
      newSolutionWordsB = filterPotentialSolutions(newSolutionWordsB, responseMatrix, attempt, responseB);
      attempt = determineBestDoublePlay(allowedGuesses, newSolutionWordsA, newSolutionWordsB, responseMatrix);
      responseA = responseMatrix.lookup(attempt, solutionA);
      responseB = responseMatrix.lookup(attempt, solutionB);
      score++;
    }

    if (!solutionA.equals(attempt)) {
      return score + simulateSinglePlay(allowedGuesses, newSolutionWordsA, responseMatrix, solutionA, attempt) - 1;
    } else {
      return score + simulateSinglePlay(allowedGuesses, newSolutionWordsB, responseMatrix, solutionB, attempt) - 1;
    }
  }

  public double average(int[] scoreTally) {
    double sum = 0;
    int games = 0;
    for (int i = 0; i < scoreTally.length; i++) {
      sum += scoreTally[i] * i;
      games += scoreTally[i];
    }
    return sum / games;
  }

  public Dictionary filterPotentialSolutions(Dictionary potentialSolutions,
                                             ResponseMatrix responseMatrix,
                                             String testGuess,
                                             String response) {
    List<String> solutionSubset = new ArrayList<>();
    potentialSolutions.getWordList().forEach(potentialSolution -> {
      if (responseMatrix.lookup(testGuess, potentialSolution).equals(response)) {
        solutionSubset.add(potentialSolution);
      }
    });
    return new Dictionary(solutionSubset);
  }

  public String determineBestSinglePlay(Dictionary allowedGuesses,
                                        Dictionary potentialSolutions,
                                        ResponseMatrix responseMatrix) {
    if (potentialSolutions.size() == 1) {
      return potentialSolutions.getWordList().stream().findFirst().get();
    }

    String bestGuess = "";
    double bestGuessRating = -9999;

    for (String guess : allowedGuesses.getWordList()) {
      Map<String, Integer> wordResponseDistributions = buildWordResponseDistributions(potentialSolutions, guess, responseMatrix);
      double guessRating = getAverageInformationGained(potentialSolutions, wordResponseDistributions);
      if (bestGuessRating == -9999 || guessRating < bestGuessRating) {
        bestGuess = guess;
        bestGuessRating = guessRating;
      }
    }
    return bestGuess;
  }

  public String determineBestDoublePlay(Dictionary allowedGuesses,
                                        Dictionary potentialSolutionsA,
                                        Dictionary potentialSolutionsB,
                                        ResponseMatrix responseMatrix) {
    if (potentialSolutionsA.size() == 1) {
      return potentialSolutionsA.getWordList().stream().findFirst().get();
    }
    if (potentialSolutionsB.size() == 1) {
      return potentialSolutionsB.getWordList().stream().findFirst().get();
    }

    String bestGuess = "";
    double bestGuessRating = -9999;

    for (String guess : allowedGuesses.getWordList()) {
      Map<String, Integer> wordResponseDistributionsA = buildWordResponseDistributions(potentialSolutionsA, guess, responseMatrix);
      double guessRatingA = getAverageInformationGained(potentialSolutionsA, wordResponseDistributionsA);

      Map<String, Integer> wordResponseDistributionsB = buildWordResponseDistributions(potentialSolutionsB, guess, responseMatrix);
      double guessRatingB = getAverageInformationGained(potentialSolutionsB, wordResponseDistributionsB);

      double totalGuessRating = guessRatingA + guessRatingB;
      if (bestGuessRating == -9999 || totalGuessRating < bestGuessRating) {
        bestGuess = guess;
        bestGuessRating = totalGuessRating;
      }
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
          Dictionary potentialSolutions, Map<String, Integer> wordResponseDistributions) {
    double totalRemainingPotentialSolutions = 0;
    for (String response : wordResponseDistributions.keySet()) {
      double remainingPotentialSolutionsCount = wordResponseDistributions.get(response);
      totalRemainingPotentialSolutions += remainingPotentialSolutionsCount * wordResponseDistributions.get(response);
    }
    return totalRemainingPotentialSolutions / potentialSolutions.size();
  }

  private static double log2(double x) {
    return Math.log(x) / log2;
  }

  private static final double log2 = Math.log(2);
}
