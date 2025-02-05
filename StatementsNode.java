import java.util.List;

public class StatementsNode extends Node {
    private final List<StatementNode> statements;

    public StatementsNode(List<StatementNode> statements) {
        this.statements = statements;
    }

    public List<StatementNode> getStatements() {
        return statements;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("StatementsNode [\n");
        for (StatementNode statement : statements) {
            sb.append("  ").append(statement).append("\n");
        }
        sb.append("]");
        return sb.toString();
    }
}