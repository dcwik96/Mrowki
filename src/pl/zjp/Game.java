package pl.zjp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.System.out;

public class Game {
    private static final String ANSI_RESET = "\u001B[0m";

    private static final double MAX_SUM_OF_COVERAGE_AVG = 100.0;
    private static final int THREAD_SLEEP_TIME = 200;
    private static final int DIGIT_RADIX = 10;
    private static final int NUMBER_OF_POSSIBILITIES = 8;
    private static final int ANSI_VALUE_OF_DIGIT = 48;
    private static final int PERCENTAGE_MULTIPLIER = 100;

    private static final int MOVE_FORWARD = 0;
    private static final int MOVE_FORWARD_RIGHT = 1;
    private static final int MOVE_RIGHT = 2;
    private static final int MOVE_BACK_RIGHT = 3;
    private static final int MOVE_BACK = 4;
    private static final int MOVE_BACK_LEFT = 5;
    private static final int MOVE_LEFT = 6;
    private static final int MOVE_FORWARD_LEFT = 7;

    private static final Random RANDOM_NUMBER_GENERATOR = new Random();

    private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";

    private List<String> colors;
    private List<Ant> ants;
    private char[][] table;
    private int numberOfAnts;
    private int[] coverage;

    public Game(int numberOfAnts, char[][] table) {
        this.numberOfAnts = numberOfAnts;
        this.colors = addColors();
        this.ants = new ArrayList<>();
        this.table = table;
        this.coverage = new int[numberOfAnts];
    }

    public final void playGame() throws InterruptedException, IOException {
        fillArrayWithBlankSpaces();

        for (int i = 0; i < numberOfAnts; i++) {
            Random random = new Random();
            int newX = random.nextInt(table.length);
            int newY = random.nextInt(table[0].length);

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

            printMapOnConsole();

            sumOfAverages = 0.0;
            for (int i = 0; i < numberOfAnts; i++) {
                double avg = countAvgCoverage(coverage[i]);
                out.println("Ant" + i + " coverage: " + avg + "%");
                coverage[i] = 0;
                sumOfAverages += avg;
            }

            setPossibilities(lastMove);
        }

    }

    private boolean isMovePossible(int value, int rand, boolean possibility) {
        return rand == value && possibility;
    }

    private void setCorrectPositionIfAntIsOutOfBound(int i) {
        ants.get(i).setPositionX(ants.get(i).getPositionX() % table.length);
        ants.get(i).setPositionY(ants.get(i).getPositionY() % table[0].length);
        if (ants.get(i).getPositionX() < 0) {
            ants.get(i).setPositionX(ants.get(i).getPositionX() + table.length);
        }
        if (ants.get(i).getPositionY() < 0) {
            ants.get(i).setPositionY(ants.get(i).getPositionY() + table[0].length);
        }
    }

    private void makeMove(boolean[] possibilities, int[] lastMove, int i) {
        boolean moved = false;
        while (!moved) {
            int rand = RANDOM_NUMBER_GENERATOR.nextInt(NUMBER_OF_POSSIBILITIES);
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

    private void setPossibilities(int[] lastMove) {
        boolean[] possibilities = new boolean[NUMBER_OF_POSSIBILITIES];

        for (int i = 0; i < ants.size(); i++) {
            if (lastMove[i] == MOVE_FORWARD) {
                possibilities[MOVE_FORWARD_LEFT] = true;
                possibilities[MOVE_FORWARD] = true;
                possibilities[MOVE_FORWARD_RIGHT] = true;
            } else if (lastMove[i] == MOVE_FORWARD_RIGHT) {
                possibilities[MOVE_FORWARD] = true;
                possibilities[MOVE_FORWARD_RIGHT] = true;
                possibilities[MOVE_RIGHT] = true;
            } else if (lastMove[i] == MOVE_RIGHT) {
                possibilities[MOVE_FORWARD_RIGHT] = true;
                possibilities[MOVE_RIGHT] = true;
                possibilities[MOVE_BACK_RIGHT] = true;
            } else if (lastMove[i] == MOVE_BACK_RIGHT) {
                possibilities[MOVE_RIGHT] = true;
                possibilities[MOVE_BACK_RIGHT] = true;
                possibilities[MOVE_BACK] = true;
            } else if (lastMove[i] == MOVE_BACK) {
                possibilities[MOVE_BACK_RIGHT] = true;
                possibilities[MOVE_BACK] = true;
                possibilities[MOVE_BACK_LEFT] = true;
            } else if (lastMove[i] == MOVE_BACK_LEFT) {
                possibilities[MOVE_BACK] = true;
                possibilities[MOVE_BACK_LEFT] = true;
                possibilities[MOVE_LEFT] = true;
            } else if (lastMove[i] == MOVE_LEFT) {
                possibilities[MOVE_BACK_LEFT] = true;
                possibilities[MOVE_LEFT] = true;
                possibilities[MOVE_FORWARD_LEFT] = true;
            } else if (lastMove[i] == MOVE_FORWARD_LEFT) {
                possibilities[MOVE_FORWARD] = true;
                possibilities[MOVE_LEFT] = true;
                possibilities[MOVE_FORWARD_LEFT] = true;
            }

            makeMove(possibilities, lastMove, i);
            setCorrectPositionIfAntIsOutOfBound(i);

            table[ants.get(i).getPositionX()][ants.get(i).getPositionY()] = (char) (i + ANSI_VALUE_OF_DIGIT);
        }
    }

    private double countAvgCoverage(int placeCovered) {
        return (double) placeCovered / (table.length * table[0].length) * PERCENTAGE_MULTIPLIER;
    }

    private void printMapOnConsole() {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
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

    private void startMovesDependingOnSystem() throws IOException, InterruptedException {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            out.print("\033[H\033[2J");
        }
    }


    private List<String> addColors() {
        List<String> addedColors = new ArrayList<>();
        addedColors.add(ANSI_RED_BACKGROUND);
        addedColors.add(ANSI_GREEN_BACKGROUND);
        addedColors.add(ANSI_BLUE_BACKGROUND);
        addedColors.add(ANSI_YELLOW_BACKGROUND);
        addedColors.add(ANSI_PURPLE_BACKGROUND);
        return addedColors;
    }

    private void fillArrayWithBlankSpaces() {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                table[i][j] = ' ';
            }
        }
    }
}
