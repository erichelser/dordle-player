package logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Dictionary {
  private final List<String> wordList;

  public Dictionary() {
    wordList = new ArrayList<>();
  }

  public Dictionary(List<String> wordList) {
    this();
    this.wordList.addAll(wordList);
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
