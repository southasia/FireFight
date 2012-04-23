import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.ui.Picture;
import com.jme3.util.SkyFactory;

/**
 * @author FireFight
 * 
 */
public class Graphics {
	boolean fullScreen;
	Game app;
	AssetManager assetManager;
	Node rootNode;
	Node guiNode;
	Picture healthBar;
	BulletAppState bulletAppState;
	Material defaultMat;
	Material normalsMat;
	Material bulletMat;
	Sphere sphere;
	Geometry mark;
	ParticleEmitter fire;
	AudioNode audio_gun;
	AudioNode audio_damage;
	MyStartScreen nifty;

	/**
	 * Graphics constructor
	 * 
	 * @param app
	 *            Game object
	 * @param collision
	 *            halnder ojbect
	 */
	public Graphics(Game appIn, CollisionHandler collision, MyStartScreen nifty) {
		fullScreen = false;
		this.nifty = nifty;
		this.app = appIn;
		assetManager = app.getAssetManager();
		rootNode = app.getRootNode();
		guiNode = app.getGuiNode();
		bulletAppState = new BulletAppState();
		app.getStateManager().attach(bulletAppState);
		bulletAppState.getPhysicsSpace().addCollisionListener(collision);
		//bulletAppState.getPhysicsSpace().enableDebug(assetManager);

		defaultMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		normalsMat = new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
		bulletMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

		sphere = new Sphere(32, 32, 0.4f, true, false);
		sphere.setTextureMode(TextureMode.Projected);

		rootNode.attachChild(SkyFactory.createSky(assetManager,
				"Materials/asteroids.dds", false));
	}

	public void destroyAll() {
		rootNode.detachAllChildren();
	}

