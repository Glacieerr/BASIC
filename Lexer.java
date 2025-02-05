import java.util.LinkedList;
import java.util.HashMap;

public class Lexer {
	private HashMap<String, Token.TokenType> knownWords;
    private HashMap<String, Token.TokenType> singleCharSymbols;
    private HashMap<String, Token.TokenType> doubleCharSymbols;
    private int lineNumber = 1;
    private int charPosition = 0;
    
    public Lexer() {
        initializeKnownWords();
        initializeSymbols();
    }
    
    private void initializeKnownWords() {
        knownWords = new HashMap<>();
        knownWords.put("Print", Token.TokenType.PRINT);
        knownWords.put("if",Token.TokenType.IF);
        knownWords.put("while",Token.TokenType.WHILE);
        knownWords.put("do",Token.TokenType.READ);
        knownWords.put("for",Token.TokenType.INPUT);
        knownWords.put("break",Token.TokenType.DATA);
        knownWords.put("continue",Token.TokenType.GOSUB);
        knownWords.put("else",Token.TokenType.FOR);
        knownWords.put("return",Token.TokenType.TO);
        knownWords.put("BEGIN",Token.TokenType.STEP);
        knownWords.put("END",Token.TokenType.NEXT);
        knownWords.put("printf",Token.TokenType.RETURN);
        knownWords.put("in",Token.TokenType.THEN);
        knownWords.put("delete",Token.TokenType.FUNCTION);
        knownWords.put("getline",Token.TokenType.END);
        knownWords.put("Print", Token.TokenType.PRINT);
        knownWords.put("if", Token.TokenType.IF);
        knownWords.put("while", Token.TokenType.WHILE);
        knownWords.put("READ", Token.TokenType.READ);
        knownWords.put("DATA", Token.TokenType.DATA);
        knownWords.put("INPUT", Token.TokenType.INPUT);
        knownWords.put("AND", Token.TokenType.AND);
        knownWords.put("OR", Token.TokenType.OR);
    }

    private void initializeSymbols() {
        singleCharSymbols = new HashMap<>();
        doubleCharSymbols = new HashMap<>();
        
        singleCharSymbols.put("=", Token.TokenType.EQUALS);
        singleCharSymbols.put("{", Token.TokenType.OPENCURLY);
        singleCharSymbols.put("}", Token.TokenType.CLOSECURLY);
        singleCharSymbols.put("[", Token.TokenType.OPENBOX);
        singleCharSymbols.put("]", Token.TokenType.CLOSEBOX);
        singleCharSymbols.put("(", Token.TokenType.OPENPARA);
        singleCharSymbols.put(")", Token.TokenType.CLOSEPARA);
        singleCharSymbols.put("$", Token.TokenType.DOLLAR);
        singleCharSymbols.put("~", Token.TokenType.MATCH);
        singleCharSymbols.put("=", Token.TokenType.ASSIGN);
        singleCharSymbols.put("<", Token.TokenType.LESSTHAN);
        singleCharSymbols.put(">", Token.TokenType.GREATERTHAN);
        singleCharSymbols.put("!", Token.TokenType.NOT);
        singleCharSymbols.put("+", Token.TokenType.ADD);
        singleCharSymbols.put("^", Token.TokenType.POWER);
        singleCharSymbols.put("-", Token.TokenType.SUBTRACT);
        singleCharSymbols.put("?", Token.TokenType.QUESTION);
        singleCharSymbols.put(":", Token.TokenType.COLON);
        singleCharSymbols.put("*", Token.TokenType.MULTIPLY);
        singleCharSymbols.put("/", Token.TokenType.DIVIDE);
        singleCharSymbols.put("%", Token.TokenType.MODULO);
        singleCharSymbols.put(";", Token.TokenType.SEPERATOR);
        singleCharSymbols.put("\n", Token.TokenType.SEPERATOR);
        singleCharSymbols.put("|", Token.TokenType.PIPE);
        singleCharSymbols.put(",", Token.TokenType.COMMA);
        singleCharSymbols.put("&", Token.TokenType.AND);
        singleCharSymbols.put("&", Token.TokenType.BITWISE_AND);
        singleCharSymbols.put("|", Token.TokenType.BITWISE_OR);
        singleCharSymbols.put("^", Token.TokenType.BITWISE_XOR);
        
        doubleCharSymbols.put("<=", Token.TokenType.NOTEQUALS);
        doubleCharSymbols .put(">=",Token.TokenType.GREATERTHAN_EQUALTO);
        doubleCharSymbols.put("<=",Token.TokenType.LESSTHAN_EQUALTO);
        doubleCharSymbols.put("++",Token.TokenType.PLUS1);
        doubleCharSymbols.put("--",Token.TokenType.MINUS1);
        doubleCharSymbols.put("==",Token.TokenType.EQUALSTO);
        doubleCharSymbols.put("!=",Token.TokenType.NOT_EQUALSTO); 
        doubleCharSymbols.put("^=",Token.TokenType.RAISED_TO);
        doubleCharSymbols.put("%=",Token.TokenType.REMAINDER_TO);
        doubleCharSymbols.put("*=",Token.TokenType.MULTIPLY_BY);
        doubleCharSymbols.put("/=",Token.TokenType.DIVIDE_BY);
        doubleCharSymbols.put("+=",Token.TokenType.ADD_BY);
        doubleCharSymbols.put("-=",Token.TokenType.SUB_BY);
        doubleCharSymbols.put("&&",Token.TokenType.AND);
        doubleCharSymbols.put("||",Token.TokenType.OR);
        doubleCharSymbols.put(">>",Token.TokenType.APPEND);
        doubleCharSymbols.put("!~",Token.TokenType.NOMATCH);
    }

