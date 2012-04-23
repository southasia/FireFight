import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class MyStartScreenTest {
	int screenWidth = 100;
	MyStartScreen nifty = new MyStartScreen(null, screenWidth);
	
	@Test
	public void testPlayerProgress(){
		int playerDistance = 10;
		int niftyDist = (int) nifty.updateDist(playerDistance);
		assertTrue(niftyDist == 0);
	}
	
	@Test
	public void testPlayerProgress2(){
		int playerDistance = 2600;
		int niftyDist = (int) nifty.updateDist(playerDistance);
		assertTrue(niftyDist == 34);
	}
}
