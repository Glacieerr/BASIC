public class NextNode extends StatementNode {
    private final String loopVariable;

    public NextNode(String loopVariable) {
        this.loopVariable = loopVariable;
    }

    public String getLoopVariable() {
        return loopVariable;
    }

    @Override
    public String toString() {
        return "NextNode{" +
               "loopVariable='" + loopVariable + '\'' +
               '}';
    }
}