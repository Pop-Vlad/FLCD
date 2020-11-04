public class MainScanner {

    private static final String program = "p2.txt";
    private static final String tokens = "token.in";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(program, tokens);
        scanner.scan();
    }
}
