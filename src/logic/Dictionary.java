package logic;

import clue.Clue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Dictionary {
  private final List<String> wordList;

  public Dictionary() {
    wordList = new ArrayList<>();
  }

  public Dictionary(List<String> wordList) {
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

  public List<String> getWordList() {
    return wordList;
  }

  public String getWord(int index) {
    return wordList.get(index);
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
