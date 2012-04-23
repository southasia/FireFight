import static org.junit.Assert.*;

import org.junit.Test;


public class StopwatchTest {
	Stopwatch testStopWatch = new Stopwatch();
	
	@Test
	public void testStartStop() {
		testStopWatch.start();
		assertTrue(testStopWatch.isRunning());
		testStopWatch.stop();
		assertFalse(testStopWatch.isRunning());
	}

	@Test
	public void testGetElapsedTime() {
		testStopWatch.start();
		
		//Sleep thread for 1 sec
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		testStopWatch.stop();
		
		//Testing elapsed time in ms, we want to be within 1000 +/- 2 ms error
		assertTrue(testStopWatch.getElapsedTime() < 1002 && testStopWatch.getElapsedTime() > 998);

		//Testing elapsed time in sec
		assertEquals(1, testStopWatch.getElapsedTimeSecs());
	}
}
