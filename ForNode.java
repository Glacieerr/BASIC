public class ForNode extends StatementNode {
    private final String variableName; // Name of the loop variable
    private final ExpressionNode startExpression;
    private final ExpressionNode endExpression;
    private final ExpressionNode stepExpression; // Can be null if not provided
    private StatementNode body; // The loop body

    // Updated constructor to expect ExpressionNode for start, end, and step
    public ForNode(String variableName, ExpressionNode startExpression, ExpressionNode endExpression, ExpressionNode stepExpression, StatementNode body) {
        this.variableName = variableName;
        this.startExpression = startExpression;
        this.endExpression = endExpression;
        this.stepExpression = stepExpression;
        this.body = body;
    }

    // Getter for the variable name
    public String getVariableName() {
        return this.variableName;
    }

    // Getters for start, end, and step expressions
    public ExpressionNode getStartExpression() {
        return this.startExpression;
    }

    public ExpressionNode getEndExpression() {
        return this.endExpression;
    }

    public ExpressionNode getStepExpression() {
        return this.stepExpression; // This can be null
    }

    // Getter for the loop body
    public StatementNode getBody() {
        return this.body;
    }

    @Override
    public String toString() {
        String stepExprString = (stepExpression != null) ? ", Step: " + stepExpression.toString() : "";
        return "ForNode{" +
               "variableName='" + variableName + '\'' +
               ", Start: " + startExpression.toString() +
               ", End: " + endExpression.toString() +
               stepExprString +
               ", Body: " + (body != null ? body.toString() : "null") +
               '}';
    }
}