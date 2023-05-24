package storage;

import java.io.Serializable;

public enum State implements Serializable {
    NEW, READY, BLOCKED, EXECUTE, FINISH
}
