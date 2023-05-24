package storage;

import java.io.Serializable;

public class Variable extends Storable implements Serializable {
	private String name;
	private Object value;

	public Variable(String name, Object value) {
		this.name = name;
		this.value = value;
		type = StorageType.VARIABLE;
	}

	public Variable() {
	}

	@Override
	public String toString() {
		return "Variable{" + "name='" + name + '\'' + ", value=" + value + '}';
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
