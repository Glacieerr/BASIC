import java.util.List;

public class FunctionNode extends Node {
    private final String functionName;
    private final List<Node> arguments;

    public FunctionNode(String functionName, List<Node> arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
    }

    // Getters...
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FunctionNode{functionName='").append(functionName).append("', arguments=[");
        for (int i = 0; i < arguments.size(); i++) {
            builder.append(arguments.get(i).toString());
            if (i < arguments.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append("]}");
        return builder.toString();
    }
}