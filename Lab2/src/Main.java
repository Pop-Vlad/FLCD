public class Main {

    public static void main(String[] args) {
        SymbolTable st = new SymbolTable(4);
        System.out.println(st.pos("1"));
        System.out.println(st.pos("abc"));
        System.out.println(st.pos("d"));
        System.out.println(st.pos("e"));
        System.out.println(st.pos("e"));
        System.out.println(st.pos("d"));
        System.out.println(st.pos("abc"));
        System.out.println(st.pos("1"));
        System.out.println(st.getTokenFromPos(new Pair<>(0, 0)));
        System.out.println(st.getTokenFromPos(new Pair<>(1, 0)));
        System.out.println(st.getTokenFromPos(new Pair<>(1, 1)));

        System.out.println(st.pos("3"));
        System.out.println(st.pos("abc"));
        System.out.println(st.pos("2"));
        System.out.println(st.getTokenFromPos(new Pair<>(2, 0)));
        System.out.println(st.getTokenFromPos(new Pair<>(2, 1)));
        System.out.println(st.getTokenFromPos(new Pair<>(3, 0)));

        System.out.println("\nInternal structure of the symbol table:");
        System.out.println(st.toString());
    }
}