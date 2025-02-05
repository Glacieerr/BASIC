import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class Basic {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Error: Please provide exactly one filename as an argument.");
            System.exit(1);
        }
        String filename = args[0];

        Lexer lexer = new Lexer();
        LinkedList<Token> tokens = lexer.lex(filename);

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}