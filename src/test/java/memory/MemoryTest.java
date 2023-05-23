package memory;

import constants.Constants;
import exceptions.NoSuchProcessException;
import exceptions.OSSimulatoeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.FileController;
import storage.State;
import storage.UnParsedLine;
import storage.Variable;
import storage.Process;

import java.util.Arrays;
import java.util.Vector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MemoryTest {
    Memory memory;

    @BeforeEach
    public void init(){
        memory = Memory.getInstance();
        memory.reset();
    }

    @Test
    void testAllocate_twoNormalProcess_memorySize() throws OSSimulatoeException {
        // Given 2 Processes and one resource
        Vector<UnParsedLine> unParsedLines = new Vector<>();
        Vector<Variable> variables = new Vector<>();
        for (int i=0;i!=10;i++){
            unParsedLines.add(new UnParsedLine());
        }
        for (int i=0;i!=6;i++){
            variables.add(new Variable());
        }
        Process p1 = new Process(1, unParsedLines, variables);
        Process p2 = new Process(2, unParsedLines, variables);

        // when both of them allocate in the memory
        memory.allocate(p1);
        memory.allocate(p2);

        //then
        assertThat(memory.size()).isEqualTo(40);
    }

    @Test
    void testAllocate_oneNormalProcess_memorySize() throws OSSimulatoeException {
        // Given 2 Processes and one resource
        Vector<UnParsedLine> unParsedLines = new Vector<>();
        Vector<Variable> variables = new Vector<>();
        for (int i=0;i!=10;i++){
            unParsedLines.add(new UnParsedLine());
            variables.add(new Variable());
        }
        Process p1 = new Process(1, unParsedLines, variables);

        // when the process allocate in the memory
        memory.allocate(p1);

        //then
        assertThat(memory.size()).isEqualTo(24);
    }

    @Test
    void testAllocate_Processes_EqualMemorySize() throws OSSimulatoeException {
        // Given 2 Processes and one resource
        Vector<UnParsedLine> unParsedLines = new Vector<>();
        Vector<Variable> variables = new Vector<>();
        for (int i=0;i!=10;i++){
            unParsedLines.add(new UnParsedLine());
        }
        for (int i=0;i!=6;i++){
            variables.add(new Variable());
        }
        Process p1 = new Process(1, unParsedLines, variables);
        Process p2 = new Process(2, unParsedLines, variables);

        // when both of them allocate in the memory
        memory.allocate(p1);
        memory.allocate(p2);

        //then
        assertThat(memory.size()).isEqualTo(40);
    }

    @Test
    void testMemorySize_byAllocateTwoProcesses_MoreThanMemorySize() throws OSSimulatoeException {
        // Given 2 Processes and one resource
        Vector<UnParsedLine> unParsedLines1 = new Vector<>();
        Vector<Variable> variables = new Vector<>();
        for (int i=0;i!=10;i++){
            unParsedLines1.add(new UnParsedLine());
        }
        for (int i=0;i!=6;i++){
            variables.add(new Variable());
        }

        Process p1 = new Process(1, unParsedLines1, variables);
        p1.setState(State.READY);
        Vector<UnParsedLine> unParsedLines2 = (Vector<UnParsedLine>) unParsedLines1.clone();
        unParsedLines2.add(new UnParsedLine());
        Process p2 = new Process(2, unParsedLines2, variables);

        // when both of them allocate in the memory
        memory.allocate(p1);
        memory.allocate(p2);
        FileController.removeProcess(p1.getID());
        //then
        assertThat(memory.size()).isEqualTo(21);
    }

    @Test
    void testMemorySize_byAllocateThreeProcesses_theThirdReplaceTheSecond() throws OSSimulatoeException {
        // Given 2 Processes and one resource
        Vector<UnParsedLine> unParsedLines = new Vector<>();
        Vector<Variable> variables = new Vector<>();
        for (int i=0;i!=10;i++){
            unParsedLines.add(new UnParsedLine());
        }
        for (int i=0;i!=6;i++){
            variables.add(new Variable());
        }
        Process p1 = new Process(1, unParsedLines, variables);
        Process p2 = new Process(2, unParsedLines, variables);
        p2.setState(State.READY);
        variables = new Vector<>();
        variables.add(new Variable());
        Process p3 = new Process(3, unParsedLines, variables);

        // when both of them allocate in the memory
        memory.allocate(p1);
        memory.allocate(p2);
        memory.allocate(p3);
        FileController.removeProcess(p2.getID());

        //then
        assertThat(memory.size()).isEqualTo(35);
    }

    @Test
    void testMemorySize_byAllocateThreeProcesses_theThirdReplaceTheFirst() throws OSSimulatoeException {
        // Given 2 Processes and one resource
        Vector<UnParsedLine> unParsedLines = new Vector<>();
        Vector<Variable> variables = new Vector<>();
        for (int i=0;i!=10;i++){
            unParsedLines.add(new UnParsedLine());
        }
        for (int i=0;i!=6;i++){
            variables.add(new Variable());
        }
        Process p1 = new Process(1, unParsedLines, variables);
        p1.setState(State.READY);
        Process p2 = new Process(2, unParsedLines, variables);
        variables = new Vector<>();
        variables.add(new Variable());
        Process p3 = new Process(3, unParsedLines, variables);

        // when both of them allocate in the memory
        memory.allocate(p1);
        memory.allocate(p2);
        memory.allocate(p3);
        FileController.removeProcess(p1.getID());

        //then
        assertThat(memory.size()).isEqualTo(35);
    }

    @Test
    void testProcessesID_byAllocateThreeProcesses_theThirdReplaceTheSecond() throws OSSimulatoeException {
        // Given 2 Processes and one resource
        Vector<UnParsedLine> unParsedLines = new Vector<>();
        Vector<Variable> variables = new Vector<>();
        for (int i=0;i!=10;i++){
            unParsedLines.add(new UnParsedLine());
        }
        for (int i=0;i!=6;i++){
            variables.add(new Variable());
        }
        Process p1 = new Process(1, unParsedLines, variables);
        Process p2 = new Process(2, unParsedLines, variables);
        p2.setState(State.READY);
        variables = new Vector<>();
        variables.add(new Variable());
        Process p3 = new Process(3, unParsedLines, variables);

        // when both of them allocate in the memory
        memory.allocate(p1);
        memory.allocate(p2);
        memory.allocate(p3);
        FileController.removeProcess(p2.getID());

        //then
        assertThat(memory.size()).isEqualTo(35);
    }

    @Test
    void testProcessesID_byAllocateThreeProcesses_theThirdReplaceTheFirst() throws OSSimulatoeException {
        // Given 2 Processes and one resource
        Vector<UnParsedLine> unParsedLines = new Vector<>();
        Vector<Variable> variables = new Vector<>();
        for (int i=0;i!=10;i++){
            unParsedLines.add(new UnParsedLine());
        }
        for (int i=0;i!=6;i++){
            variables.add(new Variable());
        }
        Process p1 = new Process(1, unParsedLines, variables);
        p1.setState(State.READY);
        Process p2 = new Process(2, unParsedLines, variables);
        variables = new Vector<>();
        variables.add(new Variable());
        Process p3 = new Process(3, unParsedLines, variables);

        // when both of them allocate in the memory
        memory.allocate(p1);
        memory.allocate(p2);
        memory.allocate(p3);
        FileController.removeProcess(p1.getID());

        //then
        assertThat(memory.size()).isEqualTo(35);
    }

    @Test
    void CheckProcessID_FromProcess_from_Memory() throws OSSimulatoeException {
        // Given 2 Processes and one resource
        Vector<UnParsedLine> unParsedLines = new Vector<>();
        Vector<Variable> variables = new Vector<>();
        for (int i=0;i!=10;i++){
            unParsedLines.add(new UnParsedLine());
        }
        for (int i=0;i!=6;i++){
            variables.add(new Variable());
        }
        Process p1 = new Process(1, unParsedLines, variables);

        // when both of them allocate in the memory
        memory.allocate(p1);

        //then
        assertThat(memory.getProcess(p1.getID()).getID()).isEqualTo(p1.getID());
    }

    @Test
    void CheckUnValidProcessID_FromProcess_from_Memory() throws OSSimulatoeException {
        // Given 2 Processes and one resource
        Vector<UnParsedLine> unParsedLines = new Vector<>();
        Vector<Variable> variables = new Vector<>();
        for (int i=0;i!=10;i++){
            unParsedLines.add(new UnParsedLine());
        }
        for (int i=0;i!=6;i++){
            variables.add(new Variable());
        }
        Process p1 = new Process(1, unParsedLines, variables);

        // when both of them allocate in the memory
        memory.allocate(p1);

        //then
        Exception exception = assertThrows(NoSuchProcessException.class, () -> {
            memory.getProcess(2);
        });
        assertThat(exception.getMessage()).isEqualTo(Constants.NO_SUCH_PROCESS_ERROR_MESSAGE);
    }
}
