package clue;

import clue.Clue;

public class MinQuantityClue extends Clue {
    private final int quantityMin;
    private final char letter;

    public MinQuantityClue(int min, char letter) {
        quantityMin = min;
        this.letter = letter;
    }

    @Override
    public boolean applyTo(String targetWord) {
        int quantity = quantityOfIn(letter, targetWord);
        return quantityMin <= quantity;
    }
}
