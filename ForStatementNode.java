public class ForStatementNode extends StatementNode {
    private final StatementNode initialization;
    private final Node condition;
    private final StatementNode update;
    private final Node forBlock;

    public ForStatementNode(StatementNode initialization, Node condition, StatementNode update, Node forBlock) {
        this.initialization = initialization;
        this.condition = condition;
        this.update = update;
        this.forBlock = forBlock;
    }

    public StatementNode getInitialization() {
        return initialization;
    }

    public Node getCondition() {
        return condition;
    }

    public StatementNode getUpdate() {
        return update;
    }

    public Node getForBlock() {
        return forBlock;
    }

    @Override
    public String toString() {
        return "ForStatementNode{" +
               "initialization=" + initialization +
               ", condition=" + condition +
               ", update=" + update +
               ", forBlock=" + forBlock +
               '}';
    }
}