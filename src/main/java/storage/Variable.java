package storage;

import java.io.Serializable;

public class Variable extends Storable implements Serializable {
    private String name;
    private Object value;

    public Variable(String name, Object value, int memoryLocation){
        this.name = name;
        this.value = value;
        type = StorageType.VARIABLE;
    }

    public Variable(){}

    @Override
    public String toString() {
        return "Variable{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
