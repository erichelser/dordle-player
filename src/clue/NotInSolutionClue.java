package clue;

import clue.Clue;

public class NotInSolutionClue extends Clue {
    private final char letter;
    public NotInSolutionClue(char letter){
        this.letter=letter;
    }
    public boolean applyTo(String targetWord){
        return targetWord.indexOf(letter) == -1;
    }
}
