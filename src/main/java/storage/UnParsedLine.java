package storage;

import java.io.Serializable;

public class UnParsedLine extends Storable implements Serializable {

    String unParsedLine;

    public UnParsedLine(String unParsedLine) {
        this.unParsedLine = unParsedLine;
        type = StorageType.UNPARSEDLINE;
    }

    public UnParsedLine(){}

    @Override
    public String toString() {
        return "UnParsedLine{" +
                "unParsedLine='" + unParsedLine + '\'' +
                '}';
    }
    
    public String getunParsedLine() {
    	return unParsedLine;
    }
    
    public String [] getSplittedLine() {
    	return unParsedLine.split(" ");
    }
}
