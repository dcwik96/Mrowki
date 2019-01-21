package pl.zjp;

public class Utils {
    private static final int PERCENTAGE_MULTIPLIER = 100;


    private Utils() {
    }


    public static char[][] fillArrayWithBlankSpaces(char[][] table) {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                table[i][j] = ' ';
            }
        }
        return table;
    }

    public static double countAvgCoverage(int placeCovered,char[][] table) {
        return (double) placeCovered / (table.length * table[0].length) * PERCENTAGE_MULTIPLIER;
    }
}
