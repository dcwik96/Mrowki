package pl.zjp;

import java.io.IOException;


public final class Main {
    private static final int X = 30;
    private static final int Y = 60;
    private static final int NUMBER_OF_ANTS = 3;

    public static void main(String[] args) throws InterruptedException, IOException {

        char[][] table = new char[X][Y];
        Game game = new Game(NUMBER_OF_ANTS, table);
        game.playGame();
    }


}
