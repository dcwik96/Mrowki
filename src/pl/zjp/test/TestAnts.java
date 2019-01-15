package pl.zjp.test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import pl.zjp.Main;

import java.io.IOException;

public class TestAnts {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
    
    @Test
    public void textMainIsRunning() throws IOException, InterruptedException {
        exit.expectSystemExitWithStatus(0);
        Main.main(null);
    }

}
