import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        try {
            // Load the source code from a file
            String content = new String(Files.readAllBytes(Paths.get("path.basic")));

            // Tokeni the source code
            Lexer lexer = new Lexer();
            LinkedList<Token> tokens = lexer.lex(content); 
            // Asu'lex' method takes the source code as a string

            // Parse the tokens to create an AST
            Parser parser = new Parser(tokens);
            Node ast = parser.parse();

            // Print the AST
            System.out.println(ast.toString());

        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}