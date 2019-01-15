package pl.zjp;

import java.io.IOException;


public final class Main {

    private static final int NUMBER_OF_ANTS = 3;

    public static void main(String[] args) throws InterruptedException, IOException {

        Game game = new Game(NUMBER_OF_ANTS);
        if (!game.playGame()){
            System.exit(0);
        }
    }


}
