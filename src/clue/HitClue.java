package clue;

import clue.Clue;

public class HitClue extends Clue {
    private final int index;
    private final char letter;
    public HitClue(int index, char letter){
        this.index=index;
        this.letter=letter;
    }

    public boolean applyTo(String targetWord){
        return targetWord.charAt(index) == letter;
    }
}
