public class SymbolTable {

    private final Node[] table;
    private final int size = 1000;

    public SymbolTable() {
        this.table = new Node[this.size];
    }

    public SymbolTable(int size) {
        this.table = new Node[this.size];
    }

    public int pos(String token) {
        Pair<Integer, Integer> result = this.search(token);
        if (result.getSecond() == -1) {
            return this.add(token);
        } else {
            return result.getFirst() + result.getSecond() * this.size;
        }
    }

    public String getTokenFromPos(int pos) {
        int index1 = pos % this.size;
        int index2 = pos / this.size;
        Node current = table[index1];
        for (int i = 0; i < index2; i++) {
            current = current.next;
        }
        return current.token;
    }

    private Pair<Integer, Integer> search(String token) {
        int index1 = this.hash(token) % this.size;
        if (table[index1] == null) {
            return new Pair<>(index1, -1);
        }
        int index2 = 0;
        Node current = table[index1];
        while (current != null && !current.token.equals(token)) {
            current = current.next;
            index2++;
        }
        if (current == null) {
            return new Pair<>(index1, -1);
        }
        return new Pair<>(index1, index2);
    }

    private int add(String token) {
        int index = this.hash(token) % this.size;
        Node newNode = new Node(null, null, token);
        Node current = table[index];
        int pos = 0;
        if (current == null) {
            table[index] = newNode;
            return index;
        } else {
            while (current.next != null) {
                current = current.next;
                pos++;
            }
            current.next = newNode;
            newNode.previous = current;
            return pos * this.size + index;
        }

    }

    private int hash(String token) {
        return token.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("The symbol table is represented using a hash table\n");
        sb.append("The contents of the symbol table are:\n");
        for (int i = 0; i < table.length; i++) {
            Node current = table[i];
            if (current == null) {
                continue;
            }
            sb.append(i).append(": ");
            while (current != null) {
                sb.append(current.token).append(" ");
                current = current.next;
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private class Node {

        public Node previous;
        public Node next;
        public String token;

        public Node(Node previous, Node next, String token) {
            this.previous = previous;
            this.next = next;
            this.token = token;
        }
    }
}
