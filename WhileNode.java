public class WhileNode extends StatementNode {
    private final Node condition; // A boolean expression node
    private final StatementNode body;

    public WhileNode(Node condition, StatementNode body) {
        this.condition = condition;
        this.body = body;
    }
    
    @Override
    public String toString() {
        return "WhileNode{" +
               "condition=" + condition.toString() +
               ", body=" + body.toString() +
               '}';
    }
}