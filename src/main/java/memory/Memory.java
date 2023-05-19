package memory;

import storage.Word;
import constants.Constants;

public class Memory {
    private Word[] memory;

    public Memory(){
        memory = new Word[Constants.MEMORY_SIZE];
    }
}
