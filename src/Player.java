import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

/**
 * @author FireFight
 */
class Player {
	private boolean left, right, up, down;
	private Spatial model;
	private RigidBodyControl control;

	private float flyingSpeed;
	private float health;
	private int ammo;
	private int score;
	private boolean isDamaged;
	private boolean hitHealthPack;
	private boolean hitAmmoPack;
	private Vector3f flyDirection = new Vector3f(-1.0f, 0.0f, 0.0f);
	private Vector3f baseSpeed = new Vector3f(-30.0f, 0.0f, 0.0f);

	private Quaternion startQuaternion;
	private Quaternion upDownRotation;
	private Quaternion rollRotation;
	
	private static final float MAX_ANGLE = 30.0f;

	private float pitch = 0.0f;
	private final float prIncrement = 0.8f;
	private float roll = 0.0f;

	protected Player() {
		// Default Values
		flyingSpeed = Common.FLYING_SPEED;
		health = Common.MAX_HEALTH;
		ammo = Common.STARTING_AMMO;
		score=0; //Score starts out at 0
		
		left = right = up = down = false;
		upDownRotation = new Quaternion();
		rollRotation = new Quaternion();
		upDownRotation = upDownRotation.fromAngleNormalAxis(pitch
				* FastMath.DEG_TO_RAD, new Vector3f(0.0f, 0.0f, 1.0f));
		rollRotation = rollRotation.fromAngleNormalAxis(pitch
				* FastMath.DEG_TO_RAD, new Vector3f(-1.0f, 0.0f, 0.0f));
	}

	/**
	 * Update what buttons are pressed
	 * 
	 * @param direction
	 * @param value
	 */
	public void updateControl(String direction, boolean value) {
		if (direction.equals("left"))
			left = value;
		else if (direction.equals("right"))
			right = value;
		else if (direction.equals("up"))
			up = value;
		else if (direction.equals("down"))
			down = value;
	}
	
	/**
	 * Updates the player models
	 * 
	 * @param cam
	 *            game camera
	 */
	public void update(Camera cam) {
		float currentX = this.control.getPhysicsLocation().x;
		float currentY = this.control.getPhysicsLocation().y;
		float currentZ = this.control.getPhysicsLocation().z;

		checkBoundsButtonPressed(currentY, currentZ);		
		checkBoundsNoButtonPress(currentX, currentY, currentZ);	

		Quaternion currentQuaternion = updateQuaternion();
		this.control.setPhysicsRotation(currentQuaternion);
		this.control.setLinearVelocity(flyDirection);
	}

	/**
	 * 
	 * Updates the rotation quaternion of the player
	 * and applies the quaternion to the players 
	 * flydirection vector
	 * @return
	 */
	private Quaternion updateQuaternion() {
		Quaternion currentQuaternion = control.getPhysicsRotation();
		currentQuaternion.normalizeLocal();

		upDownRotation.fromAngleNormalAxis(pitch * FastMath.DEG_TO_RAD,
				currentQuaternion.getRotationColumn(2));
		rollRotation.fromAngleNormalAxis(roll * FastMath.DEG_TO_RAD,
				currentQuaternion.getRotationColumn(1));
		currentQuaternion = this.initalizeQuaternion();
		currentQuaternion.apply(upDownRotation.toRotationMatrix());
		currentQuaternion.apply(rollRotation.toRotationMatrix());
		currentQuaternion.normalizeLocal();
		flyDirection = currentQuaternion.mult(baseSpeed);
		return currentQuaternion;
	}

	/**
	 * Based on what button is pressed, the player's direction
	 * and rotation is changed
	 * @param currentY
	 * @param currentZ
	 */
	private void checkBoundsButtonPressed(float currentY, float currentZ) {
		if (down) {
			if(currentY < Common.MAX_Y_BOUND){
				if (pitch > -MAX_ANGLE)
					pitch -= prIncrement;
				}else{
				pitch = 0;
			}
		} else if (up) {
			if(currentY >= Common.MIN_Y_BOUND){
			if (pitch < MAX_ANGLE)
				pitch += prIncrement;
			}else{
				pitch = 0;
			}
		}
		if (right) {
			if(currentZ > Common.MIN_Z_BOUND){
				if (roll > -MAX_ANGLE)
					roll -= prIncrement;
				}else{
					roll = 0;
			}
		}
		if (left) {
			if(currentZ <= Common.MAX_Z_BOUND){
				if (roll < MAX_ANGLE)
					roll += prIncrement;
			}else{
					roll = 0;
			}
		}
	}

