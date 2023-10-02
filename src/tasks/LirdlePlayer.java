package tasks;

import clue.Clue;
import logic.ClueFactory;
import logic.Dictionary;
import logic.GuessEvaluator;
import logic.WordResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LirdlePlayer {
  public static void main(String[] args) {
//    test();
    Dictionary allowedGuesses = new Dictionary()
            .addWordsFromFile("data/wordle_allowed.csv")
            .addWordsFromFile("data/wordle_solutions.csv");

    Dictionary solutionWords = new Dictionary()
            .addWordsFromFile("data/wordle_solutions.csv");
    GuessEvaluator guessEvaluator = GuessEvaluator.getSingleton();

    ClueFactory clueFactory = ClueFactory.getSingleton();

    for (String allowedGuess : allowedGuesses.getWordList()) {
      double potentialSolutionsTotal = 0;
      for (String solutionWord : solutionWords.getWordList()) {
        WordResponse trueResponse = guessEvaluator.evaluateGuess(solutionWord, allowedGuess);
        List<WordResponse> falseResponses = guessEvaluator.generateLies(trueResponse);

        Set<String> potentialSolutions = new HashSet<>();
        falseResponses.forEach(falseResponse -> {
          List<WordResponse> potentialTrueResponses = guessEvaluator.generateLies(falseResponse);
          potentialTrueResponses.forEach(potentialTrueResponse -> {
            List<Clue> clues = new ArrayList<>();
            clues.addAll(clueFactory.buildClues("TRACE", potentialTrueResponse));
            potentialSolutions.addAll(solutionWords.filterByClues(clues));
          });
        });
        potentialSolutionsTotal += potentialSolutions.size();
      }
      System.out.println("Guess = " + allowedGuess
              + " / Avg Possible = " + (potentialSolutionsTotal / solutionWords.size()));
    }
  }

  private static void test() {
    Dictionary allowedGuesses = new Dictionary()
            .addWordsFromFile("data/wordle_allowed.csv")
            .addWordsFromFile("data/wordle_solutions.csv");

    Dictionary solutionWords = new Dictionary()
            .addWordsFromFile("data/wordle_solutions.csv");

    GuessEvaluator guessEvaluator = GuessEvaluator.getSingleton();
    ClueFactory clueFactory = ClueFactory.getSingleton();

//    WordResponse trueResponse = guessEvaluator.evaluateGuess("FIXED", "FLIPS");
//    List<WordResponse> falseResponses = guessEvaluator.generateLies(trueResponse);

    List<WordResponse> potentialTrueResponses1 =
            guessEvaluator.generateLies(new WordResponse("--Y-Y"));

    List<WordResponse> potentialTrueResponses2 =
            guessEvaluator.generateLies(new WordResponse("-YYYY"));

    List<WordResponse> potentialTrueResponses3 =
            guessEvaluator.generateLies(new WordResponse("G-YG-"));

    List<WordResponse> potentialTrueResponses4 =
            guessEvaluator.generateLies(new WordResponse("-GYG-"));

    List<WordResponse> potentialTrueResponses5 =
            guessEvaluator.generateLies(new WordResponse("YGY-G"));

    for (int i = 0; i < potentialTrueResponses1.size(); i++) {
      for (int j = 0; j < potentialTrueResponses2.size(); j++) {
        for (int k = 0; k < potentialTrueResponses3.size(); k++) {
          for (int l = 0; l < potentialTrueResponses4.size(); l++) {
            for (int m = 0; m < potentialTrueResponses5.size(); m++) {
              List<Clue> clues = new ArrayList<>();
              WordResponse setA = potentialTrueResponses1.get(i);
              WordResponse setB = potentialTrueResponses2.get(j);
              WordResponse setC = potentialTrueResponses3.get(k);
              WordResponse setD = potentialTrueResponses4.get(l);
              WordResponse setE = potentialTrueResponses5.get(m);
              clues.addAll(clueFactory.buildClues("TRACE", setA));
              clues.addAll(clueFactory.buildClues("ORATE", setB));
              clues.addAll(clueFactory.buildClues("GLEAM", setC));
              clues.addAll(clueFactory.buildClues("TEAMS", setD));
              clues.addAll(clueFactory.buildClues("BEAMS", setE));
              Set<String> potentialSolutions = solutionWords.filterByClues(clues);
              if (potentialSolutions.size() > 0) {
                System.out.println(setA.toString()
                        + " x " + setB.toString()
                        + " x " + setC.toString()
                        + " x " + setD.toString()
                        + " x " + setE.toString()
                        + " = " + potentialSolutions.size());
                potentialSolutions.forEach(System.out::println);
              }
            }
          }
        }
      }
    }
  }
}
