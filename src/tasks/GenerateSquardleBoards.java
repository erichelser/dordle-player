package tasks;

import logic.Dictionary;

public class GenerateSquardleBoards {
  public static void main(String[] args) {
    Dictionary allowedGuesses = new Dictionary()
            .addWordsFromFile("data/wordle_allowed.csv")
            .addWordsFromFile("data/wordle_solutions.csv");

    int i = 0;
    for (String word1 : allowedGuesses.getWordList()) {
      for (String word2 : allowedGuesses.getWordList()) {
        if (word1.charAt(0) == word2.charAt(0)
                && !word1.equals(word2)) {
          for (String word3 : allowedGuesses.getWordList()) {
            if (word2.charAt(2) == word3.charAt(0)
                    && !word1.equals(word3)
                    && !word2.equals(word3)) {
              for (String word4 : allowedGuesses.getWordList()) {
                if (word1.charAt(2) == word4.charAt(0)
                        && word3.charAt(2) == word4.charAt(2)
                        && !word1.equals(word4)
                        && !word2.equals(word4)
                        && !word3.equals(word4)) {
                  for (String word5 : allowedGuesses.getWordList()) {
                    if (word2.charAt(4) == word5.charAt(0)
                            && word4.charAt(4) == word5.charAt(2)
                            && !word1.equals(word5)
                            && !word2.equals(word5)
                            && !word3.equals(word5)
                            && !word4.equals(word5)) {
                      for (String word6 : allowedGuesses.getWordList()) {
                        if (word1.charAt(4) == word6.charAt(0)
                                && word3.charAt(4) == word6.charAt(2)
                                && word5.charAt(4) == word6.charAt(4)
                                && !word1.equals(word6)
                                && !word2.equals(word6)
                                && !word3.equals(word6)
                                && !word4.equals(word6)
                                && !word5.equals(word6)) {
//                          System.out.println(++i + ": " + word1 + " " + word2 + " " + word3
//                                  + " " + word4 + " " + word5 + " " + word6);
                          i++;
                          if (i % 10000 == 0) System.out.println(i);
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
