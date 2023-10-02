import clue.Clue;
import org.junit.Assert;
import org.junit.Test;

public class ClueTest {
    @Test
    public void testLetterDoesNotExist() {
        Assert.assertEquals(0, quantityOfIn('X', "TESTWORD"));
    }

    @Test
    public void testLetterExistsOnce() {
        Assert.assertEquals(1, quantityOfIn('X', "EXAGGERATED"));
    }

    @Test
    public void testLetterExistsMultipleTimes() {
        Assert.assertEquals(3, quantityOfIn('R', "ERROR"));
    }

    private int quantityOfIn(char targetLetter, String targetWord) {
        int result = 0;
        for (char letter : targetWord.toCharArray()) {
            if (letter == targetLetter) {
                result++;
            }
        }
        return result;
    }
}
