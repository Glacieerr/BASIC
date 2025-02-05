public class MathOpNode extends Node {
	
    public enum MathOp { ADD, SUBTRACT, MULTIPLY, DIVIDE }
    private final MathOp operation;
    private final Node left;
    private final Node right;

    public MathOpNode(MathOp operation, Node left, Node right) {
        this.operation = operation;
        this.left = left;
        this.right = right;
    }

    public MathOp getOperation() {
        return operation;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "MathOpNode(" + operation + ", " + left + ", " + right + ")";
    }
}