import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MainFA {

    private static Map<String, Runnable> menu;
    private static FiniteAutomaton fa;

    public static void main(String[] args) throws Exception {
        fa = new FiniteAutomaton("FA.in");
        buildMenu();
        run();
        System.out.println("Is the sequence '101' accepted?");
        System.out.println(fa.isAccepted("101"));
        System.out.println("Is the sequence '1001' accepted?");
        System.out.println(fa.isAccepted("1001"));

//        System.out.println(fa.isAccepted("abc"));
//        System.out.println(fa.isAccepted("ab12"));
//        System.out.println(fa.isAccepted("1001"));
//        System.out.println(fa.isAccepted("123"));
//        System.out.println(fa.isAccepted("+123"));
//        System.out.println(fa.isAccepted("-123"));
//        System.out.println(fa.isAccepted("0"));
//        System.out.println(fa.isAccepted("\"a\""));
//
//        System.out.println(fa.isAccepted("1a23"));
//        System.out.println(fa.isAccepted("\"ab\""));
    }

    public static void run() {
        String input;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            input = scanner.nextLine();
            if (input.equals("5")) {
                return;
            } else {
                menu.get(input).run();
            }
        }
    }

    public static void buildMenu() {
        menu = new HashMap<>();
        menu.put("1", MainFA::printStates);
        menu.put("2", MainFA::printAlphabet);
        menu.put("3", MainFA::printTransitions);
        menu.put("4", MainFA::printFinalStates);
        System.out.println("Menu:\n" +
                "1 - states\n" +
                "2 - alphabet\n" +
                "3 - transitions\n" +
                "4 - final states\n" +
                "5 - exit\n");
    }

    public static void printStates() {
        List<String> states = fa.getStates();
        System.out.println(
                states.stream()
                        .reduce("", (s, t) -> s + " " + t)
        );
    }

    public static void printAlphabet() {
        List<String> alphabet = fa.getAlphabet();
        System.out.println(
                alphabet.stream()
                        .reduce("", (s, t) -> s + " " + t)
        );
    }

    public static void printTransitions() {
        Map<String, List<Pair<String, String>>> transitions = fa.getTransitions();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<Pair<String, String>>> entry : transitions.entrySet()) {
            for (Pair<String, String> p : entry.getValue()) {
                sb.append(entry.getKey()).append(p.getFirst()).append(p.getSecond()).append("\n");
            }
        }
        System.out.println(sb.toString());
    }

    public static void printFinalStates() {
        List<String> finalStates = fa.getFinalStates();
        System.out.println(
                finalStates.stream()
                        .reduce("", (s, t) -> s + " " + t)
        );
    }
}
