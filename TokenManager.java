import java.util.LinkedList;
import java.util.Optional;

public class TokenManager {
    private LinkedList<Token> tokens;

    public TokenManager(LinkedList<Token> tokens) {
        this.tokens = new LinkedList<>(tokens); // Create a copy of the tokens list
    }

    public Optional<Token> peek(int j) {
        if (j >= 0 && j < tokens.size()) {
            return Optional.of(tokens.get(j));
        }
        return Optional.empty();
    }

    public boolean moreTokens() {
        return !tokens.isEmpty();
    }

    public Optional<Token> matchAndRemove(Token.TokenType t) {
        if (!tokens.isEmpty() && tokens.getFirst().getType() == t) {
            return Optional.of(tokens.removeFirst());
        }
        return Optional.empty();
    }
}