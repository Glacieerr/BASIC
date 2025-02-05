public class GosubNode extends StatementNode {
    private final String identifier;
    
    private String label; // The label of the subroutine to jump to

    public GosubNode(String identifier) {
        this.identifier = identifier;
        this.label = label;
    }

    public String getIdentifier() {
        return identifier;
    }
    
    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "GosubNode{" +
               "identifier='" + identifier + '\'' +
               '}';
    }
}