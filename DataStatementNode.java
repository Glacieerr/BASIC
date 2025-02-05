import java.util.ArrayList;
import java.util.List;

public class DataStatementNode {
    private String label;
    private List<Object> data; // Consider changing Object to a more specific type if applicable

    public DataStatementNode(String label, List<Object> data) {
        this.label = label;
        this.data = new ArrayList<>(data); // Create a copy of the data list
    }

    public String getLabel() {
        return label;
    }

    public List<Object> getData() {
        return new ArrayList<>(data); // Return a copy to prevent external modifications
    }

    // Method to validate data types within the statement
    public boolean validateDataTypes(Class<?>... expectedTypes) {
        if (data.size() != expectedTypes.length) {
            return false; // The number of items doesn't match the expected types
        }
        for (int i = 0; i < data.size(); i++) {
            if (!expectedTypes[i].isInstance(data.get(i))) {
                return false; // Mismatch between expected and actual data type
            }
        }
        return true; // All data items matched the expected types
    }

    // Method to convert all data items to a specified type
    public List<String> convertDataToStrings() {
        List<String> stringData = new ArrayList<>();
        for (Object item : data) {
            stringData.add(item.toString());
        }
        return stringData;
    }

    // Method to add a new data item to the node
    public void addData(Object newData) {
        data.add(newData);
    }

    // Method to remove a data item by index
    public boolean removeData(int index) {
        if (index >= 0 && index < data.size()) {
            data.remove(index);
            return true;
        }
        return false; // Index out of bounds
    }
    
    public void execute() {

        System.out.println("Executing DataStatementNode");

        for (Object dataItem : this.data) {
            System.out.println(dataItem.toString());
        }
    }
}