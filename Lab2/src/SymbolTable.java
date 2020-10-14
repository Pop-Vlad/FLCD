public class SymbolTable {

    private Node[] table;
    private int size;

    public SymbolTable(){
        this.size = 1000;
        this.table = new Node[this.size];
    }

    public SymbolTable(int size) {
        this.size = size;
        this.table = new Node[this.size];
    }

    public Pair<Integer, Integer> pos(String token){
        Pair<Integer, Integer> result = this.search(token);
        if(result.getSecond() == -1){
            return  this.add(token);
        }
        else {
            return result;
        }
    }

    public String getTokenFromPos(Pair<Integer, Integer> pos){
        Node current = table[pos.getFirst()];
        for(int i=0; i<pos.getSecond(); i++){
            current = current.next;
        }
        return current.token;
    }

    private Pair<Integer, Integer> search(String token){
        int index1 = this.hash(token) % this.size;
        if(table[index1] == null) {
            return new Pair<>(index1, -1);
        }
        int index2 = 0;
        Node current = table[index1];
        while(current != null && !current.token.equals(token)){
            current = current.next;
            index2 ++;
        }
        if(current == null){
            return new Pair<>(index1, -1);
        }
        return new Pair<>(index1, index2);
    }

    private Pair<Integer, Integer> add(String token){
        int index = this.hash(token) % this.size;
        Node newNode = new Node(null, null, token);
        Node current = table[index];
        int pos = 0;
        if (current == null){
            table[index] = newNode;
            return new Pair<>(index, 0);
        }
        else{
            while(current.next != null){
                current = current.next;
                pos++;
            }
            current.next = newNode;
            newNode.previous = current;
            return new Pair<>(index, pos);
        }

    }

    private int hash(String token){
        return token.hashCode();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<table.length; i++){
            sb.append(i).append(": ");
            Node current = table[i];
            while(current != null){
                sb.append(current.token).append(" ");
                current = current.next;
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
