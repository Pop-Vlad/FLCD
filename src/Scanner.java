import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scanner {

    private final String programFile;
    private final String tokensFile;
    private final SymbolTable st;
    private final ProgramInternalForm pif;
    private List<String> tokens;
    private int currentLine;
    private FiniteAutomaton fa;

    public Scanner(String programFile, String tokensFile) {
        this.programFile = programFile;
        this.tokensFile = tokensFile;
        this.tokens = new ArrayList<>();
        this.currentLine = 1;
        st = new SymbolTable();
        pif = new ProgramInternalForm();
        try {
            this.fa = new FiniteAutomaton("FA-identifier-or-constant.in");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void scan() {
        boolean ok = true;
        try {
            tokens = readTokensFromFile();
            String program = Files.readString(Path.of(programFile));

            int i = 0;
            while (i < program.length()) {
                try {
                    Pair<String, Integer> p = detect(program, i);

                    i = p.getSecond();
                    String token = p.getFirst();
                    if (!token.equals("")) {
                        if (tokens.contains(token)) {
                            pif.add(new Pair<>(token, -1));
                        } else if (isIdentifierOrConstant(token)) {
                            int index = st.pos(token);
                            if (isIdentifier(token)) {
                                pif.add(new Pair<>("0", index));
                            } else {
                                pif.add(new Pair<>("1", index));
                            }
                        } else {
                            System.out.println("lexical error at line " + currentLine + ": " + token);
                            ok = false;
                            //return;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("lexical error at line " + currentLine + ": " + program.charAt(i));
                    i++;
                    ok = false;
                    //return;
                }
            }
            BufferedWriter pifFile = new BufferedWriter(new FileWriter(new File("PIF.out")));
            pifFile.write(pif.toString());
            pifFile.close();
            BufferedWriter stFile = new BufferedWriter(new FileWriter(new File("ST.out")));
            stFile.write(st.toString());
            stFile.close();
            if (ok) {
                System.out.println("lexically correct");
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private Pair<String, Integer> detect(String program, Integer pos) throws Exception {
        if (!String.valueOf(program.charAt(pos)).matches("[a-zA-Z0-9{};():=*/%<>!\\-\\[\\]\"\\+\\s]")) {
            throw new Exception(String.valueOf(program.charAt(pos)));
        }
        StringBuilder token = new StringBuilder();
        if (Character.isWhitespace(program.charAt(pos))) {
            pos++;
            if (program.charAt(pos) == '\n') {
                currentLine++;
            }
            return new Pair<>(token.toString(), pos);
        }
        if (program.charAt(pos) == '"') {
            token.append(program.charAt(pos));
            pos++;
            token.append(program.charAt(pos));
            pos++;
            token.append(program.charAt(pos));
            pos++;
            if(fa.isAccepted(token.toString())){
                return new Pair<>(token.toString(), pos);
            }
            else {
                return new Pair<>("", pos);
            }
        }
        boolean aux = false;
        while (pos < program.length() && canBeToken(token.toString() + program.charAt(pos))) {
            aux = true;
            token.append(program.charAt(pos));
            pos++;
        }
        if (!aux) {
            pos++;
        }
        if (token.toString().equals("+") || token.toString().equals("-")) {
            if (pif.get(pif.size() - 1).getFirst().equals(":=")) {
                Pair<String, Integer> nextToken = detect(program, pos);
                return new Pair<>(token + nextToken.getFirst(), nextToken.getSecond());
            }
        }
        return new Pair<>(token.toString(), pos);
    }

    private boolean canBeToken(String toCheck) {
        boolean ok = false;
        for (String s : tokens) {
            if (s.startsWith(toCheck)) {
                ok = true;
                break;
            }
        }
        return ok || toCheck.matches("[a-zA-Z0-9]{0,100}");
    }

    private boolean isIdentifierOrConstant(String token) {
        try {
            return fa.isAccepted(token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isIdentifier(String token) {
        return token.matches("[a-zA-Z][a-zA-Z0-9]{0,100}");
    }

    private boolean isConstant(String token) {
        return token.matches("[-+]?[1-9][0-9]{0,8}") || token.matches("0")
                || token.matches("\"[a-zA-Z0-9]\"");
    }

    private List<String> readTokensFromFile() throws IOException {
        String tokensString = Files.readString(Path.of(tokensFile));
        return Arrays.asList(tokensString.split("\\s"));
    }
}
