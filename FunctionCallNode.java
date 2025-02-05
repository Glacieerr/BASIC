import java.util.List;

public class FunctionCallNode extends Node {
	private final List<Node> arguments;
    private String functionName;

    public FunctionCallNode(List<Node> arguments) {
        this.arguments = arguments;
    }

    // Setter for functionName
    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }
    
    public FunctionCallNode(String functionName, List<Node> arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<Node> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        StringBuilder argsString = new StringBuilder();
        for (Node arg : arguments) {
            if (argsString.length() > 0) {
                argsString.append(", ");
            }
            argsString.append(arg.toString());
        }
        return "FunctionCallNode{" +
               "functionName='" + functionName + '\'' +
               ", arguments=[" + argsString + "]" +
               '}';
    }
}