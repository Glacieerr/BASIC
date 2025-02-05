import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramNode {
    private List<DataStatementNode> dataStatements;
    private Map<String, LabeledStatementNode> labeledStatements;

    public ProgramNode() {
        this.dataStatements = new ArrayList<>();
        this.labeledStatements = new HashMap<>();
    }

    public void addDataStatement(DataStatementNode dataStatement) {
        this.dataStatements.add(dataStatement);
    }

    public List<DataStatementNode> getDataStatements() {
        return this.dataStatements;
    }

    public void addLabeledStatement(String label, LabeledStatementNode statement) {
        this.labeledStatements.put(label, statement);
    }

    public LabeledStatementNode getLabeledStatement(String label) {
        return this.labeledStatements.get(label);
    }
}