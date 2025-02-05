public class AssignmentNode extends StatementNode {
    private final VariableNode variable;
    private final Node value;

    public AssignmentNode(VariableNode variable, Node value) {
        this.variable = variable;
        this.value = value;
    }

    public VariableNode getVariable() {
        return variable;
    }

    public Node getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "AssignmentNode [variable=" + variable + ", value=" + value + "]";
    }
}