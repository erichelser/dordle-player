package clue;

public abstract class Clue {
  public abstract boolean applyTo(String targetWord);

  protected int quantityOfIn(char targetLetter, String targetWord) {
    int result = 0;
    for (char letter : targetWord.toCharArray()) {
      if (letter == targetLetter) {
        result++;
      }
    }
    return result;
  }
}
