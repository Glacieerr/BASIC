public class WhileStatementNode extends StatementNode {
    private final Node condition;
    private final Node whileBlock;

    public WhileStatementNode(Node condition, Node whileBlock) {
        this.condition = condition;
        this.whileBlock = whileBlock;
    }

    public Node getCondition() {
        return condition;
    }

    public Node getWhileBlock() {
        return whileBlock;
    }

    @Override
    public String toString() {
        return "WhileStatementNode{" +
               "condition=" + condition +
               ", whileBlock=" + whileBlock +
               '}';
    }
}