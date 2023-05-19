package storage;

public class Variable extends Storable {
    private String name;
    private Object value;

    public Variable(String name, Object value, int memoryLocation){
        this.name = name;
        this.value = value;
        this.memoryLocation = memoryLocation;
        type = StorageType.VARIABLE;
    }
}
