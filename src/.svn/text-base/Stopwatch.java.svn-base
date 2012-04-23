/**
 * Stop Watch class for keeping time
 */
public class Stopwatch {
	private long startTime = 0;
	private long stopTime = 0;
	private boolean running = false;

	/**
	 * Start the stop watch
	 */
	public void start() {
		this.startTime = System.currentTimeMillis();
		this.running = true;
	}

	/**
	 * Stop the stop watch
	 */
	public void stop() {
		this.stopTime = System.currentTimeMillis();
		this.running = false;
	}

	/**
	 * Check if the stop watch is running
	 * 
	 * @return true is running, false otherwise
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Returns the elapsed time in milliseconds
	 * 
	 * @return the elapsed time
	 */
	public long getElapsedTime() {
		long elapsed;
		if (running) {
			elapsed = (System.currentTimeMillis() - startTime);
		} else {
			elapsed = (stopTime - startTime);
		}
		return elapsed;
	}

	/**
	 * Returns the elapsed time in seconds
	 * 
	 * @return the elapsed time
	 */
	public long getElapsedTimeSecs() {
		long elapsed;
		if (running) {
			elapsed = ((System.currentTimeMillis() - startTime) / 1000);
		} else {
			elapsed = ((stopTime - startTime) / 1000);
		}
		return elapsed;
	}

}