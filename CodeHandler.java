import java.nio.file.Files;
import java.nio.file.Paths;

public class CodeHandler {
    private String document;
    private int index = 0;

    public CodeHandler(String filename) {
        try {
            document = new String(Files.readAllBytes(Paths.get(filename)));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public char peek(int i) {
    	if (index + i >= document.length()) {
            return '\0'; // End of document
        }
        return document.charAt(index + i);
    }

    public String peekString(int i) {
        return document.substring(index, index + i);
    }

    public char getChar() {
    	if (index >= document.length()) {
            return '\0'; // End of document
        }
        return document.charAt(index++);
    }

    public void swallow(int i) {
        index += i;
    }

    public boolean isDone() {
        return index >= document.length();
    }

    public String remainder() {
        return document.substring(index);
    }
}