	protected ParticleEmitter initFire() {
		fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
		Material mat_red = new Material(assetManager,
				"Common/MatDefs/Misc/Particle.j3md");
		mat_red.setTexture("Texture",
				assetManager.loadTexture("Effects/Explosion/flame.png"));
		fire.setMaterial(mat_red);
		fire.setImagesX(2);
		fire.setImagesY(2); // 2x2 texture animation
		fire.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f)); // red
		fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
		fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
		fire.setStartSize(1.5f);
		fire.setEndSize(0.1f);
		fire.setGravity(0, 0, 0);
		fire.setLowLife(1f);
		fire.setHighLife(3f);
		fire.getParticleInfluencer().setVelocityVariation(0.3f);
		return fire;
	}

	protected void attachFire(Vector3f collisionPoint) {
		initFire().setLocalTranslation(collisionPoint);
		rootNode.attachChild(fire);
	}

	/**
	 * @return player Spatial
	 */
	public Spatial createPlayer() {
		// Temporary hard code loading player model
		assetManager.registerLocator("assets/", FileLocator.class);
		Spatial playerModel = assetManager
				.loadModel("Models/Plane/arwing.scene");
		playerModel.setName("Player");
		playerModel.setMaterial(normalsMat);
		playerModel.scale(0.1f, 0.1f, 0.1f);

		CollisionShape playerShape = CollisionShapeFactory
				.createMeshShape((Node) playerModel);
		RigidBodyControl playerControl = new RigidBodyControl(playerShape, 1f);
		playerModel.addControl(playerControl);

		rootNode.attachChild(playerModel);
		bulletAppState.getPhysicsSpace().add(playerControl);
		playerControl.setGravity(new Vector3f().zero());

		return playerModel;
	}

	/**
	 * updates health bar based on amout of health
	 * 
	 * @param health
	 */
	public void updateHealthBar(float health) {
		nifty.setHealth(health);
	}

	/**
	 * @param cam
	 * @param playerModel
	 * @param inputManager
	 */
	public void setupCamera(Camera cam, Spatial playerModel,
			InputManager inputManager) {
		ChaseCamera chaseCam = new ChaseCamera(cam, playerModel, inputManager);
		chaseCam.setDefaultVerticalRotation(15 * FastMath.DEG_TO_RAD);
		chaseCam.setMinDistance(20f);
		chaseCam.setMaxDistance(20f);
	//	chaseCam.setDefaultVerticalRotation((FastMath.PI * 2));

	}

	protected void initAudio() {
		audio_gun = new AudioNode(assetManager, "Sound/Effects/Bang.wav", false);
		audio_gun.setLooping(false);
		audio_gun.setVolume(1);
		rootNode.attachChild(audio_gun);

		audio_damage = new AudioNode(assetManager, "Sound/Effects/Gun.wav",
				false);
		audio_damage.setLooping(false);
		audio_damage.setVolume(1);
		rootNode.attachChild(audio_damage);
	}
	
	public void setupLevel() {
		createFloor();
		createObstacles();
		createEnemies();
		createPowerups();
	}
	
	public void createFloor() {
		Box floor = new Box(Vector3f.ZERO, Common.LEVEL_LENGTH, 0.1f, 40f);
		Geometry floorGeo = createGeometry("Floor", floor, null, ColorRGBA.DarkGray);
		floorGeo.setLocalTranslation(0, Common.MIN_Y_BOUND - 1, 0);
		rootNode.attachChild(floorGeo);
	}

	/**
	 * creates all objects in the game
	 */
	public void createObstacles() {
		Texture obstacle_tex = assetManager.loadTexture("Materials/Images/building.jpg");

		for (int i = 1; i < Common.LEVEL_LENGTH; i += 5) {
			int blocksPerLayer = getRandom(0, Common.EASY);

			for (int block = 0; block < blocksPerLayer; block++) {
				int size = getRandom(1, 2);
				int height = getRandom(5, Common.MAX_Y_BOUND);
				int yCoord = Common.MIN_Y_BOUND;
				int zCoord = getRandom(Common.MIN_Z_BOUND, Common.MAX_Z_BOUND);
				
				Box newBox = new Box(size, height, size);
				Geometry newGem = createGeometry("Obstacle", newBox, obstacle_tex, ColorRGBA.randomColor());

				BoxCollisionShape boxShape = new BoxCollisionShape(
						new Vector3f(size, height, size));
				
				addControl(newGem, boxShape, 0f, new Vector3f(-1 * i, yCoord + height, zCoord));
				rootNode.attachChild(newGem);
			}
		}
	}

	/**
	 *   Creates the enemy objects and attaches them to 
	 *   the root node in the scene graph
	 */
	public void createEnemies() {
		Material enemy_mat = normalsMat.clone();
		
		for (int i = 1; i < Common.LEVEL_LENGTH; i += 5) {
			int enemiesPerLayer = getRandom(0, Common.EASY);

			for (int enemy = 0; enemy < enemiesPerLayer; enemy++) {
				int yCoord = getRandom(Common.MIN_Y_BOUND, Common.MAX_Y_BOUND);
				int zCoord = getRandom(Common.MIN_Z_BOUND, Common.MAX_Z_BOUND);

				assetManager.registerLocator("assets/", FileLocator.class);
				Spatial enemyModel = assetManager
						.loadModel("Models/Enemy/enemy.scene");
				enemyModel.setMaterial(enemy_mat);
				enemyModel.scale(0.05f, 0.05f, 0.05f);
				enemyModel.setName("Enemy");
				BoxCollisionShape enemyShape = new BoxCollisionShape(
						new Vector3f(1.5f, 1.5f, 1.5f));
				addControl(enemyModel, enemyShape, 1f, new Vector3f(-1 * i, yCoord, zCoord));
				
				rootNode.attachChild(enemyModel);
			}
		}
	}

	public void createPowerups() {
		for (int i = 1; i < Common.LEVEL_LENGTH; i += 5) {
			int powerupsPerLayer = getRandom(0, Common.EASY);

			for (int powerup = 0; powerup < powerupsPerLayer; powerup++) {
				int yCoord = getRandom(Common.MIN_Y_BOUND, Common.MAX_Y_BOUND);
				int zCoord = getRandom(Common.MIN_Z_BOUND, Common.MAX_Z_BOUND);

				Geometry newCylinderGem = randPowerUp();
				addControl(newCylinderGem, new SphereCollisionShape(1f), 0f, new Vector3f(-1 * i, yCoord,zCoord));
				rootNode.attachChild(newCylinderGem);
			}
		}
	}
	
	private Geometry randPowerUp() {
		Sphere newSphere = new Sphere(10, 10, 1f);
		Geometry newGem;

		int powerupType = getRandom(1, 3);
		if (powerupType == 1) {
			newGem = createGeometry("HealthPack", newSphere, null, ColorRGBA.Green);
		} else if (powerupType == 2) {
			newGem = createGeometry("Invincibility", newSphere, null, ColorRGBA.Yellow);
		} else {
			newGem = createGeometry("Ammo", newSphere, null, ColorRGBA.Orange);
		}
		
		return newGem;
	}
	
	private Geometry createGeometry(String name, Mesh mesh, Texture tex, ColorRGBA matColor) {
		Material newMat = defaultMat.clone();
		if(tex != null)
			newMat.setTexture("ColorMap", tex);
		newMat.setColor("Color", matColor);
		
		Geometry newGem = new Geometry(name, mesh);
		newGem.setMaterial(newMat);
		return newGem;
	}
	
	private void addControl(Spatial mesh, CollisionShape shape, float mass, Vector3f coord) {
		RigidBodyControl control = new RigidBodyControl(shape, mass);
		setupControl(mesh, null, control, coord);
	}
	
	private void addControl(Geometry geometry, CollisionShape shape, float mass, Vector3f coord) {
		RigidBodyControl control = new RigidBodyControl(shape, mass);
		setupControl(null, geometry, control, coord);
	}
	
	private void addControl(Geometry geometry, float mass, Vector3f coord, Vector3f direction) {
		RigidBodyControl control = new RigidBodyControl(mass);
		setupControl(null, geometry, control, coord);
		control.setLinearVelocity(direction);
	}
	
	private RigidBodyControl setupControl(Spatial mesh, Geometry geometry, RigidBodyControl control, Vector3f coord) {
		if(geometry != null)
			geometry.addControl(control);
		if(mesh != null)
			mesh.addControl(control);
		
		control.setKinematic(false);
		bulletAppState.getPhysicsSpace().add(control);
		control.setPhysicsLocation(coord);
		control.setGravity(new Vector3f().zero());
		return control;
	}

	/**
	 * @param localTranslation
	 * @param direction
	 */
	public void shoot(Vector3f location, Vector3f direction) {
		Geometry bullet = new Geometry("Bullet", sphere);
		bullet.setMaterial(getBulletMat());
		addControl(bullet, 1f, location, direction);
		rootNode.attachChild(bullet);
		audio_gun.playInstance(); // play each instance once!
	}
	
	public void changeScreen() {
		AppSettings settings = new AppSettings(true);
		settings.setTitle("FIREFIGHT");
		settings.setFullscreen(fullScreen);
		app.setSettings(settings);
		app.restart();
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates a random int between min and max
	 * @param min the minimum number
	 * @param max the maximum number
	 * @return integer the random int
	 */
	public int getRandom(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}
	
	/**
	 * Return Materials
	 */
	public Material getRainbowMat() {
		return normalsMat;
	}
	
	public Material getBulletMat() {
		bulletMat.setColor("Color", ColorRGBA.Orange);
		return bulletMat;
	}

	public Material getDamageMat() {
		defaultMat.setColor("Color", ColorRGBA.Red);
		return defaultMat;
	}

	public Material getInvincibilityMat() {
		defaultMat.setColor("Color", ColorRGBA.Yellow);
		return defaultMat;
	}

	public Material getHealthMat() {
		defaultMat.setColor("Color", ColorRGBA.Green);
		return defaultMat;
	}
}
