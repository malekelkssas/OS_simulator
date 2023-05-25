package Serializer;

import exceptions.OSSimulatoeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.Process;
import storage.State;
import util.Serializer;
import util.FileController;
import java.io.File;
import java.util.Vector;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SerializingTest {

    @BeforeEach
    public void init(){
    }

    @Test
    void Serializing_oneNormal_process() throws OSSimulatoeException {
        //Given one process
        Process p = new Process(1,new Vector<>(),new Vector<>());

        //Serialize it
        Serializer.serializeProcess(p);

        File file = new File(FileController.getPath(p.getID()));
        boolean exist = file.exists();

        //then
        assertThat(exist).isEqualTo(true);
    }

    @Test
    void deSerializing_oneNormal_process() throws OSSimulatoeException {
        //Given one process
        Process p = new Process(1,new Vector<>(),new Vector<>());
        p.setState(State.READY);
        p.getPcb().setPc(2);
        //Serialize it then deserialize it
        Serializer.serializeProcess(p);

        Process p2 = Serializer.deserializeProcess(p.getID());

        boolean equal = p.getPcb().getId() == p2.getPcb().getId() &&
                p.getPcb().getPc() == p2.getPcb().getPc() && p.getPcb().getState().toString().equals(p2.getPcb().getState().toString());

        //then
        assertThat(equal).isEqualTo(true);
    }

}
