import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        while (true)
            try {
                new CommandLineInterface().start();
            } catch (Exception e){
                System.err.println("An Error Occurred: " + e);
            }
    }
}
