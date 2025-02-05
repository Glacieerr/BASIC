import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Parser {
    private TokenManager tokenManager;

    public Parser(LinkedList<Token> tokens) {
        this.tokenManager = new TokenManager(tokens);
    }

    public Node parse() {
    	if (!tokenManager.moreTokens()) {
            throw new IllegalStateException("No tokens to parse");
        }
    	
        return statements();
    }

    private Node expression() {
        Node left = term();

        while (tokenManager.moreTokens()) {
            Optional<Token> optToken = tokenManager.peek(0);
            if (!optToken.isPresent()) break;

            Token token = optToken.get();
            switch (token.getType()) {
                case PLUS:
                case MINUS:
                    tokenManager.matchAndRemove(token.getType());
                    Node right = term();
                    left = new MathOpNode(
                        token.getType() == Token.TokenType.PLUS ? MathOpNode.MathOp.ADD : MathOpNode.MathOp.SUBTRACT,
                        left, 
                        right
                    );
                    break;
                default:
                    return left;
            }
        }
        return left;
    }

    private Node term() {
        Node left = factor();

        while (tokenManager.moreTokens()) {
            Optional<Token> optToken = tokenManager.peek(0);
            if (!optToken.isPresent()) break;

            Token token = optToken.get();
            switch (token.getType()) {
                case MULTIPLY:
                case DIVIDE:
                    tokenManager.matchAndRemove(token.getType());
                    Node right = factor();
                    left = new MathOpNode(
                        token.getType() == Token.TokenType.MULTIPLY ? MathOpNode.MathOp.MULTIPLY : MathOpNode.MathOp.DIVIDE,
                        left, 
                        right
                    );
                    break;
                default:
                    return left;
            }
        }
        return left;
    }

    private Node factor() {
    	
    	Token token = tokenManager.peek(0).orElseThrow();
        if (token.getType() == Token.TokenType.WORD) {
            tokenManager.matchAndRemove(Token.TokenType.WORD); // Consume the variable token
            return new VariableNode(token.getValue()); // Create and return a VariableNode
        }
    	
        if (!tokenManager.moreTokens()) {
            throw new IllegalStateException("Unexpected end of tokens in factor");
        }

        Optional<Token> optToken = tokenManager.peek(0);
        if (!optToken.isPresent()) {
            throw new IllegalStateException("Unexpected token in factor");
        }

        switch (token.getType()) {
            case NUMBER:
                tokenManager.matchAndRemove(Token.TokenType.NUMBER);
                return new IntegerNode(Integer.parseInt(token.getValue()));
            case FLOAT:
                tokenManager.matchAndRemove(Token.TokenType.FLOAT);
                return new FloatNode(Float.parseFloat(token.getValue()));
                
            default:
                throw new IllegalStateException("Unexpected token type in factor: " + token.getType());
        }
    }

    private boolean acceptSeparators() {
        boolean found = false;
        while (tokenManager.peek(0).isPresent() &&
               tokenManager.peek(0).get().getType() == Token.TokenType.ENDOFLINE) {
            tokenManager.matchAndRemove(Token.TokenType.ENDOFLINE);
            found = true;
        }
        return found;
    }
    
    private Node statements() {
        List<StatementNode> statementList = new ArrayList<>();
        
        while (tokenManager.moreTokens()) {
            Optional<Token> optToken = tokenManager.peek(0);
            if (!optToken.isPresent()) {
                break; // End of tokens, exit the loop
            }

            Token token = optToken.get();
            switch (token.getType()) {
                case END:
                case GOSUB:
                    // Here parseActualStatement() should handle specific statement types
                    StatementNode statement = parseActualStatement();
                    statementList.add(statement);
                    break;
                default:
                    // Other statement parsing logic
                    StatementNode statement1 = statement(); // statement() is a general parsing method
                    if (statement1 != null) {
                        statementList.add(statement1);
                    }
                    break;
            }
        }
        return new StatementsNode(statementList);
    }
    
    private StatementNode statement() {
        Optional<Token> labelTokenOpt = tokenManager.peek(0);
        
        String label = null;
        if (labelTokenOpt.isPresent() && labelTokenOpt.get().getType() == Token.TokenType.LABEL) {
            label = labelTokenOpt.get().getValue(); // Extract label
            tokenManager.matchAndRemove(Token.TokenType.LABEL); // Consume label token
            labelTokenOpt = tokenManager.peek(0); // Update token after consuming label
        }

        if (!labelTokenOpt.isPresent()) {
            return null; // No more tokens to process
        }

        Token token = labelTokenOpt.get();
        StatementNode stmt;
        switch (token.getType()) {
            case PRINT:
                stmt = printStatement(); // Handle print statements
                break;
            case WORD:
                // Check for assignment
                if (tokenManager.peek(1).isPresent() && tokenManager.peek(1).get().getType() == Token.TokenType.EQUALS) {
                    stmt = assignment(); // Handle assignment statements
                } 
                else {
                    stmt = null; // Or handle other WORD cases
                }
                break;
            case IF:
                stmt = ifStatement(); // Handle if statements
                break;
            case WHILE:
                stmt = whileStatement(); // Handle while statements
                break;
            case FOR:
                stmt = forStatement(); // Handle for statements
                break;
            case READ:
                stmt = readStatement(); // Parse READ statement
                break;
            case DATA:
                stmt = dataStatement(); // Parse DATA statement
                break;
            case INPUT:
                stmt = inputStatement(); // Parse INPUT statement
                break;

            default:
                throw new IllegalStateException("Unrecognized statement type: " + token.getType());
        }

        if (label != null) {
            return new LabeledStatementNode(label, stmt);
        }
        return stmt;
    }
    
    private StatementNode parseActualStatement() {
        Optional<Token> nextTokenOpt = tokenManager.peek(0);
        if (!nextTokenOpt.isPresent()) {
            throw new IllegalStateException("Unexpected end of input");
        }

        Token nextToken = nextTokenOpt.get();
        switch (nextToken.getType()) {
            case PRINT:
                return printStatement();
            case GOSUB:
                return gosubStatement();
            case RETURN:
                return returnStatement();
            case FOR:
                return forStatement();
            case NEXT:
                return nextStatement();
            case IF:
                return ifStatement();
            case WHILE:
                return whileStatement();
            case END:
                return endStatement();
            // Add more ↓

            default:
                throw new IllegalStateException("Unrecognized statement type: " + nextToken.getType());
        }
    }
    
    private StatementNode ifStatement() {
        // Consume the 'if' token
        tokenManager.matchAndRemove(Token.TokenType.IF);

        // Parse the condition
        Node condition = parseBooleanExpression(); // Set the condition is an expression

        // Expect an opening brace for the if block
        tokenManager.matchAndRemove(Token.TokenType.OPENBRACE);
        
        // Parse statements inside the if block
        Node ifBlock = statements();

        // Expect a closing brace for the if block
        tokenManager.matchAndRemove(Token.TokenType.CLOSEBRACE);

        Node elseBlock = null;
        // Check for an 'else' part
        if (tokenManager.peek(0).isPresent() && tokenManager.peek(0).get().getType() == Token.TokenType.ELSE) {
            // Consume the 'else' token
            tokenManager.matchAndRemove(Token.TokenType.ELSE);

            // Expect an opening brace for the else block
            tokenManager.matchAndRemove(Token.TokenType.OPENBRACE);

            // Parse statements inside the else block
            elseBlock = statements();

            // Expect a closing brace for the else block
            tokenManager.matchAndRemove(Token.TokenType.CLOSEBRACE);
        }

        // Construct and return the if statement node
        return new IfStatementNode(condition, ifBlock, elseBlock);
    }

    private StatementNode whileStatement() {
        // Consume the 'while' token
        tokenManager.matchAndRemove(Token.TokenType.WHILE);

        // Parse the condition
        Node condition = parseBooleanExpression(); // Set the condition is an expression

        // Expect an opening brace for the while block
        tokenManager.matchAndRemove(Token.TokenType.OPENBRACE);
        
        // Parse statements inside the while block
        Node whileBlock = statements();

        // Expect a closing brace for the while block
        tokenManager.matchAndRemove(Token.TokenType.CLOSEBRACE);

        // Construct and return the while statement node
        return new WhileStatementNode(condition, whileBlock);
    }

    private StatementNode forStatement() {
        // Consume the 'for' token
        tokenManager.matchAndRemove(Token.TokenType.FOR);

        // Expect an opening parenthesis for the for loop components
        tokenManager.matchAndRemove(Token.TokenType.OPENPARA);

        // Parse the initialization part (set it's an assignment statement)
        StatementNode initialization = assignment();
        
        // Expect a separator (semicolon)
        tokenManager.matchAndRemove(Token.TokenType.SEPERATOR);

        // Parse the condition (set it's an expression)
        Node condition = parseBooleanExpression();

        // Expect another separator (semicolon)
        tokenManager.matchAndRemove(Token.TokenType.SEPERATOR);

        // Parse the update part (assignment or increment/decrement)
        StatementNode update = assignment(); // Placeholde

        // Expect a closing parenthesis for the for loop components
        tokenManager.matchAndRemove(Token.TokenType.CLOSEPARA);

        // Expect an opening brace for the for loop block
        tokenManager.matchAndRemove(Token.TokenType.OPENBRACE);

        // Parse statements inside the for loop block
        Node forBlock = statements();

        // Expect a closing brace for the for loop block
        tokenManager.matchAndRemove(Token.TokenType.CLOSEBRACE);

        // Construct and return the for statement node
        return new ForStatementNode(initialization, condition, update, forBlock);
    }

    // other methods... ↓
    
    private StatementNode printStatement() {

        tokenManager.matchAndRemove(Token.TokenType.PRINT);
        List<Node> printItems = printList();
        return new PrintNode(printItems);
    }

    private List<Node> printList() {
        List<Node> items = new ArrayList<>();

        while (true) {
            Optional<Token> optToken = tokenManager.peek(0);
            if (!optToken.isPresent()) {
                break; // Break if no more tokens are available
            }

            Token token = optToken.get();
            Node item;
            switch (token.getType()) {
                case STRING:
                    tokenManager.matchAndRemove(Token.TokenType.STRING); // Consume the string token
                    item = new StringNode(token.getValue()); // Create a StringNode
                    break;
                // Handle other types of expressions that can appear in printList
                default:
                    item = expression(); // Parse general expressions
                    break;
            }

            items.add(item);

            // Check if the next token is a comma
            if (tokenManager.peek(0).isPresent() && tokenManager.peek(0).get().getType() != Token.TokenType.COMMA) {
                break; // Break if the next token is not a comma
            }
            tokenManager.matchAndRemove(Token.TokenType.COMMA); // Consume the comma
        }

        return items;
    }
    
    private StatementNode assignment() {
        String variableName = tokenManager.matchAndRemove(Token.TokenType.WORD).orElseThrow().getValue();
        tokenManager.matchAndRemove(Token.TokenType.EQUALS); // Consume '='
        Node value = expression(); // Asu RHS is an expression
        return new AssignmentNode(new VariableNode(variableName), value);
    }
    
    private VariableNode variable() {
        // set that a variable is represented by a WORD token
        Token token = tokenManager.peek(0).orElseThrow(
            () -> new IllegalStateException("Expected variable token not found")
        );

        if (token.getType() != Token.TokenType.WORD) {
            throw new IllegalStateException("Expected a variable, but found: " + token.getType());
        }

        // Consume the WORD token
        tokenManager.matchAndRemove(Token.TokenType.WORD);

        return new VariableNode(token.getValue());
    }

    private StatementNode readStatement() {
        // Consume the 'READ' token
        tokenManager.matchAndRemove(Token.TokenType.READ);

        List<VariableNode> variables = new ArrayList<>();
        
        while (tokenManager.moreTokens()) {
            Optional<Token> nextToken = tokenManager.peek(0);
            if (!nextToken.isPresent() || nextToken.get().getType() != Token.TokenType.WORD) {
                break; // Break if next token is not variable
            }

            // Parse and add the variable to the list
            VariableNode variable = variable();
            variables.add(variable);

            // Check for comma
            if (tokenManager.peek(0).isPresent() && tokenManager.peek(0).get().getType() == Token.TokenType.COMMA) {
                tokenManager.matchAndRemove(Token.TokenType.COMMA);
            } 
            else {
                break; // No more variables to read
            }
        }

        return new ReadNode(variables);
    }

    private StatementNode dataStatement() {
        // Consume the 'DATA' token
        tokenManager.matchAndRemove(Token.TokenType.DATA);

        List<Node> dataItems = new ArrayList<>();
        
        while (tokenManager.moreTokens()) {
            Optional<Token> nextToken = tokenManager.peek(0);
            if (!nextToken.isPresent()) {
                break; // No more tokens
            }

            Node dataItem;
            switch (nextToken.get().getType()) {
                case STRING:
                    dataItem = new StringNode(nextToken.get().getValue());
                    tokenManager.matchAndRemove(Token.TokenType.STRING);
                    break;
                case NUMBER:
                    dataItem = new IntegerNode(Integer.parseInt(nextToken.get().getValue()));
                    tokenManager.matchAndRemove(Token.TokenType.NUMBER);
                    break;
                case FLOAT:
                    dataItem = new FloatNode(Float.parseFloat(nextToken.get().getValue()));
                    tokenManager.matchAndRemove(Token.TokenType.FLOAT);
                    break;
                default:
                    throw new IllegalStateException("Unexpected token type in DATA statement: " + nextToken.get().getType());
            }

            dataItems.add(dataItem);

            // Check for comma
            if (tokenManager.peek(0).isPresent() && tokenManager.peek(0).get().getType() == Token.TokenType.COMMA) {
                tokenManager.matchAndRemove(Token.TokenType.COMMA);
            } 
            else {
                break; // End
            }
        }
        return new DataNode(dataItems);
    }
    
    private StatementNode inputStatement() {
        tokenManager.matchAndRemove(Token.TokenType.INPUT); // Consume the 'INPUT' token

        // Parse first parameter
        Token firstToken = tokenManager.peek(0).orElseThrow();
        Node firstParam;
        if (firstToken.getType() == Token.TokenType.STRING) {
            tokenManager.matchAndRemove(Token.TokenType.STRING);
            firstParam = new StringNode(firstToken.getValue());
        } 
        else if (firstToken.getType() == Token.TokenType.WORD) {
            firstParam = variable(); // set a method to parse a variable
        } 
        else {
            throw new IllegalStateException("Expected string or variable as first parameter in INPUT");
        }

        // Parse the list of variables
        List<VariableNode> variables = new ArrayList<>();
        while (tokenManager.moreTokens()) {
            // Set comma-separated variables
            Token nextToken = tokenManager.peek(0).orElseThrow();
            if (nextToken.getType() == Token.TokenType.WORD) {
                variables.add(variable()); // Add parsed variable to the list
            } 
            else if (nextToken.getType() != Token.TokenType.COMMA) {
                break; // Break if the next token is not a comma or a variable
            }
            tokenManager.matchAndRemove(Token.TokenType.COMMA); // Consume the comma
        }

        return new InputNode(firstParam, variables);
    }

    private StatementNode gosubStatement() {
        // Consume the 'GOSUB' token
        tokenManager.matchAndRemove(Token.TokenType.GOSUB);

        // Expecting an identifier next
        Optional<Token> identifierTokenOpt = tokenManager.peek(0);
        if (!identifierTokenOpt.isPresent() || identifierTokenOpt.get().getType() != Token.TokenType.WORD) {
            throw new IllegalStateException("Expected an identifier after GOSUB");
        }

        // Get the identifier and consume the token
        String identifier = identifierTokenOpt.get().getValue();
        tokenManager.matchAndRemove(Token.TokenType.WORD);

        // Return new GosubNode with the identifier
        return new GosubNode(identifier);
    }

    private StatementNode returnStatement() {
        tokenManager.matchAndRemove(Token.TokenType.RETURN);
        return new ReturnNode();
    }

    private StatementNode nextStatement() {
        // Consume the 'NEXT' token
        tokenManager.matchAndRemove(Token.TokenType.NEXT);
        // Check if there's an optional loop variable after 'NEXT'
        Optional<Token> identifierTokenOpt = tokenManager.peek(0);
        String loopVariable = null;
        if (identifierTokenOpt.isPresent() && identifierTokenOpt.get().getType() == Token.TokenType.WORD) {
            loopVariable = identifierTokenOpt.get().getValue();
            tokenManager.matchAndRemove(Token.TokenType.WORD); // Consume the loop variable
        }

        // Return a new NextNode
        return new NextNode(loopVariable);
    }
    
    private Node parseExpression() {
        Node left = parseTerm();

        while (true) {
            Optional<Token> tokenOpt = tokenManager.peek(0);
            if (!tokenOpt.isPresent()) {
                return left;
            }

            Token token = tokenOpt.get();
            switch (token.getType()) {
                case PLUS:
                case MINUS:
                    tokenManager.matchAndRemove(token.getType());
                    Node right = parseTerm();
                    left = new BinaryOperationNode(left, token.getType(), right);
                    break;
                case MULTIPLY:
                case DIVIDE:
                    tokenManager.matchAndRemove(token.getType());
                    Node rightOperand = parseTerm();
                    left = new BinaryOperationNode(left, token.getType(), rightOperand);
                    break;
                case OPENPARA:
                    tokenManager.matchAndRemove(Token.TokenType.OPENPARA);
                    Node subExpression = parseExpression();
                    Optional<Token> closingParenOpt = tokenManager.peek(0);
                    if (!closingParenOpt.isPresent() || closingParenOpt.get().getType() != Token.TokenType.CLOSEPARA) {
                        throw new IllegalStateException("Expected closing parenthesis");
                    }
                    tokenManager.matchAndRemove(Token.TokenType.CLOSEPARA);
                    return subExpression;
                case FUNCTION_NAME:
                    return parseFunctionCall();
                default:
                    return left;
            }
        }
    }
    
    private Node parseFunctionCall() {
        // Set the function name has already been consumed, next is '('
        Optional<Token> openParen = tokenManager.peek(0);
        if (!openParen.isPresent() || openParen.get().getType() != Token.TokenType.OPENPARA) {
            throw new IllegalStateException("Expected '(' after function name");
        }
        tokenManager.matchAndRemove(Token.TokenType.OPENPARA); // Consume '('

        // Parse the arguments
        List<Node> arguments = new ArrayList<>();
        while (true) {
            Optional<Token> nextToken = tokenManager.peek(0);
            if (nextToken.isPresent() && nextToken.get().getType() == Token.TokenType.CLOSEPARA) {
                break; // End of arguments
            }

            Node argument = parseExpression(); // Parse an expression for each argument
            arguments.add(argument);

            nextToken = tokenManager.peek(0);
            if (nextToken.isPresent() && nextToken.get().getType() == Token.TokenType.COMMA) {
                tokenManager.matchAndRemove(Token.TokenType.COMMA); // Consume ','
            } 
            else if (nextToken.isPresent() && nextToken.get().getType() != Token.TokenType.CLOSEPARA) {
                throw new IllegalStateException("Expected ',' or ')' after function argument");
            }
        }

        tokenManager.matchAndRemove(Token.TokenType.CLOSEPARA); // Consume ')'

        // Return a node representing the function call
        return new FunctionCallNode(arguments);
    }

    
    private Node parseTerm() {
        // Set a term can be a number or a variable
        Optional<Token> tokenOpt = tokenManager.peek(0);
        if (!tokenOpt.isPresent()) {
            throw new IllegalStateException("Unexpected end of expression");
        }

        Token token = tokenOpt.get();
        switch (token.getType()) {
            case NUMBER:
                tokenManager.matchAndRemove(Token.TokenType.NUMBER);
                return new NumberNode(token.getValue());
            case WORD:
                // Set WORD tokens are variable names
                tokenManager.matchAndRemove(Token.TokenType.WORD);
                return new VariableNode(token.getValue());
            // Add more ↓
            default:
                throw new IllegalStateException("Unexpected token in term: " + token.getType());
        }
    }
    
    private BooleanExpressionNode parseBooleanExpression() {
        Node leftOperand = parseExpression(); // Set parseExpression() parses an expression

        Optional<Token> operatorTokenOpt = tokenManager.peek(0);
        if (!operatorTokenOpt.isPresent()) {
            throw new IllegalStateException("Expected relational operator in boolean expression");
        }

        Token operatorToken = operatorTokenOpt.get();
        RelationalOperator operator = mapTokenToRelationalOperator(operatorToken);
        tokenManager.matchAndRemove(operatorToken.getType()); // Consume the operator token

        Node rightOperand = parseExpression(); // Parse the expression on the right side

        return new BooleanExpressionNode(leftOperand, operator, rightOperand);
    }

    private RelationalOperator mapTokenToRelationalOperator(Token token) {
        switch (token.getType()) {
            case GREATER_THAN:
                return RelationalOperator.GREATER_THAN;
            case LESS_THAN:
                return RelationalOperator.LESS_THAN;
            case EQUALS:
                return RelationalOperator.EQUALS;
            // Add more↓
                
            default:
                throw new IllegalStateException("Unexpected operator in boolean expression: " + token.getType());
        }
    }
    
    private StatementNode endStatement() {
        // Consume the 'END' token
        tokenManager.matchAndRemove(Token.TokenType.END);
        return new EndNode();
    }
    
    private FunctionCallNode parseFunctionCall(String functionName) {
        // Check if the function is one of the special built-in functions that do not require parentheses
        if (Arrays.asList("getline", "print", "printf", "exit", "nextfile", "next").contains(functionName)) {
            List<Node> parameters = new ArrayList<>();
            
            // Check if the next token indicates the start of parameters
            Optional<Token> nextTokenOpt = tokenManager.peek(0);
            while (nextTokenOpt.isPresent() && isParameterStart(nextTokenOpt.get())) {
                Node parameter = parseExpression(); // parseExpression can handle parsing parameters
                parameters.add(parameter);
                nextTokenOpt = tokenManager.peek(0); // Peek next token to decide if the loop should continue
            }
            
            return new FunctionCallNode(functionName, parameters);
        } 
        else {
            // For regular function calls, expect and consume an opening parenthesis
            tokenManager.matchAndRemove(Token.TokenType.OPENPARA);

            List<Node> parameters = new ArrayList<>();
            // Parse parameters until a closing parenthesis is encountered
            while (!tokenManager.peek(0).get().getType().equals(Token.TokenType.CLOSEPARA)) {
                Node parameter = parseExpression(); // suitable for parsing parameters
                parameters.add(parameter);

                // If the next token is a comma, consume it and move to the next parameter
                if (tokenManager.peek(0).get().getType().equals(Token.TokenType.COMMA)) {
                    tokenManager.matchAndRemove(Token.TokenType.COMMA);
                }
            }
            
            // Consume the closing parenthesis
            tokenManager.matchAndRemove(Token.TokenType.CLOSEPARA);

            return new FunctionCallNode(functionName, parameters);
        }
    }

    private boolean isParameterStart(Token token) {
        // Token.TokenType enum includes these types
        return token.getType() == Token.TokenType.STRING ||
               token.getType() == Token.TokenType.NUMBER ||
               token.getType() == Token.TokenType.WORD || // Variables or function names
               token.getType() == Token.TokenType.BOOLEAN_LITERAL || // true/false
               token.getType() == Token.TokenType.NULL_LITERAL || // null
               token.getType() == Token.TokenType.OPENPARA || // Opening parenthesis for nested expressions
               token.getType() == Token.TokenType.MINUS || // Unary minus, for negative numbers or expressions
               token.getType() == Token.TokenType.PLUS || // Unary plus
               token.getType() == Token.TokenType.NOT || // Logical NOT operator, for boolean expressions
               token.getType() == Token.TokenType.ARRAY_START; // If supports array literals starting with a token
    }
}