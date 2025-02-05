import java.util.HashMap;
import java.util.List;

public class LineManager {
    private List<String> lines;
    private int currentLineIndex = 0;
    private HashMap<String, String> globalVariables;

    // Constructor with lines and global variables
    public LineManager(List<String> lines, HashMap<String, String> globalVariables) {
        this.lines = lines;
        this.globalVariables = globalVariables;
    }

    // Constructor that only takes a List<String>
    public LineManager(List<String> lines) {
        this(lines, new HashMap<>()); // Calls the existing constructor with an empty HashMap for global variables
    }

    // Method to split the current line and assign to global variables
    public boolean splitAndAssign() {
        if (currentLineIndex < lines.size()) {
            String currentLine = lines.get(currentLineIndex++);
            String fs = globalVariables.getOrDefault("FS", " "); // Field separator
            String[] fields = currentLine.split(fs);

            // Assigning $0 to the entire line
            globalVariables.put("$0", currentLine);
            
            // Assigning $1, $2, ... to individual fields
            for (int i = 0; i < fields.length; i++) {
                globalVariables.put("$" + (i + 1), fields[i]);
            }
            
            // Setting NF (number of fields)
            globalVariables.put("NF", String.valueOf(fields.length));
            
            return true; // Successfully split and assigned
        }
        return false; // No more lines to process
    }

    // Method to check if there are more lines
    public boolean hasMoreLines() {
        return currentLineIndex < lines.size();
    }

    // Method to get the current line number being processed
    public int getCurrentLineNumber() {
        return currentLineIndex;
    }

    // Method to reset the line processing back to the first line
    public void reset() {
        currentLineIndex = 0;
    }

    // Method to get all lines (mainly for testing or debugging)
    public List<String> getAllLines() {
        return lines;
    }

    // Method to get the global variables (for testing or debugging)
    public HashMap<String, String> getGlobalVariables() {
        return globalVariables;
    }
}