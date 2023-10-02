package logic;

import clue.Clue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Dictionary {
  private final Set<String> wordList;

  public Dictionary() {
    wordList = new HashSet<>();
  }

  public Dictionary(Set<String> wordList) {
    this.wordList = wordList;
  }

  public Dictionary addWordsFromFile(String sourceFile) {
    wordList.addAll(readWordsFromFile(sourceFile));
    return this;
  }


  public Dictionary addWordsFromDictionary(Set<String> wordList) {
    this.wordList.addAll(wordList);
    return this;
  }

  public Set<String> getWordList() {
    return wordList;
  }

  public int size() {
    return wordList.size();
  }

  public Set<String> filterByClues(Collection<Clue> clues) {
    return wordList.stream().filter(word ->
                    clues.stream().allMatch(clue -> clue.applyTo(word)))
            .collect(Collectors.toSet());
  }

  public int countRemainingPossibleWords(Collection<Clue> clues) {
    return wordList.stream().map(word ->
                    clues.stream().allMatch(clue -> clue.applyTo(word)) ? 1 : 0)
            .reduce(0, Integer::sum);
  }

  private Set<String> readWordsFromFile(String location) {
    Set<String> words = new HashSet<>();
    try (Scanner scanner = new Scanner(new File(location))) {
      scanner.useDelimiter("[,\\s]");
      while (scanner.hasNext()) {
        String nextWord = scanner.next().trim().toUpperCase();
        if (nextWord.length() == 5) {
          words.add(nextWord);
        }
      }
    } catch (FileNotFoundException ignore) {
      return new HashSet<>();
    }
    return words;
  }
}