    public LinkedList<Token> lex(String filename) {
        LinkedList<Token> tokens = new LinkedList<>();
        CodeHandler codeHandler = new CodeHandler(filename);

        while (!codeHandler.isDone()) {
            char currentChar = codeHandler.peek(0);
            if (Character.isWhitespace(currentChar)) {
                if (currentChar == '\n') {
                    tokens.add(new Token(Token.TokenType.ENDOFLINE, lineNumber, charPosition));
                    lineNumber++;
                    charPosition = 0;
                } 
                else {
                    charPosition++;
                }
                codeHandler.getChar(); // Advance the index
            } 
            else if (Character.isLetter(currentChar)) {
                tokens.add(processWord(codeHandler));
            } 
            else if (Character.isDigit(currentChar)) {
                tokens.add(processNumber(codeHandler));
            } 
            else {
                throw new IllegalArgumentException("Unrecognized character: " + currentChar);
            }
        }

        return tokens;
    }

    private Token processWord(CodeHandler codeHandler) {
        StringBuilder word = new StringBuilder();
        int startPos = charPosition; // Record the start position of the word

        // Collecting characters of the word
        while (Character.isLetterOrDigit(codeHandler.peek(0)) || codeHandler.peek(0) == '_') {
            word.append(codeHandler.getChar());
            charPosition++;
        }

        String wordStr = word.toString();

        // Check for labels: a word followed by a colon
        if (codeHandler.peek(0) == ':') {
            codeHandler.getChar(); // Consume the colon
            charPosition++;
            return new Token(Token.TokenType.LABEL, wordStr, lineNumber, startPos);
        }

        // Check if the word is a known keyword
        Token.TokenType type = knownWords.getOrDefault(wordStr, Token.TokenType.WORD);
        return new Token(type, wordStr, lineNumber, startPos);
    }

    private Token processNumber(CodeHandler codeHandler) {
        StringBuilder number = new StringBuilder();
        int startPos = charPosition; // Record the start position of the number

        boolean hasDecimal = false; // Flag to check if the number contains a decimal point

        // Collecting digits (and possibly one decimal point) of the number
        while (Character.isDigit(codeHandler.peek(0)) || (!hasDecimal && codeHandler.peek(0) == '.')) {
            char currentChar = codeHandler.getChar();

            if (currentChar == '.') {
                if (hasDecimal) {
                    // If a second decimal point is encountered, break the loop
                    break;
                }
                hasDecimal = true;
            }

            number.append(currentChar);
            charPosition++;
        }

        // Check for invalid numbers like single '.' or trailing dot without digits after
        if (number.length() == 0 || (hasDecimal && number.length() == 1)) {
            throw new IllegalArgumentException("Invalid numeric literal at line " + lineNumber + ", position " + startPos);
        }

        return new Token(Token.TokenType.NUMBER, number.toString(), lineNumber, startPos);
    }
    
    private Token processSymbol(CodeHandler codeHandler) {
        int startPos = charPosition; // Record the start position of the symbol

        // Check for two-character symbols
        char firstChar = codeHandler.getChar();
        char secondChar = codeHandler.peek(0);
        String twoCharSymbol = "" + firstChar + secondChar;
        if (doubleCharSymbols.containsKey(twoCharSymbol)) {
            codeHandler.getChar(); // Consume the second character
            charPosition += 2;
            return new Token(doubleCharSymbols.get(twoCharSymbol), lineNumber, startPos);
        }

        // Single-character symbol
        charPosition++;
        if (singleCharSymbols.containsKey(String.valueOf(firstChar))) {
            return new Token(singleCharSymbols.get(String.valueOf(firstChar)), lineNumber, startPos);
        }

        // If symbol not recognized, throw an error or handle accordingly
        throw new IllegalArgumentException("Unrecognized symbol '" + firstChar + "' at line " + lineNumber + ", position " + startPos);
    }
    
    private void handleWhitespace(CodeHandler codeHandler) {
        char currentChar = codeHandler.peek(0);

        while (currentChar == ' ' || currentChar == '\t' || currentChar == '\n') {
            if (currentChar == '\n') {
                // Newline character, increment line number and reset char position
                lineNumber++;
                charPosition = 0;
            } 
            else {
                // Space or tab, just move forward in the same line
                charPosition++;
            }

            codeHandler.getChar(); // Consume the whitespace character
            currentChar = codeHandler.peek(0); // Look at the next character
        }
    }
}