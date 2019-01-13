package pl.zjp;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.System.out;


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

    private static final int X = 30;
    private static final int Y = 60;

    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> colors = addColors();
        List<Ant> ants = new ArrayList<>();
        char[][] table = new char[X][Y];


        table = setSpacesInTable(table);

        int numberOfAnts = 3;
        int[] coverage = new int[numberOfAnts];


        for (int i = 0; i < numberOfAnts; i++) {
            Random random = new Random();

            int newX = random.nextInt(X);
            int newY = random.nextInt(Y);
            if (table[newX][newY] == ' ') {
                ants.add(new Ant(newX, newY));
                table[newX][newY] = Character.forDigit(i, 10);

            }
        }
        int[] lastMove = new int[numberOfAnts];

        double sumOfAverages = 0.0;

        while (sumOfAverages != 100.0) {
            Thread.sleep(200);

            startMovesDependingOnSystem();

            coverage = setColorsAndReturnCoverageValue(table, colors, coverage);
            

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

    private static int[] setColorsAndReturnCoverageValue(char[][] table, List<String> colors, int[] coverage) {
        for (int i = 0; i < X; i++) {
            for (int j = 0; j < Y; j++) {
                if (table[i][j] != ' ') {
                    out.print(colors.get(Character.digit(table[i][j], 10)) + " " + ANSI_RESET);
                    coverage[Character.digit(table[i][j], 10)]++;
                } else {
                    out.print(table[i][j]);
                }
            }
            out.println();
        }
        return coverage;
    }

    private static char[][] setSpacesInTable(char[][] table) {
        for (int i = 0; i < X; i++) {
            for (int j = 0; j < Y; j++) {
                table[i][j] = ' ';
            }
        }
        return table;
    }

    private static void setPosibilities(int numberOfAnts, List<Ant> ants, int[] lastMove, char[][] table) {
        boolean[] possibilities = new boolean[8];

        for (int i = 0; i < numberOfAnts; i++) {
            if (lastMove[i] == 0) {
                possibilities[7] = true;
                possibilities[0] = true;
                possibilities[1] = true;
            }
            else if (lastMove[i] == 1) {
                possibilities[0] = true;
                possibilities[1] = true;
                possibilities[2] = true;
            }
            else if (lastMove[i] == 2) {
                possibilities[1] = true;
                possibilities[2] = true;
                possibilities[3] = true;
            }
            else if (lastMove[i] == 3) {
                possibilities[2] = true;
                possibilities[3] = true;
                possibilities[4] = true;
            }
            else if (lastMove[i] == 4) {
                possibilities[3] = true;
                possibilities[4] = true;
                possibilities[5] = true;
            }
            else if (lastMove[i] == 5) {
                possibilities[4] = true;
                possibilities[5] = true;
                possibilities[6] = true;
            }
            else if (lastMove[i] == 6) {
                possibilities[5] = true;
                possibilities[6] = true;
                possibilities[7] = true;
            }
            else if (lastMove[i] == 7) {
                possibilities[0] = true;
                possibilities[6] = true;
                possibilities[7] = true;
            }


            boolean moved = false;
            while (!moved) {
                moved = changePosition(possibilities, ants, lastMove, i);

            }
            setAntsPosition(ants, i);


            table[ants.get(i).getPositionX()][ants.get(i).getPositionY()] = (char) (i + 48);

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

    private static boolean changePosition(boolean[] possibilities, List<Ant> ants, int[] lastMove, int i) {
        Random random = new Random();

        int rand = random.nextInt(8);
        lastMove[i] = rand;

        if (rand == 0 && possibilities[0]) {
            ants.get(i).setPositionX(ants.get(i).getPositionX()-1);
            return true;
        }
        else if (rand == 1 && possibilities[1]) {
            ants.get(i).setPositionX(ants.get(i).getPositionX()-1);
            ants.get(i).setPositionY(ants.get(i).getPositionY()+1);
            return true;
        }
        else if (rand == 2 && possibilities[2]) {
            ants.get(i).setPositionY(ants.get(i).getPositionY()+1);
            return true;
        }
        else if (rand == 3 && possibilities[3]) {
            ants.get(i).setPositionX(ants.get(i).getPositionX()+1);
            ants.get(i).setPositionY(ants.get(i).getPositionY()+1);
            return true;
        }
        else if (rand == 4 && possibilities[4]) {
            ants.get(i).setPositionX(ants.get(i).getPositionX()+1);
            return true;
        }
        else if (rand == 5 && possibilities[5]) {
            ants.get(i).setPositionX(ants.get(i).getPositionX()+1);
            ants.get(i).setPositionY(ants.get(i).getPositionY()-1);
            return true;

        }
        else if (rand == 6 && possibilities[6]) {
            ants.get(i).setPositionY(ants.get(i).getPositionY()-1);
            return true;

        }
        else if (rand == 7 && possibilities[7]) {
            ants.get(i).setPositionX(ants.get(i).getPositionX()-1);
            ants.get(i).setPositionY(ants.get(i).getPositionY()-1);
            return true;

        }
        return false;
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
        return (double)placeCovered/(x*y) * 100;
    }
}
