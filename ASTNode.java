import java.util.ArrayList;
import java.util.List;

public abstract class ASTNode {
    private List<ASTNode> children = new ArrayList<>();
    public abstract void accept(ASTNodeVisitor visitor);

    public void addChild(ASTNode child) {
        if (child != null) {
            children.add(child);
        }
    }

    public List<ASTNode> getChildren() {
        return new ArrayList<>(children); // Return copy to prevent external modifications
    }
    
    interface ASTNodeVisitor {
        void visit(ASTNode node);
    }
}