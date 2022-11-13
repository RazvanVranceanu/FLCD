package uni.flcd;

import uni.flcd.finalAutomata.FinalAutomataProcessor;
import uni.flcd.finalAutomata.repository.FinalAutomata;

public class Main {

    private static final String INPUT_FILE = "src/main/resources/fa.txt";

    public static void main(String[] args) {
        FinalAutomata finalAutomata = new FinalAutomata();
        FinalAutomataProcessor finalAutomataProcessor = new FinalAutomataProcessor(INPUT_FILE, finalAutomata);
        finalAutomataProcessor.processLines();
    }
}