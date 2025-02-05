public class AssignNode extends StatementNode {
    private final String variableName;
    private final ExpressionNode expression;

    public AssignNode(String variableName, ExpressionNode expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    public String getVariableName() {
        return variableName;
    }

    public ExpressionNode getExpression() {
        return expression;
    }
    
    @Override
    public String toString() {
        return "AssignNode{" +
               "variableName='" + variableName + '\'' +
               ", expression=" + expression +
               '}';
    }
}