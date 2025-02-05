public class InterpreterDataType {
    private Object value; // Holds the actual data value
    private DataType type; // Enum to identify the data type
    

    public enum DataType {
        INTEGER, FLOAT, STRING,
    }

    // Constructor that accepts the type and value as arguments
    public InterpreterDataType(DataType type, Object value) {
        this.type = type;
        this.value = value;
        setValue(value); // This method will validate the value based on the type
    }

    // Method to set the value with type validation
    public void setValue(Object value) {
        switch (this.type) {
            case INTEGER:
                if (!(value instanceof Integer)) {
                    throw new IllegalArgumentException("Value must be an Integer for INTEGER type");
                }
                break;
            case FLOAT:
                if (!(value instanceof Float)) {
                    throw new IllegalArgumentException("Value must be a Float for FLOAT type");
                }
                break;
            case STRING:
                if (!(value instanceof String)) {
                    throw new IllegalArgumentException("Value must be a String for STRING type");
                }
                break;
        }
        this.value = value;
    }

    // Method to get the value
    public Object getValue() {
        return this.value;
    }

    // Method to get the data type
    public DataType getType() {
        return this.type;
    }

    // Method to convert the value to a string representation
    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    // Convenience method to create a typed data object based on the value's runtime type
    public static InterpreterDataType fromObject(Object value) {
        if (value instanceof Integer) {
            return new InterpreterDataType(DataType.INTEGER, value);
        } 
        else if (value instanceof Float) {
            return new InterpreterDataType(DataType.FLOAT, value);
        } 
        else if (value instanceof String) {
            return new InterpreterDataType(DataType.STRING, value);
        } 
        else {
            throw new IllegalArgumentException("Unsupported data type");
        }
    }
    
	// Constructor for int values
    public InterpreterDataType(int value) {
        this.value = value;
        this.type = DataType.INTEGER;
    }

    public void setType(DataType type) {
        this.type = type;
    }
    
	// Add a method to specifically get the integer value
    public int getIntValue() {
        if (this.type == DataType.INTEGER && this.value instanceof Integer) {
            return (Integer) this.value;
        }
        else {
            throw new IllegalStateException("Attempted to get integer value from a non-integer InterpreterDataType");
        }
    }
}