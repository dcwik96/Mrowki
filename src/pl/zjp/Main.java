package pl.zjp;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.System.out;


final class Main {
    public static final String ANSI_RESET = "\u001B[0m";

    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";

    private static final int X = 30;
    private static final int Y = 60;
    private static final int NUMBER_OF_ANTS = 3;
    private static final double MAX_SUM_OF_COVERAGE_AVG = 100.0;
    private static final int THREAD_SLEEP_TIME = 200;
    private static final int DIGIT_RADIX = 10;
    private static final int NUMBER_OF_POSSIBILITIES = 8;
    private static final int ANSII_VALUE_OF_DIGIT = 48;
    private static final int PERCENTAGE_MULTIPLIER = 100;

    private static final int MOVE_FORWARD = 0;
    private static final int MOVE_FORWARD_RIGHT = 1;
    private static final int MOVE_RIGHT = 2;
    private static final int MOVE_BACK_RIGHT = 3;
    private static final int MOVE_BACK = 4;
    private static final int MOVE_BACK_LEFT = 5;
    private static final int MOVE_LEFT = 6;
    private static final int MOVE_FORWARD_LEFT = 7;

    private Main() {
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> colors = addColors();
        List<Ant> ants = new ArrayList<>();
        char[][] table = new char[X][Y];


        setSpacesInTable(table);

        int numberOfAnts = NUMBER_OF_ANTS;
        int[] coverage = new int[numberOfAnts];


        for (int i = 0; i < numberOfAnts; i++) {
            Random random = new Random();

            int newX = random.nextInt(X);
            int newY = random.nextInt(Y);
            if (table[newX][newY] == ' ') {
                ants.add(new Ant(newX, newY));
                table[newX][newY] = Character.forDigit(i, DIGIT_RADIX);

            }
        }
        int[] lastMove = new int[numberOfAnts];

        double sumOfAverages = 0.0;

        while (sumOfAverages != MAX_SUM_OF_COVERAGE_AVG) {
            Thread.sleep(THREAD_SLEEP_TIME);

            startMovesDependingOnSystem();

            setColorsAndReturnCoverageValue(table, colors, coverage);


            sumOfAverages = 0.0;
            for (int i = 0; i < numberOfAnts; i++) {
                double avg = countAvgCoverage(coverage[i], X, Y);
                out.println("Ant" + i + " coverage: " +  avg + "%");
                coverage[i] = 0;
                sumOfAverages += avg;
            }

            setPosibilities(numberOfAnts, ants, lastMove, table);

        }

    }

    private static void setColorsAndReturnCoverageValue(char[][] table, List<String> colors, int[] coverage) {
        for (int i = 0; i < X; i++) {
            for (int j = 0; j < Y; j++) {
                if (table[i][j] != ' ') {
                    out.print(colors.get(Character.digit(table[i][j], DIGIT_RADIX)) + " " + ANSI_RESET);
                    coverage[Character.digit(table[i][j], DIGIT_RADIX)]++;
                } else {
                    out.print(table[i][j]);
                }
            }
            out.println();
        }
    }

    private static void setSpacesInTable(char[][] table) {
        for (int i = 0; i < X; i++) {
            for (int j = 0; j < Y; j++) {
                table[i][j] = ' ';
            }
        }
    }

    private static void setPosibilities(int numberOfAnts, List<Ant> ants, int[] lastMove, char[][] table) {
        boolean[] possibilities = new boolean[NUMBER_OF_POSSIBILITIES];

        for (int i = 0; i < numberOfAnts; i++) {
            if (lastMove[i] == MOVE_FORWARD) {
                possibilities[MOVE_FORWARD_LEFT] = true;
                possibilities[MOVE_FORWARD] = true;
                possibilities[MOVE_FORWARD_RIGHT] = true;
            }
            else if (lastMove[i] == MOVE_FORWARD_RIGHT) {
                possibilities[MOVE_FORWARD] = true;
                possibilities[MOVE_FORWARD_RIGHT] = true;
                possibilities[MOVE_RIGHT] = true;
            }
            else if (lastMove[i] == MOVE_RIGHT) {
                possibilities[MOVE_FORWARD_RIGHT] = true;
                possibilities[MOVE_RIGHT] = true;
                possibilities[MOVE_BACK_RIGHT] = true;
            }
            else if (lastMove[i] == MOVE_BACK_RIGHT) {
                possibilities[MOVE_RIGHT] = true;
                possibilities[MOVE_BACK_RIGHT] = true;
                possibilities[MOVE_BACK] = true;
            }
            else if (lastMove[i] == MOVE_BACK) {
                possibilities[MOVE_BACK_RIGHT] = true;
                possibilities[MOVE_BACK] = true;
                possibilities[MOVE_BACK_LEFT] = true;
            }
            else if (lastMove[i] == MOVE_BACK_LEFT) {
                possibilities[MOVE_BACK] = true;
                possibilities[MOVE_BACK_LEFT] = true;
                possibilities[MOVE_LEFT] = true;
            }
            else if (lastMove[i] == MOVE_LEFT) {
                possibilities[MOVE_BACK_LEFT] = true;
                possibilities[MOVE_LEFT] = true;
                possibilities[MOVE_FORWARD_LEFT] = true;
            }
            else if (lastMove[i] == MOVE_FORWARD_LEFT) {
                possibilities[MOVE_FORWARD] = true;
                possibilities[MOVE_LEFT] = true;
                possibilities[MOVE_FORWARD_LEFT] = true;
            }

            changePosition(possibilities, ants, lastMove, i);
            setAntsPosition(ants, i);

            table[ants.get(i).getPositionX()][ants.get(i).getPositionY()] = (char) (i + ANSII_VALUE_OF_DIGIT);
        }
    }

