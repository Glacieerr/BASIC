import java.util.List;

public class DataNode extends StatementNode {
    private final List<Node> dataItems;

    public DataNode(List<Node> dataItems) {
        this.dataItems = dataItems;
    }

    public List<Node> getDataItems() {
        return dataItems;
    }

    @Override
    public String toString() {
        return "DataNode{" +
               "dataItems=" + dataItems +
               '}';
    }
}