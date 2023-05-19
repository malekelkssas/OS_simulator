package storage;

public class UnParsedLine extends Storable {

    public UnParsedLine(int memoryLocation){
        this.memoryLocation = memoryLocation;
        type = StorageType.UNPARSEDLINE;
    }
}
