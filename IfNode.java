public class IfNode extends StatementNode {
    private ExpressionNode condition; // Set the condition is stored as an ExpressionNode
    private StatementNode trueBranch;
    private StatementNode falseBranch; // For if-else structures
    private String label; // The label to jump to if the condition is true

    public IfNode(ExpressionNode condition, StatementNode trueBranch, StatementNode falseBranch) {
        this.condition = condition;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
        this.label = label;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public StatementNode getTrueBranch() {
        return trueBranch;
    }

    public StatementNode getFalseBranch() {
        return falseBranch;
    }
    
	// Getter for the label
    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        String falseBranchString = (falseBranch != null) ? ", False Branch: " + falseBranch.toString() : "";
        return "IfNode{" +
               "Condition: " + condition.toString() +
               ", True Branch: " + trueBranch.toString() +
               falseBranchString +
               '}';
    }
}