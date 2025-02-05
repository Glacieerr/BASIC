public class BinaryExpressionNode extends ExpressionNode {
    public enum Operator {
        ADD, SUBTRACT, MULTIPLY, DIVIDE, EQUALS // Extend
    }

    private ExpressionNode left;
    private ExpressionNode right;
    private Operator operator;

    public BinaryExpressionNode(ExpressionNode left, ExpressionNode right, Operator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    // Getters
    public ExpressionNode getLeft() {
        return left;
    }

    public ExpressionNode getRight() {
        return right;
    }

    public Operator getOperator() {
        return operator;
    }

    // Additional methods...
}