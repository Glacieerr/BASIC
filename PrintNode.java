import java.util.List;
import java.util.stream.Collectors;

public class PrintNode extends StatementNode {
    private final List<Node> itemsToPrint;

    public PrintNode(List<Node> itemsToPrint) {
        this.itemsToPrint = itemsToPrint;
    }

    public List<Node> getItemsToPrint() {
        return itemsToPrint;
    }

    @Override
    public String toString() {
        String printItemsString = itemsToPrint.stream()
                                              .map(Node::toString)
                                              .collect(Collectors.joining(", "));
        return "PrintNode [itemsToPrint=" + printItemsString + "]";
    }
}