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

    @Override
    public String toString() {
        return "PIF:" + pairs.stream()
                .map(Pair::toString)
                .reduce("", (s, t) -> s + "\n" + t);
    }
}
