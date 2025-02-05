import java.util.List;

public class InputNode extends StatementNode {
    private final Node prompt; // StringNode or VariableNode
    private final List<VariableNode> variables;

    public InputNode(Node prompt, List<VariableNode> variables) {
        this.prompt = prompt;
        this.variables = variables;
    }

    public Node getPrompt() {
        return prompt;
    }

    public List<VariableNode> getVariables() {
        return variables;
    }

    @Override
    public String toString() {
        return "InputNode{" +
               "prompt=" + prompt +
               ", variables=" + variables +
               '}';
    }
}