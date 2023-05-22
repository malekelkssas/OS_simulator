package memory;

import exceptions.OSSimulatoeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.State;
import storage.UnParsedLine;
import storage.Variable;
import storage.Process;

import java.util.Vector;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryTest {
    Memory memory;

    @BeforeEach
    public void init(){
        memory = Memory.getInstance();
    }

    @Test
    void testAllocate_twoNormalProcess_memorySize() throws OSSimulatoeException {
        // Given 2 Processes and one resource
        Vector<UnParsedLine> unParsedLines = new Vector<>();
        Vector<Variable> variables = new Vector<>();
        for (int i=0;i!=10;i++){
            unParsedLines.add(null);
            variables.add(null);
        }
        Process p1 = new Process(1, unParsedLines, variables);
        Process p2 = new Process(2, unParsedLines, variables);

        // when both of them allocate in the memory
        memory.allocate(p1, 6);
        memory.allocate(p2, 6);

        //then
        assertThat(memory.size()).isEqualTo(40);
    }

    @Test
    void testAllocate_oneNormalProcess_memorySize() throws OSSimulatoeException {
        // Given 2 Processes and one resource
        Vector<UnParsedLine> unParsedLines = new Vector<>();
        Vector<Variable> variables = new Vector<>();
        for (int i=0;i!=10;i++){
            unParsedLines.add(null);
            variables.add(null);
        }
        Process p1 = new Process(1, unParsedLines, variables);

        // when the process allocate in the memory
        memory.allocate(p1, 10);

        //then
        assertThat(memory.size()).isEqualTo(24);
    }

    @Test
    void testAllocate_Processes_MoreThanMemorySize() throws OSSimulatoeException {
        // Given 2 Processes and one resource
        Vector<UnParsedLine> unParsedLines = new Vector<>();
        Vector<Variable> variables = new Vector<>();
        for (int i=0;i!=10;i++){
            unParsedLines.add(null);
            variables.add(null);
        }
        Process p1 = new Process(1, unParsedLines, variables);
        Process p2 = new Process(2, unParsedLines, variables);

        // when both of them allocate in the memory
        memory.allocate(p1, 6);
        memory.allocate(p2, 6);

        //then
        assertThat(memory.size()).isEqualTo(40);
    }
}
