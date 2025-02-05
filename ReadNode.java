import java.util.List;

public class ReadNode extends StatementNode {
    private final List<VariableNode> variables;

    public ReadNode(List<VariableNode> variables) {
        this.variables = variables;
    }

    public List<VariableNode> getVariables() {
        return variables;
    }

    @Override
    public String toString() {
        return "ReadNode{" +
               "variables=" + variables +
               '}';
    }
}