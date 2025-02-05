import java.util.List;

public class IfStatementNode extends StatementNode {
    private final Node condition;
    private final Node ifBlock;
    private final Node elseBlock;

    public IfStatementNode(Node condition, Node ifBlock, Node elseBlock) {
        this.condition = condition;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    public Node getCondition() {
        return condition;
    }

    public Node getIfBlock() {
        return ifBlock;
    }

    public Node getElseBlock() {
        return elseBlock;
    }

    @Override
    public String toString() {
        return "IfStatementNode{" +
               "condition=" + condition +
               ", ifBlock=" + ifBlock +
               ", elseBlock=" + (elseBlock != null ? elseBlock : "null") +
               '}';
    }
}