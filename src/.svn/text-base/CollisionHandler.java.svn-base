import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.scene.Spatial;

/**
 * Collision Handler for objects in the game
 */
public class CollisionHandler implements PhysicsCollisionListener {
	private Player player;
	private Graphics graphics;
	private Stopwatch stopwatch;
	private Stopwatch sound_stopwatch;

	public CollisionHandler(Stopwatch stopwatchIn, Stopwatch sound_stopwatchIn, Player playerIn) {
		player = playerIn;
		stopwatch = stopwatchIn;
		sound_stopwatch = sound_stopwatchIn;
		sound_stopwatch.start();
	}

	public void setGraphics(Graphics graphicsIn) {
		graphics = graphicsIn;
	}

	@Override
	public void collision(PhysicsCollisionEvent collision) {
		Spatial objectA = collision.getNodeA();
		Spatial objectB = collision.getNodeB();
		
		if (checkObject(objectA, objectB, "Obstacle") && !sound_stopwatch.isRunning())
			graphics.audio_damage.playInstance();
		
		if ((objectA.getName().equals("Bullet") && objectB.getName().equals("Enemy"))
				|| (objectA.getName().equals("Enemy") && objectB.getName().equals("Bullet"))){
			player.incrementScore(100); //You get many points for hitting an enemy
		}
		
		
		if ((objectA.getName().equals("Bullet") && objectB.getName().equals("Obstacle"))
				|| (objectA.getName().equals("Obstacle") && objectB.getName().equals("Bullet"))) {
			player.incrementScore(1); //You get points for hitting an object with your bullet!
			
			if (objectA.getName().equals("Bullet")) {
				objectA.removeFromParent();
				graphics.attachFire(collision.getPositionWorldOnA());
			} else {
				objectB.removeFromParent();
				graphics.attachFire(collision.getPositionWorldOnB());
			}
		}

		if (checkObject(objectA, objectB, "Player")) {
		
			//Switch Collision Group
			player.removeCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
		
			player.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
			
			
			
			stopwatch.start();
			
			Spatial otherObject = getOtherObject(objectA, objectB, "Player");
			if(checkObject(objectA, objectB, new String[]{"HealthPack", "Invincibility", "Ammo"}))
				playerPowerUpCollision(otherObject);
			else {
				player.takeDamage(graphics.getDamageMat());
				graphics.updateHealthBar(player.getHealth());
			}
		}
	
	}
	
	/**
	 * Return the object that is NOT name
	 * @param name the name of object
	 * @return Spatial Object
	 */
	public Spatial getOtherObject(Spatial objectA, Spatial objectB, String name) {
		if(objectA.getName().equals(name))
			return objectB;
		else
			return objectA;
	}

	/**
	 * Check rather objectA or objectB is equal to name
	 * @param name the name to check against
	 * @return true if objectA or objectB is equal name, false otherwise
	 */
	public boolean checkObject(Spatial objectA, Spatial objectB, String name) {
		return objectA.getName().equals(name) || objectB.getName().equals(name);
	}
	
	/**
	 * Check rather objectA or objectB is in nameList
	 * @param nameList the list of names to check
	 * @return true if objectA or objectB is in nameList, false otherwise
	 */
	public boolean checkObject(Spatial objectA, Spatial objectB, String[] nameList) {
		for(int i = 0; i < nameList.length; i++) {
			if(checkObject(objectA, objectB, nameList[i]))
				return true;
		}
		
		return false;	
	}
	
	private void playerPowerUpCollision(Spatial object) {
		//Remove power up from scene(game).
		object.removeFromParent();
		
		if(object.getName().equals("HealthPack")){
			player.healthPack(graphics.getHealthMat());
			graphics.updateHealthBar(player.getHealth());
		}
		else if(object.getName().equals("Ammo")) {
			player.ammoPack(graphics.getBulletMat());
		}
		else {
			player.setMaterial(graphics.getInvincibilityMat());
			player.incrementScore(500); //You get many points for being invincible
		}
		
		player.setPhysicsLocation(1f);
	}
}