    private static void setAntsPosition(List<Ant> ants, int i) {
        ants.get(i).setPositionX(ants.get(i).getPositionX()%X);
        ants.get(i).setPositionY(ants.get(i).getPositionY()%Y);
        if (ants.get(i).getPositionX() < 0) {
            ants.get(i).setPositionX(ants.get(i).getPositionX()+X);
        }
        if (ants.get(i).getPositionY() < 0) {
            ants.get(i).setPositionY(ants.get(i).getPositionY()+Y);
        }
    }

    private static void changePosition(boolean[] possibilities, List<Ant> ants, int[] lastMove, int i) {
        Random random = new Random();
        boolean moved = false;
        while (!moved) {
            int rand = random.nextInt(NUMBER_OF_POSSIBILITIES);
            lastMove[i] = rand;

            if (isMovePossible(MOVE_FORWARD, rand, possibilities[MOVE_FORWARD])) {
                ants.get(i).setPositionX(ants.get(i).getPositionX() - 1);
                moved = true;
            } else if (isMovePossible(MOVE_FORWARD_RIGHT, rand, possibilities[MOVE_FORWARD_RIGHT])) {
                ants.get(i).setPositionX(ants.get(i).getPositionX() - 1);
                ants.get(i).setPositionY(ants.get(i).getPositionY() + 1);
                moved = true;
            } else if (isMovePossible(MOVE_RIGHT, rand, possibilities[MOVE_RIGHT])) {
                ants.get(i).setPositionY(ants.get(i).getPositionY() + 1);
                moved = true;
            } else if (isMovePossible(MOVE_BACK_RIGHT, rand, possibilities[MOVE_BACK_RIGHT])) {
                ants.get(i).setPositionX(ants.get(i).getPositionX() + 1);
                ants.get(i).setPositionY(ants.get(i).getPositionY() + 1);
                moved = true;
            } else if (isMovePossible(MOVE_BACK, rand, possibilities[MOVE_BACK])) {
                ants.get(i).setPositionX(ants.get(i).getPositionX() + 1);
                moved = true;
            } else if (isMovePossible(MOVE_BACK_LEFT, rand, possibilities[MOVE_BACK_LEFT])) {
                ants.get(i).setPositionX(ants.get(i).getPositionX() + 1);
                ants.get(i).setPositionY(ants.get(i).getPositionY() - 1);
                moved = true;
            } else if (isMovePossible(MOVE_LEFT, rand, possibilities[MOVE_LEFT])) {
                ants.get(i).setPositionY(ants.get(i).getPositionY() - 1);
                moved = true;
            } else if (isMovePossible(MOVE_FORWARD_LEFT, rand, possibilities[MOVE_FORWARD_LEFT])) {
                ants.get(i).setPositionX(ants.get(i).getPositionX() - 1);
                ants.get(i).setPositionY(ants.get(i).getPositionY() - 1);
                moved = true;

            }
        }
    }

    private static boolean isMovePossible(int value, int rand, boolean possibility) {
        return rand == value && possibility;
    }

    private static void startMovesDependingOnSystem() throws IOException, InterruptedException {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            out.print("\033[H\033[2J");
        }
    }

    private static List<String> addColors() {
        List<String> colors = new ArrayList<>();
        colors.add(ANSI_RED_BACKGROUND);
        colors.add(ANSI_GREEN_BACKGROUND);
        colors.add(ANSI_BLUE_BACKGROUND);
        colors.add(ANSI_YELLOW_BACKGROUND);
        colors.add(ANSI_PURPLE_BACKGROUND);
        return colors;
    }


    private static double countAvgCoverage(int placeCovered, int x, int y) {
        return (double)placeCovered/(x*y) * PERCENTAGE_MULTIPLIER;
    }
}
