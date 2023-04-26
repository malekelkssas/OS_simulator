package storage;

public class Variable implements Storable {
    private String name;
    private Object value;
    private StorageType type = StorageType.VARIABLE;
}
