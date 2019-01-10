package pl.zjp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> colors = new ArrayList<>();
        colors.add(ANSI_RED_BACKGROUND);
        colors.add(ANSI_GREEN_BACKGROUND);
        colors.add(ANSI_YELLOW_BACKGROUND);
        colors.add(ANSI_BLUE_BACKGROUND);
        colors.add(ANSI_PURPLE_BACKGROUND);
        int x = 30;
        int y = 60;
        List<int[]> ants = new ArrayList<>();


        char[][] table = new char[x][y];
        boolean possible0 = false, possible1 = false, possible2 = false, possible3 = false, possible4 = false, possible5 = false, possible6 = false, possible7 = false;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                table[i][j] = ' ';
            }
        }

        int numberOfAnts = 3;
        int[] coverage = new int[numberOfAnts];
        for (int i = 0; i < numberOfAnts; i++) {
            coverage[i] = 0;
            Random random = new Random();

            int newX = random.nextInt(x);
            int newY = random.nextInt(y);
            if (table[newX][newY] == ' ') {
                ants.add(new int[]{newX, newY});
                table[newX][newY] = Character.forDigit(i, 10);

            }
        }
        int[] lastMove = new int[numberOfAnts];

        while (true) {
            Thread.sleep(200);

            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                System.out.print("\033[H\033[2J");
            }

            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    if (table[i][j] != ' ') {
                        System.out.print(colors.get(Character.digit(table[i][j], 10)) + " " + ANSI_RESET);
                        coverage[Character.digit(table[i][j], 10)]++;
                    } else {
                        System.out.print(table[i][j]);
                    }
                }
                System.out.println();
            }
            for (int i = 0; i < numberOfAnts; i++) {
                System.out.println("Ant" + i + " coverage: " + ((double) coverage[i] / (x * y)) * 100 + "%");
                coverage[i] = 0;
            }
            for (int i = 0; i < numberOfAnts; i++) {
                if (lastMove[i] == 0) {
                    possible7 = true;
                    possible0 = true;
                    possible1 = true;
                    possible2 = false;
                    possible3 = false;
                    possible4 = false;
                    possible5 = false;
                    possible6 = false;
                }
                if (lastMove[i] == 1) {
                    possible7 = false;
                    possible0 = true;
                    possible1 = true;
                    possible2 = true;
                    possible3 = false;
                    possible4 = false;
                    possible5 = false;
                    possible6 = false;
                }
                if (lastMove[i] == 2) {
                    possible7 = false;
                    possible0 = false;
                    possible1 = true;
                    possible2 = true;
                    possible3 = true;
                    possible4 = false;
                    possible5 = false;
                    possible6 = false;
                }
                if (lastMove[i] == 3) {
                    possible7 = false;
                    possible0 = false;
                    possible1 = false;
                    possible2 = true;
                    possible3 = true;
                    possible4 = true;
                    possible5 = false;
                    possible6 = false;
                }
                if (lastMove[i] == 4) {
                    possible7 = false;
                    possible0 = false;
                    possible1 = false;
                    possible2 = false;
                    possible3 = true;
                    possible4 = true;
                    possible5 = true;
                    possible6 = false;
                }
                if (lastMove[i] == 5) {
                    possible7 = false;
                    possible0 = false;
                    possible1 = false;
                    possible2 = false;
                    possible3 = false;
                    possible4 = true;
                    possible5 = true;
                    possible6 = true;
                }
                if (lastMove[i] == 6) {
                    possible7 = true;
                    possible0 = false;
                    possible1 = false;
                    possible2 = false;
                    possible3 = false;
                    possible4 = false;
                    possible5 = true;
                    possible6 = true;
                }
                if (lastMove[i] == 6) {
                    possible7 = true;
                    possible0 = true;
                    possible1 = false;
                    possible2 = false;
                    possible3 = false;
                    possible4 = false;
                    possible5 = false;
                    possible6 = true;
                }


                boolean moved = false;
                while (moved == false) {
                    Random random = new Random();

                    int rand = random.nextInt(8);
                    lastMove[i] = rand;

                    boolean posibble = false;
                    if (rand == 0 && possible0) {
                        ants.get(i)[0]--;
                        moved = true;
                    }
                    if (rand == 1 && possible1) {
                        ants.get(i)[0]--;
                        ants.get(i)[1]++;
                        moved = true;

                    }
                    if (rand == 2 && possible2) {
                        ants.get(i)[1]++;
                        moved = true;

                    }
                    if (rand == 3 && possible3) {
                        ants.get(i)[1]++;
                        ants.get(i)[0]++;
                        moved = true;

                    }
                    if (rand == 4 && possible4) {
                        ants.get(i)[0]++;
                        moved = true;

                    }
                    if (rand == 5 && possible5) {
                        ants.get(i)[1]--;
                        ants.get(i)[0]++;
                        moved = true;

                    }
                    if (rand == 6 && possible6) {
                        ants.get(i)[1]--;
                        moved = true;

                    }
                    if (rand == 7 && possible7) {
                        ants.get(i)[1]--;
                        ants.get(i)[0]--;
                        moved = true;

                    }
                }
                ants.get(i)[0] = ants.get(i)[0] % x;
                ants.get(i)[1] = ants.get(i)[1] % y;
                if (ants.get(i)[0] < 0) {
                    ants.get(i)[0] = ants.get(i)[0] + x;
                }

                if (ants.get(i)[1] < 0) {
                    ants.get(i)[1] = ants.get(i)[1] + y;
                }
                table[ants.get(i)[0]][ants.get(i)[1]] = (char) (i + 48);

            }
        }
    }
}
