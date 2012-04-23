import static org.junit.Assert.*;
import org.junit.Test;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;


public class CollisionHandlerTest {

	CollisionHandler handlerTest = new CollisionHandler(null, null);
	Box testBox = new Box();
	Geometry testObjectA = new Geometry("objectA", testBox);
	Geometry testObjectB = new Geometry("objectB", testBox);

	@Test
	public void testGetOtherObject() {
		assertEquals(testObjectA, handlerTest.getOtherObject(testObjectA, testObjectB, "objectB"));
		assertEquals(testObjectB, handlerTest.getOtherObject(testObjectA, testObjectB, "objectA"));
	}

	@Test
	public void testCheckObjectSpatialSpatialString() {
		assertTrue(handlerTest.checkObject(testObjectA, testObjectB, "objectA"));
		assertFalse(handlerTest.checkObject(testObjectA, testObjectB, "test"));
	}

	@Test
	public void testCheckObjectSpatialSpatialStringArray() {
		assertTrue(handlerTest.checkObject(testObjectA, testObjectB, new String[]{"objectA", "test"}));
		assertFalse(handlerTest.checkObject(testObjectA, testObjectB, new String[]{"test", "test2", "test3"}));
		assertFalse(handlerTest.checkObject(testObjectA, testObjectB, new String[]{}));
	}

}
