package tasks;

import clue.Clue;
import logic.*;
import misc.DordleDictionary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DordlePlayer {

  private final GuessEvaluator guessEvaluator;
  private final ClueFactory clueFactory;

  private DordlePlayer() {
    guessEvaluator = GuessEvaluator.getSingleton();
    clueFactory = ClueFactory.getSingleton();
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

    System.out.println(allowedGuesses.getWordList().size());

    determineBestOpener(allowedGuesses, solutionWords);
  }

  private String determineBestOpener(Dictionary allowedGuesses,
                                     Dictionary potentialSolutions) {
    int i = 0;
    String bestGuess = "";
    double bestGuessRating = -1;
    final List<String> possibleResponses = ResponseBuilder.buildAllPossibleResponses();

    for (String guess : allowedGuesses.getWordList()) {
      Map<String, Integer> wordResponseDistributions =
              buildWordResponseDistributions(potentialSolutions, possibleResponses, guess);

      double guessRating =
              getAverageInformationGained(potentialSolutions,
                      wordResponseDistributions, possibleResponses, guess);

      if (bestGuessRating == -1 ||
              guessRating < bestGuessRating) {
        bestGuess = guess;
        bestGuessRating = guessRating;
      }

      System.out.println(++i + ": " + guess + " -> " + guessRating
              + " (best so far: " + bestGuess + "/" + bestGuessRating + ")");
    }
    return bestGuess;
  }

  private Map<String, Integer> buildWordResponseDistributions(Dictionary potentialSolutions,
                                                                    List<String> possibleResponses,
                                                                    String testGuess) {
    Map<String, Integer> wordResponseDistributions = new HashMap<>();
    possibleResponses.forEach(possibleResponse -> wordResponseDistributions.put(possibleResponse, 0));

    potentialSolutions.getWordList().forEach(potentialSolution -> {
      String response = guessEvaluator.evaluateGuess(potentialSolution, testGuess);
      wordResponseDistributions.put(response, wordResponseDistributions.get(response) + 1);
    });
    return wordResponseDistributions;
  }

  private double getAverageInformationGained(
          Dictionary potentialSolutions, Map<String, Integer> wordResponseDistributions,
          List<String> possibleResponses, String testGuess) {
    double totalBitsOfInformation = 0;
    for (String response : possibleResponses) {
      if (wordResponseDistributions.get(response) == 0) {
        continue;
      }
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
      List<Clue> solutionClues = clueFactory.buildClues(testGuess, response);
      double remainingPotentialSolutions = potentialSolutions.filterByClues(solutionClues).size();
      totalRemainingPotentialSolutions += remainingPotentialSolutions * wordResponseDistributions.get(response);
    }
    return totalRemainingPotentialSolutions / potentialSolutions.size();
  }

  private static double log2(double x) {
    return Math.log(x) / log2;
  }

  private static final double log2 = Math.log(2);
}
