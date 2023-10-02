package clue;

import clue.Clue;

public class ExactQuantityClue extends Clue {
    private final int quantity;
    private final char letter;

    public ExactQuantityClue(int quantity, char letter) {
        this.quantity = quantity;
        this.letter = letter;
    }

    @Override
    public boolean applyTo(String targetWord) {
        return quantity == quantityOfIn(letter, targetWord);
    }
}
