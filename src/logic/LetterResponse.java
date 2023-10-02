package logic;

public enum LetterResponse {
  GRAY('-'),
  YELLOW('Y'),
  GREEN('G');

  private final char symbol;
  private LetterResponse(char symbol){
    this.symbol=symbol;
  }
  public static LetterResponse of(char color){
    switch(color){
      case '-': return GRAY;
      case 'Y': return YELLOW;
      case 'G': return GREEN;
    }
    throw new IndexOutOfBoundsException();
  }

  public char getSymbol() {
    return symbol;
  }
}
