import static org.junit.Assert.*;
import org.junit.Test;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;


public class PlayerTest {
	Player testPlayer = new Player();
	Box playerBox = new Box(10, 10, 10);
	Geometry playerGem = new Geometry("Player", playerBox);
	RigidBodyControl playerControl = new RigidBodyControl(1f);

	@Test
	public void testShoot() {
		assertTrue(testPlayer.shoot());
		
		for(int i = 0; i < Common.STARTING_AMMO; i++)
			testPlayer.shoot();
		assertFalse(testPlayer.shoot());
	}

	@Test
	public void testHealthPack() {
		testPlayer.healthPack(null);
		assertEquals((int)Common.MAX_HEALTH, (int)testPlayer.getHealth());
	}

	@Test
	public void testAmmoPack() {
		testPlayer.ammoPack(null);
		assertEquals(Common.STARTING_AMMO + Common.AMMO_PACK, testPlayer.getAmmo());
	}

	@Test
	public void testTakeDamage() {
		playerGem.addControl(playerControl);
		testPlayer.setModel(playerGem);
		testPlayer.takeDamage(null);
		assertEquals((int)(Common.MAX_HEALTH - Common.BUILDING_DAMAGE), (int)testPlayer.getHealth());
		
		//Player goes below 0
		for(int i = 0; i < 20; i++)
			testPlayer.takeDamage(null);
		assertEquals(0, (int)testPlayer.getHealth());
	}

	@Test
	public void testGetDirection() {
		playerGem.addControl(playerControl);
		testPlayer.setModel(playerGem);
		testPlayer.takeDamage(null);
		//The player's initial direction
		assertEquals(Vector3f.ZERO, testPlayer.getDirection());
	}

	@Test
	public void testGetLocation() {
		playerGem.addControl(playerControl);
		testPlayer.setModel(playerGem);
		testPlayer.takeDamage(null);
		//The player's initial position
		assertEquals(new Vector3f(50f, 0f, 0f), testPlayer.getLocation());
		
		//Move player forward 100 units
		testPlayer.setPhysicsLocation(50f);
		assertEquals(new Vector3f(100f, 0f, 0f), testPlayer.getLocation());
	}
	
	@Test
	public void testStartPosition() {
		Quaternion correct = new Quaternion();
		assertTrue(testPlayer.initalizeQuaternion().toString()
				.equals(correct.toString()));
	}
}
