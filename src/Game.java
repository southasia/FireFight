import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.math.ColorRGBA;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

import de.lessvoid.nifty.Nifty;

/**
 * Main game class that initialize everything and start the game.
 */
public class Game extends SimpleApplication {
	private static Player player;
	boolean gameStarted;
	Graphics graphics;
	InputHandler input;
	CollisionHandler collision;
	Stopwatch stopwatch;
	Stopwatch sound_stopwatch;
	AudioNode audio_gun;
	MyStartScreen startScreen;

	/**
	 * Main program
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Game app = new Game();
		
		//Configure App Setting
		AppSettings settings = new AppSettings(true);
        
		//Set Game Icon
		try {
			String curDir = System.getProperty("user.dir");
			settings.setIcons(new BufferedImage[]{
					ImageIO.read(new FileInputStream(curDir + "/assets/Interface/Icons/space256.png")),
					ImageIO.read(new FileInputStream(curDir + "/assets/Interface/Icons/space128.png")),
					ImageIO.read(new FileInputStream(curDir + "/assets/Interface/Icons/space32.png")),
					ImageIO.read(new FileInputStream(curDir + "/assets/Interface/Icons/space16.png")),
			 });
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Set FrameRate
		  settings.setFrameRate(Common.FRAME_RATE);
		//Set Title
		  settings.setTitle("FIREFIGHT");
		//Save Setting
		  app.setSettings(settings);
		  
		  app.start(); // start the game
	}

	/**
	 * Returns the player object
	 * 
	 * @return player
	 */
	public static Player getPlayer() {
		return player;
	}

	/**
	 * Initializes all the main elements of the game.
	 */
	@Override
	public void simpleInitApp() {
		gameStarted = false;
		assetManager.registerLocator("assets/", FileLocator.class.getName());

		// Start Menu Initialization
		NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
				inputManager, audioRenderer, guiViewPort);
		Nifty nifty = niftyDisplay.getNifty();
		// Read XML and initialize custom ScreenController
		startScreen = new MyStartScreen(this, settings.getWidth());
    	nifty.fromXml("Interface/HelloJme3.xml", "start", startScreen);
		// Attach the Nifty display to the gui view port as a processor
		guiViewPort.addProcessor(niftyDisplay);

		viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));

		// Disable the fly cam
		flyCam.setDragToRotate(true);
		flyCam.setEnabled(false);

		stopwatch = new Stopwatch();
		sound_stopwatch = new Stopwatch();
		player = new Player();
		collision = new CollisionHandler(stopwatch, sound_stopwatch, player);
		graphics = new Graphics(this, collision, startScreen);
		input = new InputHandler(this, graphics, player);

		// Initializing gameplay
		collision.setGraphics(graphics);
		Spatial playerModel = graphics.createPlayer();
		player.setModel(playerModel);
		graphics.setupCamera(cam, playerModel, inputManager);
		graphics.setupLevel();
		graphics.initAudio();
		
		//Set render distance to get better performance
		cam.setFrustumFar(300);
	    cam.onFrameChange();
	}
	
	/**
	 * Start the actual game loop
	 */
	public void startGame() {
		mouseInput.setCursorVisible(false);
		gameStarted = true;
	}

	/**
	 * Main update loop
	 * 
	 * @see com.jme3.app.SimpleApplication#simpleUpdate(float)
	 */
	public void simpleUpdate(float tpf) {
		// Don't start actual game until we hit start from Start Menu
		if (gameStarted) {
			if (sound_stopwatch.isRunning() && sound_stopwatch.getElapsedTimeSecs() >= 1)
				sound_stopwatch.stop();
			
			// Resetting Collision Detection, makes player invulnerable for 1
			// sec
			if (stopwatch.isRunning()) {
				int noDamageTime;
				if (player.isDamaged())
					noDamageTime = Common.DAMAGED_INVINCIBILITY_TIME;
				else if (player.hitHealthPack() || player.hitAmmoPack())
					noDamageTime = Common.HEALTH_PACK_INVINCIBILITY_TIME;
				else
					noDamageTime = Common.INVINCIBILITY_PACK_TIME;

				if (stopwatch.getElapsedTimeSecs() >= noDamageTime) {
					player.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
					player.setMaterial(graphics.getRainbowMat());
					stopwatch.stop();
					if (player.isDamaged())
						player.setDamaged(false);
					if (player.hitHealthPack())
						player.setHitHealthPack(false);
					if (player.hitAmmoPack())
						player.setHitAmmoPack(false);
				}
			}

			cam.setRotation(player.getPhysicsRotation());
			player.update(cam);
			startScreen.updateDist(player.getXPos());
			startScreen.setAmmo(player.getAmmo());
			startScreen.setScore(player.getScore());
			
			if(player.getXPos() >= Common.LEVEL_LENGTH || player.getHealth() <= 0){
				//restartGame();
				startScreen.toScreen("gameOver");
				startScreen.setGameoverScore(player.getScore());
				if(player.getXPos() >= Common.LEVEL_LENGTH) startScreen.setGameoverText(true);
				mouseInput.setCursorVisible(true);
				gameStarted=false;
				
			}
		}
	}


	public void restartGame(){
		graphics.destroyAll();
		player=null;
		graphics=null;
		input=null;
		collision=null;
		
		startScreen.resetProgress();
		startScreen=null;

		//////////////////////////Restart Game////////////////////////
		
		// Start Menu Initialization
		NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
				inputManager, audioRenderer, guiViewPort);
		Nifty nifty = niftyDisplay.getNifty();
		// Read XML and initialize custom ScreenController
		startScreen = new MyStartScreen(this, settings.getWidth());
    	nifty.fromXml("Interface/HelloJme3.xml", "hud", startScreen);
		// Attach the Nifty display to the gui view port as a processor
		guiViewPort.addProcessor(niftyDisplay);

		viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));

		// Disable the fly cam
		flyCam.setDragToRotate(true);
		flyCam.setEnabled(false);

		stopwatch = new Stopwatch();
		sound_stopwatch = new Stopwatch();
		player = new Player();
		collision = new CollisionHandler(stopwatch, sound_stopwatch, player);
		graphics = new Graphics(this, collision, startScreen);
		input = new InputHandler(this, graphics, player);

		// Initializing gameplay
		collision.setGraphics(graphics);
		Spatial playerModel = graphics.createPlayer();
		player.setModel(playerModel);
		graphics.setupCamera(cam, playerModel, inputManager);
		graphics.setupLevel();
		graphics.initAudio();
		
		//Set render distance to get better performance
		cam.setFrustumFar(300);
	    cam.onFrameChange();
	    gameStarted=true;
	    mouseInput.setCursorVisible(false);
	}
}