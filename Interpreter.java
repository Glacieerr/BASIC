import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.Queue;

public class Interpreter {
    // Storage for variables by type
    private Map<String, Integer> integerVariables = new HashMap<>();
    private Map<String, Float> floatVariables = new HashMap<>();
    private Map<String, String> stringVariables = new HashMap<>();

    // Label optimization
    private Map<String, LabeledStatementNode> labeledStatements = new HashMap<>();

    // Data for READ optimization
    private Queue<Object> dataQueue = new LinkedList<>();
    private Map<String, InterpreterDataType> variables = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);
    
    private boolean loop = true;
    private StatementNode currentStatement;
    private Stack<StatementNode> callStack = new Stack<>();
    private Map<String, StatementNode> labelMap; // labelMap is populated during AST construction
    private Map<String, Integer> intVariables = new HashMap<>();
    private Stack<StatementNode> controlFlowStack = new Stack<>();
    private int currentStatementIndex = 0;

    public Interpreter() {
        // Initialize variable storage
        integerVariables = new HashMap<>();
        floatVariables = new HashMap<>();
        stringVariables = new HashMap<>();

        // Initial data queue for DATA statement values
        dataQueue = new LinkedList<>();

        // Initial map for labeled statements for quick access
        labeledStatements = new HashMap<>();
    }
    
    
	// LEFT$ function
    public static String left(String data, int characters) {
        return data.substring(0, Math.min(characters, data.length()));
    }

    // RIGHT$ function
    public static String right(String data, int characters) {
        return data.length() > characters ? data.substring(data.length() - characters) : data;
    }

    // MID$ function
    public static String mid(String data, int start, int characters) {

    	int endIndex = Math.min(data.length(), start + characters - 1);
        if (start > data.length() || start < 1) return "";
        return data.substring(start - 1, endIndex);
    }

    // NUM$ function (for int)
    public static String num$(int number) {
        return String.valueOf(number);
    }

    // NUM$ function (for float)
    public static String num$(float number) {
        return String.format("%f", number);
    }

    // VAL function (convert string to float)
    public static float val(String data) {
        try {
            return Float.parseFloat(data.trim());
        } 
        catch (NumberFormatException e) {
            return 0.0f; // BASIC typically returns 0 for invalid numbers
        }
    }

    // VAL% function (convert string to int)
    public static int valP(String data) {
        try {
            return Integer.parseInt(data.trim());
        } 
        catch (NumberFormatException e) {
            return 0; // BASIC typically returns 0 for invalid numbers
        }
    }

    // RANDOM() function returns a random float between inclusive and exclusive
    public static float random() {
        return new java.util.Random().nextFloat();
    }
    
    private void interpretReadNode(ReadNode node) {
        // set ReadNode.getVariables() returns a List<VariableNode>
        for (VariableNode varNode : node.getVariables()) {
            String varName = varNode.getName(); // Correctly extract the variable name from VariableNode
            
            if (dataQueue.isEmpty()) {
                throw new RuntimeException("Insufficient data for READ statement.");
            }
            
            Object dataObj = dataQueue.poll(); // Get the next data value as an Object
            
            // Check if the dequeued object is an instance of InterpreterDataType
            if (!(dataObj instanceof InterpreterDataType)) {
                throw new RuntimeException("Data type mismatch: Expected InterpreterDataType but found " + dataObj.getClass().getSimpleName());
            }
            
            InterpreterDataType dataValue = (InterpreterDataType) dataObj; // Safe to cast
            variables.put(varName, dataValue); // Store the data value in the variables map
        }
    }

    private void interpretAssignNode(AssignNode node) {
        // Extract the variable name from the AssignNode
        String variableName = node.getVariableName();

        // Evaluate the expression on the right-hand side of the assignment
        InterpreterDataType evaluatedResult = evaluate(node.getExpression());

        // Check if the variable already exists in the map
        if (variables.containsKey(variableName)) {
            // If the variable exists, update its value
            variables.get(variableName).setValue(evaluatedResult.getValue());
        } 
        else {
            // If the variable does not exist, add it to the map
            variables.put(variableName, evaluatedResult);
        }
    }


    private void interpretInputNode(InputNode node) {
        System.out.print(node.getPrompt());
        String input = scanner.nextLine();
        // Process input and update variables map
    }

    private void interpretPrintNode(PrintNode node) {
        node.getItemsToPrint().forEach(item -> {
            // Evaluate if necessary and print each item
            System.out.print(item + " ");
        });
        System.out.println();
    }
    
    // Evaluate methods
    private InterpreterDataType evaluate(ExpressionNode node) {
        // Implementation depends on  r AST structure
        return new InterpreterDataType(InterpreterDataType.DataType.STRING, "");
    }
    
    public Interpreter(Map<String, StatementNode> labelMap) {
        this.labelMap = labelMap;
    }

    public void interpret(StatementNode startNode) {
        currentStatement = startNode;
        while (loop && currentStatement != null) {
            if (currentStatement instanceof IfNode) {
                // Process IfNode
            } 
            else if (currentStatement instanceof GosubNode) {
                // Process GosubNode
            } 
            else if (currentStatement instanceof ReturnNode) {
                // Process ReturnNode
            } 
            else if (currentStatement instanceof ForNode) {
                // Process ForNode
            } 
            else if (currentStatement instanceof NextNode) {
                // Process NextNode
            } 
            else if (currentStatement instanceof EndNode) {
                loop = false; // Terminate the loop
            }

            if (loop) {
                currentStatement = currentStatement.getNext(); // Move to next statement unless loop is terminated
            }
        }
    }
    
    private void processIfNode(IfNode node) {
        boolean conditionResult = evaluateCondition(node.getCondition());
        if (conditionResult) {
            String label = node.getLabel();
            StatementNode labelStatement = labelMap.get(label);
            if (labelStatement != null) {
                currentStatement = labelStatement;
            } 
            else {
                throw new RuntimeException("Label not found: " + label);
            }
        }
    }
    
    private boolean evaluateCondition(ExpressionNode node) {
    	if (node instanceof BinaryExpressionNode) {
            BinaryExpressionNode binaryNode = (BinaryExpressionNode) node;
            // Set evaluate() method returns an InterpreterDataType with the evaluated result
            InterpreterDataType leftResult = evaluate(binaryNode.getLeft());
            InterpreterDataType rightResult = evaluate(binaryNode.getRight());

            // Check for demonstration purposes
            if (binaryNode.getOperator() == BinaryExpressionNode.Operator.EQUALS) {
                // Check if both sides are equal
                return leftResult.getValue().equals(rightResult.getValue());
            }
        }
        return false;
    }


    private void processGosubNode(GosubNode node) {
        // Check if the next statement exists and push it onto the stack for return
        if (currentStatement.getNext() != null) {
            controlFlowStack.push(currentStatement.getNext());
        } 
        else {
            // Handle the case where there's no next statement, though this situation might indicate a design or parsing issue
            throw new RuntimeException("GOSUB without a return address.");
        }

        // Find the subroutine's start based on its label
        StatementNode subroutineStart = labelMap.get(node.getLabel());
        if (subroutineStart == null) {
            // Handle the case where the label doesn't exist in the program, runtime error
            throw new RuntimeException("Label for GOSUB not found: " + node.getLabel());
        }

        // Jump to the subroutine by setting currentStatement to the subroutine's start
        currentStatement = subroutineStart;
    }

    private void processReturnNode(ReturnNode node) {
        if (controlFlowStack.isEmpty()) {
            // Handle the error or end execution if there's no return address
            throw new RuntimeException("RETURN without GOSUB");
        }

        // Pop the return address from the stack
        StatementNode returnAddress = controlFlowStack.pop();

        // Update currentStatement to continue execution from the return address
        currentStatement = returnAddress;
    }


    private void processForNode(ForNode node) {
        // Initialize or update the loop variable
        String varName = node.getVariableName();
        InterpreterDataType startValue = evaluate(node.getStartExpression()); // Use the existing evaluate method

        // Update the variable in the map
        variables.put(varName, startValue);
        
        //added getEndExpression to ForNode
        InterpreterDataType endValue = evaluate(node.getEndExpression());

        // Check if the loop's continuation condition holds
        if (startValue.getType() == InterpreterDataType.DataType.INTEGER && endValue.getType() == InterpreterDataType.DataType.INTEGER) {
            int startIntValue = (Integer) startValue.getValue();
            int endIntValue = (Integer) endValue.getValue();

            if (startIntValue <= endIntValue) {
                // Execute the loop body, Push the ForNode itself onto the controlFlowStack with updated currentStatement for the next iteration
                controlFlowStack.push(node.getNext()); // getNext() gets the statement following this FOR in the AST
                currentStatement = node.getNext(); // Proceed to the next statement in the loop
            } 
            else {
                // Loop terminates, find the NEXT statement that matches this FOR loop
                skipToNextStatementForLoop(varName);
            }
        } 
        else {
            // Handle type mismatch or error
            throw new IllegalArgumentException("Type mismatch or unexpected error occurred.");
        }
    }


    private int evaluateToInt(ExpressionNode node) {
        if (node instanceof IntegerLiteralNode) {
            // Directly return the value for integer literals
            IntegerLiteralNode literalNode = (IntegerLiteralNode) node;
            return literalNode.getValue();
        } 
        else if (node instanceof BinaryExpressionNode) {
            // Recursively evaluate binary expressions
            BinaryExpressionNode binaryNode = (BinaryExpressionNode) node;
            int leftValue = evaluateToInt(binaryNode.getLeft());
            int rightValue = evaluateToInt(binaryNode.getRight());
            
            switch (binaryNode.getOperator()) {
                case ADD:
                    return leftValue + rightValue;
                case SUBTRACT:
                    return leftValue - rightValue;
                case MULTIPLY:
                    return leftValue * rightValue;
                case DIVIDE:
                    // Ensure no division by zero
                    if (rightValue == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    return leftValue / rightValue;
                default:
                    throw new IllegalArgumentException("Unsupported operator: " + binaryNode.getOperator());
            }
        } 
        else {
            // Handle other types of ExpressionNodes or throw an exception for unsupported nodes
            throw new IllegalArgumentException("Unsupported node type for evaluation: " + node.getClass().getName());
        }
    }


    private void skipToNextStatementForLoop(String varName) {
        boolean foundMatchingNext = false;
        StatementNode current = this.currentStatement; // Start from the current statement

        while (current != null && !foundMatchingNext) {
            if (current instanceof ForNode) {
                ForNode forNode = (ForNode) current;
                if (forNode.getVariableName().equals(varName)) {
                    // ForNode links directly to its corresponding NextNode, or have a mechanism to find it
                    StatementNode next = forNode.getNextNode(); // Have a way to get the next node for a ForNode
                    if (next != null) {
                        this.currentStatement = next; // Move execution to the NextNode
                        foundMatchingNext = true;
                    }
                    break; // Found the ForNode, break regardless of finding the NextNode or not
                }
            }
            // Move to the next statement in the sequence
            current = current.getNext(); // Each node knows its next node
        }

        if (!foundMatchingNext) {
            // Handle error no matching NEXT statement found for the FOR loop variable
            System.err.println("Error: No matching NEXT statement found for variable '" + varName + "'.");
            // Potentially throw an exception or halt execution as appropriate
        }
    }

    private void processNextNode(NextNode node) {
        if (controlFlowStack.isEmpty()) {
            throw new IllegalStateException("Next statement without a corresponding For statement");
        }

        Object top = controlFlowStack.peek();
        if (!(top instanceof ForNode)) {
            throw new IllegalStateException("Next statement not following a For statement");
        }

        ForNode forNode = (ForNode) controlFlowStack.pop();

        String varName = forNode.getVariableName();
        // Correctly extract and convert the current value
        InterpreterDataType currentDataType = variables.get(varName);
        int currentValue = currentDataType != null ? currentDataType.getIntValue() : 0; // Add getIntValue() method in InterpreterDataType

        // EvaluateToInt() method exists that handles evaluation and conversion
        int stepValue = forNode.getStepExpression() != null ? evaluateToInt(forNode.getStepExpression()) : 1;
        int newValue = currentValue + stepValue;
        
        // Wrap the newValue in an InterpreterDataType before updating the map
        variables.put(varName, new InterpreterDataType(InterpreterDataType.DataType.INTEGER, newValue));

        boolean conditionMet = stepValue >= 0 ? 
            newValue <= evaluateToInt(forNode.getEndExpression()) : 
            newValue >= evaluateToInt(forNode.getEndExpression());

        if (conditionMet) {
            controlFlowStack.push(forNode);
            currentStatement = forNode.getBody();
        }
        
        // If the condition is not met, the loop exits and execution continues with the next statement after this NextNode
        if (!conditionMet) {
        	// Used to track the current position in a statement list or array and execute the statements following the loop
            currentStatementIndex++;

            // Ensure that the currentStatementIndex does not exceed the bounds of statement list
            if (currentStatementIndex < labeledStatements.size()) {
                // Set the currentStatement to the next statement in the list
                currentStatement = labeledStatements.get(currentStatementIndex);
            } 
            else {
                // If there are no more statements to execute
                currentStatement = null; // Indicates no more statements to execute
            }
        }
    }
}