	private void checkBoundsNoButtonPress(float currentX, float currentY,
			float currentZ) {
		if(currentY > Common.MAX_Y_BOUND){			 
			 control.setPhysicsLocation(new Vector3f(currentX, Common.MAX_Y_BOUND, currentZ));
			 pitch  = 0.0f;				
		 }	 

		 if(currentY < Common.MIN_Y_BOUND){
			 control.setPhysicsLocation(new Vector3f(currentX, Common.MIN_Y_BOUND, currentZ));
			 pitch  = 0.0f;
		 }
		 
		 if(currentZ > Common.MAX_Z_BOUND){			 
			 control.setPhysicsLocation(new Vector3f(currentX, currentY, Common.MAX_Z_BOUND));
			 pitch  = 0.0f;		
			 roll = 0.0f;
		 }	 

		 if(currentZ < Common.MIN_Z_BOUND){
			 control.setPhysicsLocation(new Vector3f(currentX, currentY, Common.MIN_Z_BOUND));
			 pitch  = 0.0f;
			 roll = 0.0f;
		 }
	}
	
	public boolean shoot() {
		if(ammo > 0) {
			ammo -= 1;
			return true;
		}
		return false;
	}
	
	public void healthPack(Material material) {
		setMaterial(material);
		health += Common.HEALTH_PACK_POINTS;
		
		if (health > Common.MAX_HEALTH)
			health = Common.MAX_HEALTH;
		
		hitHealthPack = true;
		incrementScore( (int) Common.HEALTH_PACK_POINTS); //Your score increases when you hit a health pack
	}
	
	public void ammoPack(Material material) {
		setMaterial(material);
		ammo += Common.AMMO_PACK;
		hitAmmoPack = true;
		incrementScore(Common.AMMO_PACK); //Your score increases when you hit an ammo pack
	}

	/**
	 * Does Damage to the player
	 * 
	 * @param damage
	 */
	public void takeDamage(Material material) {
		setMaterial(material);
		
		// Stops collision rotation
		control.setAngularVelocity(new Vector3f(0.0f, 0.0f, 0.0f));
		control.setPhysicsRotation(startQuaternion);
		
		setPhysicsLocation(3f);
		
	
		
		health -= Common.BUILDING_DAMAGE;
		if (health < 0)
			health = 0;
		
		isDamaged = true;
		incrementScore( (int) Common.BUILDING_DAMAGE); //Your score suffers when you hit a building
	}

	/**
	 * @param group
	 */
	public void setCollisionGroup(int group) {
		control.setCollisionGroup(group);
	}

	/**
	 * @param group
	 */
	public void removeCollisionGroup(int group) {
		control.removeCollideWithGroup(group);
	}

	/**
	 * @param modelIn
	 */
	public void setModel(Spatial modelIn) {
		model = modelIn;
		control = (RigidBodyControl) model.getControl(0);
		startQuaternion = model.getLocalRotation();
	}
	
	public void setMaterial(Material material) {
		if(material != null)
			model.setMaterial(material);
	}
	
	public Quaternion getPhysicsRotation() {
		return control.getPhysicsRotation();
	}
	
	public void setPhysicsLocation(float x) {
		Vector3f posX = control.getPhysicsLocation().addLocal(x, 0, 0);
		control.setPhysicsLocation(posX);
	}

	/**
	 * @return
	 */
	public Quaternion initalizeQuaternion() {
		Quaternion toreturn = new Quaternion();
		return toreturn.fromAngles(0.0f, 0.0f, 0.0f);
	}
	
	private Vector3f getNormalDirection() {
		Vector3f direction = control.getLinearVelocity();
		direction = direction.normalize();
		direction = direction.mult(2f);
		return direction;
	}

	/**
	 * @return
	 */
	public Vector3f getDirection() {
		Vector3f direction = getNormalDirection();
		return direction.mult(1.0f * flyingSpeed);
	}
	
	/**
	 * @return
	 */
	public Vector3f getLocation() {
		Vector3f location = control.getPhysicsLocation();
		Vector3f direction = getNormalDirection();
		return location.add(direction);
	}

	/**
	 * GETTERS/SETTERS
	 */
	public float getHealth() {
		return health;
	}
	
	public int getAmmo() {
		return ammo;
	}

	public void setDamaged(boolean damagedIn) {
		isDamaged = damagedIn;
	}

	public boolean isDamaged() {
		return isDamaged;
	}
	
	public void setHitHealthPack(boolean hpIn) {
		hitHealthPack = hpIn;
	}
	
	public boolean hitHealthPack() {
		return hitHealthPack;
	}
	
	public void setHitAmmoPack(boolean ammoIn) {
		hitAmmoPack = ammoIn;
	}
	
	public boolean hitAmmoPack() {
		return hitAmmoPack;
	}
	
	public float getXPos(){
		return Math.abs(control.getPhysicsLocation().x);
	}
	public int getScore(){
		return score;
	}
	public void incrementScore(int value){
		score += value;
		if(score< 0) score=0;
	}
}
