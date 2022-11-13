package uni.flcd;

import lombok.extern.slf4j.Slf4j;
import uni.flcd.finalAutomata.FinalAutomataProcessor;
import uni.flcd.finalAutomata.model.State;
import uni.flcd.finalAutomata.model.Transition;
import uni.flcd.finalAutomata.repository.FinalAutomata;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static uni.flcd.finalAutomata.model.StateType.FINAL;
import static uni.flcd.finalAutomata.model.StateType.INITIAL;

@Slf4j
public class Main {

    private static final String INPUT_FILE = "src/main/resources/fa.txt";
    private static final String INPUT_FILE1 = "src/main/resources/faIntegerConstant.txt";

    public static void main(String[] args) {
        FinalAutomata finalAutomata = new FinalAutomata();
        FinalAutomataProcessor finalAutomataProcessor = new FinalAutomataProcessor(INPUT_FILE1, finalAutomata);
        finalAutomataProcessor.processLines();

        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. states \n2. alphabet\n3. transitions\n4. initial state\n5. final states\n6. validate word");
            System.out.print("$");
            String option = scan.nextLine();
            switch (option) {
                case "1" -> finalAutomata.getStates()
                        .keySet()
                        .forEach(state -> System.out.println(state.getLabel()));
                case "2" -> System.out.println(finalAutomata.getAlphabet());
                case "3" -> {
                    Map<State, List<Transition>> states = finalAutomata.getStates();
                    states.keySet()
                            .forEach(key -> states.get(key)
                                    .forEach(transition -> System.out.println(key.getLabel() + transition.toString())));
                }
                case "4" -> finalAutomata.getStates()
                        .keySet()
                        .stream()
                        .filter(state -> INITIAL.equals(state.getStateType()))
                        .forEach(System.out::println);
                case "5" -> finalAutomata.getStates()
                        .keySet()
                        .stream()
                        .filter(state -> FINAL.equals(state.getStateType()))
                        .forEach(System.out::println);
                case "6" -> System.out.println(finalAutomataProcessor.validateWord(scan.nextLine()));
                default -> log.error("Unknown command");
            }
        }
    }
}