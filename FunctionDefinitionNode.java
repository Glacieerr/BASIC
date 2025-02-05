import java.util.List;
import java.util.function.Function;

public abstract class FunctionDefinitionNode {
    // Name of the function
    private String functionName;
    // Parameters of the function
    private List<String> parameters;
    // The body or action of the function
    private Function<List<InterpreterDataType>, InterpreterDataType> action;
    
    public FunctionDefinitionNode(String functionName) {
        this(functionName, List.of(), arguments -> null); // Provide default empty list and a no-op action
    }
    
    public String getName() {
        return this.functionName;
    }
    
    public FunctionDefinitionNode(String functionName, List<String> parameters, Function<List<InterpreterDataType>, InterpreterDataType> action) {
        this.functionName = functionName;
        this.parameters = parameters;
        this.action = action;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<String> getParameters() {
        return parameters;
    }

    // Execute the function with given arguments
    public InterpreterDataType execute(List<InterpreterDataType> arguments) {
        return action.apply(arguments);
    }
    
    public class BuiltInFunctionDefinitionNode extends FunctionDefinitionNode {
        private Function<List<InterpreterDataType>, InterpreterDataType> execute;
        private boolean isVariadic;

        public BuiltInFunctionDefinitionNode(String name, Function<List<InterpreterDataType>, InterpreterDataType> execute, boolean isVariadic) {
            super(name); // Call the superclass constructor with the function name
            this.execute = execute;
            this.isVariadic = isVariadic;
        }

        @Override
        public InterpreterDataType execute(List<InterpreterDataType> arguments) {
            // Delegate to the lambda function for execution
            return execute.apply(arguments);
        }
        
        public boolean isVariadic() {
            return isVariadic;
        }
    }
}