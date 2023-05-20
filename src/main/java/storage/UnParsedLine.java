package storage;

public class UnParsedLine extends Storable {

    String unParsedLine;
    public UnParsedLine(String unParsedLine){
        this.unParsedLine = unParsedLine;
        type = StorageType.UNPARSEDLINE;
    }
}
