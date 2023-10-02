package logic;

import clue.*;

import java.util.LinkedList;
import java.util.List;

public class ClueFactory {

  private static ClueFactory singleton;

  public static ClueFactory getSingleton() {
    if (singleton == null) {
      singleton = new ClueFactory();
    }
    return singleton;
  }

  public List<Clue> buildClues(String myGuess, String response) {
    int[] freqCounts = new int [26];
    int[] yellowCounts = new int [26];
    int[]  greenCounts = new int[26];
    tallyResponseColors(freqCounts, yellowCounts, greenCounts, myGuess, response);

    List<Clue> clues = new LinkedList<>();
    for (int guessIndex = 0; guessIndex < myGuess.length(); guessIndex++) {
      char guessedLetter = myGuess.charAt(guessIndex);
      int guessedLetterIndex=guessedLetter-'A';

      if (response.charAt(guessIndex) == LetterResponse.GREEN) {
        clues.add(new HitClue(guessIndex, guessedLetter));
      } else if (response.charAt(guessIndex) == LetterResponse.YELLOW) {

        clues.add(new ExcludeClue(guessIndex, guessedLetter));

        if (freqCounts[guessedLetterIndex] ==
                yellowCounts[guessedLetterIndex] + greenCounts[guessedLetterIndex]) {
          clues.add(new MinQuantityClue(freqCounts[guessedLetterIndex], guessedLetter));
        } else {
          clues.add(new ExactQuantityClue(freqCounts[guessedLetterIndex], guessedLetter));
        }
      } else {
        if (yellowCounts[guessedLetterIndex] == 0
                && greenCounts[guessedLetterIndex] == 0) {
          clues.add(new NotInSolutionClue(guessedLetter));
        }
      }
    }
    return clues;
  }

  // Rules are hints about the quantity and placement of letters
  // Black -> None of the letters are X
  // Yellow -> At least one letter is X but not this one
  // Yellow/Yellow -> At least N letters are X but not these
  // Yellow/Black -> Exactly N letters are X but not these
  // Green -> Letter P is X
  private static void tallyResponseColors(int[] freqCounts,
                                          int[] yellowCounts,
                                          int[] greenCounts,
                                          String myGuess,
                                          String response) {
    for (int guessIndex = 0; guessIndex < myGuess.length(); guessIndex++) {
      int guessLetter = myGuess.charAt(guessIndex) - 'A';
      freqCounts[guessLetter]++;
      if (response.charAt(guessIndex) == LetterResponse.YELLOW) {
        yellowCounts[guessLetter]++;
      } else if (response.charAt(guessIndex) == LetterResponse.GREEN) {
        greenCounts[guessLetter]++;
      }
    }
  }

}
