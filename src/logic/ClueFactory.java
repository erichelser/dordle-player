package logic;

import clue.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ClueFactory {

  private static ClueFactory singleton;

  public static ClueFactory getSingleton() {
    if (singleton == null) {
      singleton = new ClueFactory();
    }
    return singleton;
  }

  public List<Clue> buildClues(String myGuess, WordResponse response) {
    Map<Character, Integer> freqCounts = createEmptyAlphabetMap();
    Map<Character, Integer> yellowCounts = createEmptyAlphabetMap();
    Map<Character, Integer> greenCounts = createEmptyAlphabetMap();
    tallyResponseColors(freqCounts, yellowCounts, greenCounts, myGuess, response);

    List<Clue> clues = new LinkedList<>();
    for (int guessIndex = 0; guessIndex < myGuess.length(); guessIndex++) {
      char guessedLetter = myGuess.charAt(guessIndex);

      if (response.get(guessIndex) == LetterResponse.GREEN) {
        clues.add(new HitClue(guessIndex, guessedLetter));
      } else if (response.get(guessIndex) == LetterResponse.YELLOW) {

        clues.add(new ExcludeClue(guessIndex, guessedLetter));

        if (freqCounts.get(guessedLetter) ==
                yellowCounts.get(guessedLetter) + greenCounts.get(guessedLetter)) {
          clues.add(new MinQuantityClue(freqCounts.get(guessedLetter), guessedLetter));
        } else {
          clues.add(new ExactQuantityClue(freqCounts.get(guessedLetter), guessedLetter));
        }
      } else {
        if (yellowCounts.get(guessedLetter) == 0
                && greenCounts.get(guessedLetter) == 0) {
          clues.add(new NotInSolutionClue(guessedLetter));
        }
      }
    }
    return clues;
  }

  private static Map<Character, Integer> createEmptyAlphabetMap() {
    Map<Character, Integer> letterCounts = new HashMap<>();
    for (char ch = 'A'; ch <= 'Z'; ch++) {
      letterCounts.put(ch, 0);
    }
    return letterCounts;
  }

  // Rules are hints about the quantity and placement of letters
  // Black -> None of the letters are X
  // Yellow -> At least one letter is X but not this one
  // Yellow/Yellow -> At least N letters are X but not these
  // Yellow/Black -> Exactly N letters are X but not these
  // Green -> Letter P is X
  private static void tallyResponseColors(Map<Character, Integer> freqCounts,
                                          Map<Character, Integer> yellowCounts,
                                          Map<Character, Integer> greenCounts,
                                          String myGuess,
                                          WordResponse response) {
    for (int guessIndex = 0; guessIndex < myGuess.length(); guessIndex++) {
      char guessLetter = myGuess.charAt(guessIndex);
      freqCounts.put(guessLetter, freqCounts.get(guessLetter) + 1);
      if (response.get(guessIndex) == LetterResponse.YELLOW) {
        yellowCounts.put(guessLetter, yellowCounts.get(guessLetter) + 1);
      } else if (response.get(guessIndex) == LetterResponse.GREEN) {
        greenCounts.put(guessLetter, greenCounts.get(guessLetter) + 1);
      }
    }
  }

}
