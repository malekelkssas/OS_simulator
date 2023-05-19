package mutexes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.Process;
import storage.State;
import static org.assertj.core.api.Assertions.*;

public class MutexTest {
	Mutex mutex;

	@BeforeEach
	void init() {
		mutex = Mutex.getInstance();
	}

	@Test
	void testUserInputMutex_TwoProcesses_SecondShouldBeBlocked() {
		// Given 2 Processes and one resource
		Process process1 = new Process(1);
		Resource resource = Resource.userInput;
		Process process2 = new Process(2);

		// When both of them are trying to access the same resource
		mutex.semWait(resource, process1);
		mutex.semWait(resource, process2);

		// Then the second one should be blocked
		assertThat(process2.getState()).isEqualTo(State.BLOCKED);
	}

	@Test
	void testTwoDifferentMutex_TwoProcesses_NoOneShouldBeBlocked() {
		// Given 2 Processes and two resources
		Process process1 = new Process(1);
		Resource resource1 = Resource.userInput;
		Process process2 = new Process(2);
		Resource resource2 = Resource.userOutput;

		// When each of them is trying to access only one of the resources
		mutex.semWait(resource1, process1);
		mutex.semWait(resource2, process2);

		// Then no one should be blocked
		assertThat(process2.getState()).isNotEqualTo(State.BLOCKED);
	}

}
