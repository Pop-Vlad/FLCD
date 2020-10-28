import java.util.ArrayList;
import java.util.List;

public class ProgramInternalForm {

    List<Pair<String, Integer>> pairs;

    public ProgramInternalForm() {
        pairs = new ArrayList<>();
    }

    public void add(Pair<String, Integer> elem) {
        pairs.add(elem);
    }

    public Pair<String, Integer> get(int index) {
        return pairs.get(index);
    }

    public int size() {
        return pairs.size();
    }

    @Override
    public String toString() {
        return "PIF:" + pairs.stream()
                .map(Pair::toString)
                .reduce("", (s, t) -> s + "\n" + t);
    }
}
