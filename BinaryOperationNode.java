public class BinaryOperationNode extends Node {
    private final Node leftOperand;
    private final Token.TokenType operator;
    private final Node rightOperand;

    public BinaryOperationNode(Node leftOperand, Token.TokenType operator, Node rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
    }

    public Node getLeftOperand() {
        return leftOperand;
    }

    public Token.TokenType getOperator() {
        return operator;
    }

    public Node getRightOperand() {
        return rightOperand;
    }

    @Override
    public String toString() {
        return "BinaryOperationNode{" +
               "leftOperand=" + leftOperand +
               ", operator=" + operator +
               ", rightOperand=" + rightOperand +
               '}';
    }
}