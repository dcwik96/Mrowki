package pl.zjp.test;

import org.junit.Test;
import pl.zjp.Utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UtilsTest {
    @Test
    public void checkUtilsCountAvgCoverage() {
        char[][] table = new char[10][10];
        double avg = Utils.countAvgCoverage(10, table);
        assertEquals(avg, 10.0, 10);
    }

    @Test
    public void checkFillArrayWithBlankSpaces() {
        boolean isTrue = true;
        char[][] checkingTable = Utils.fillArrayWithBlankSpaces(new char[10][10]);

        for (int i = 0; i < checkingTable.length; i++) {
            for (int j = 0; j < checkingTable[0].length; j++) {
                if(checkingTable[i][j] != ' ') {
                    isTrue = false;
                }
            }
        }

        assertTrue(isTrue);
    }
}
