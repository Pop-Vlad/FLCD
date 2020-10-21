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


    public Scanner(String programFile, String tokensFile) {
        this.programFile = programFile;
        this.tokensFile = tokensFile;
        this.tokens = new ArrayList<>();
        this.currentLine = 1;
        st = new SymbolTable();
        pif = new ProgramInternalForm();
    }

    public void scan() {
        boolean ok  =true;
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
                            pif.add(new Pair<>(token, index));
                        } else {
                            System.out.println("lexical error at line " + currentLine + ": " + token);
                            ok = false;
                            //return;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("lexical error at line " + currentLine + ": " + program.charAt(i));
                    i++;
                    ok  =false;
                    //return;
                }
            }
            BufferedWriter pifFile = new BufferedWriter(new FileWriter(new File("PIF.out")));
            pifFile.write(pif.toString());
            pifFile.close();
            BufferedWriter stFile = new BufferedWriter(new FileWriter(new File("ST.out")));
            stFile.write(st.toString());
            stFile.close();
            if(ok){
                System.out.println("lexically correct");
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private Pair<String, Integer> detect(String program, Integer pos) throws Exception {
        if (!String.valueOf(program.charAt(pos)).matches("[_a-zA-Z0-9{};():=*/%<>!\\-\\[\\]\"\\+\\s]")) {
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
        boolean aux = false;
        while (pos < program.length() && canBeToken(token.toString() + program.charAt(pos))) {
            aux = true;
            token.append(program.charAt(pos));
            pos++;
        }
        if (!aux) {
            pos++;
        }
        return new Pair<>(token.toString(), pos);
    }

    private boolean canBeToken(String toCheck) {
        if (toCheck.matches("[_a-zA-Z0-9\"]{0,100}")) {
            return true;
        }
        boolean ok = false;
        for (String s : tokens) {
            if (s.startsWith(toCheck)) {
                ok = true;
                break;
            }
        }
        return ok;
    }

    private boolean isIdentifierOrConstant(String token) {
        return token.matches("[a-zA-Z][a-zA-Z0-9]{0,100}")
                || token.matches("[0-9]{0,8}") //TODO
                || token.matches("\".\"");  //TODO
    }

    private List<String> readTokensFromFile() throws IOException {
        String tokensString = Files.readString(Path.of(tokensFile));
        return Arrays.asList(tokensString.split("\\s"));
    }
}