public class BooleanExpressionNode extends Node {
    private final Node leftOperand;
    private final RelationalOperator operator;
    private final Node rightOperand;

    public BooleanExpressionNode(Node leftOperand, RelationalOperator operator, Node rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
    }

    @Override
    public String toString() {
        return "BooleanExpressionNode{" +
               "leftOperand=" + leftOperand.toString() +
               ", operator=" + operator +
               ", rightOperand=" + rightOperand.toString() +
               '}';
    }
}