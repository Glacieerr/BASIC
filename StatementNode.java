public abstract class StatementNode extends Node {
	protected StatementNode next;

    public StatementNode getNext() {
        return next;
    }

    public void setNext(StatementNode next) {
        this.next = next;
    }
    
	// Getter for the next node
    public StatementNode getNextNode() {
        return this.next;
    }

    // Setter for the next node
    public void setNextNode(StatementNode next) {
        this.next = next;
    }
}