public class Token {
	enum TokenType {
        WORD, NUMBER, ENDOFLINE, STRINGLITERAL, LABEL,PRINT, READ, INPUT, DATA, GOSUB, FOR, TO, STEP, 
        NEXT, RETURN, IF, THEN, FUNCTION, WHILE, END, EQUALS, LESS, GREATER, LPAREN, RPAREN, PLUS, 
        MINUS, MULTIPLY, DIVIDE, NOTEQUALS, DO, BREAK, CONTINUE, ELSE, BEGIN, PRINTF, IN, DELETE, 
        GETLINE, EXIT, NEXTFILE, AND, COMMA, PIPE, SEPERATOR,MODULO, COLON, QUESTION, SUBTRACT, POWER, 
        ADD, NOT, GREATERTHAN, LESSTHAN, ASSIGN, MATCH, DOLLAR, CLOSEPARA, OPENPARA, CLOSEBOX, OPENBOX, 
        CLOSECURLY, OPENCURLY, GREATERTHAN_EQUALTO, LESSTHAN_EQUALTO, PLUS1, MINUS1, EQUALSTO, 
        NOT_EQUALSTO, RAISED_TO, REMAINDER_TO, MULTIPLY_BY, DIVIDE_BY, ADD_BY, SUB_BY, OR, APPEND, 
        NOMATCH, FLOAT, OPENBRACE, CLOSEBRACE, STRING, GREATER_THAN, LESS_THAN, FUNCTION_NAME, 
        BITWISE_AND, BITWISE_OR, BITWISE_XOR, BOOLEAN_LITERAL, NULL_LITERAL, ARRAY_START, 
    }

    private final TokenType type;
    private final String value;
    private final int lineNumber;
    private final int charPosition;
    
    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public Token(TokenType type, int lineNumber, int charPosition) {
        this(type, null, lineNumber, charPosition);
    }

    public Token(TokenType type, String value, int lineNumber, int charPosition) {
        this.type = type;
        this.value = value;
        this.lineNumber = lineNumber;
        this.charPosition = charPosition;
    }

    @Override
    public String toString() {
        return type + (value != null ? " (" + value + ")" : "") + " at line " + lineNumber + ", position " + charPosition;
    }
}