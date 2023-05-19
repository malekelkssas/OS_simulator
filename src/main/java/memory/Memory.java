package memory;

import storage.Word;
import constants.Constants;

public class Memory {
    private Word[] memory;
    private int start;
    private int end;
    private int nItems;

    public Memory(){
        memory = new Word[Constants.MEMORY_SIZE];
        start = -1;
        end = 0;
    }

    public void allocate(){
        //TODO: implement the method
    }

    public void swap(){
        //TODO: implement the method
    }

    public boolean isEmpty() {
        return nItems == 0;
    }

    public boolean isFull() {
        return (nItems == Constants.MEMORY_SIZE);
    }
    public int size(){
        return nItems;
    }
}
