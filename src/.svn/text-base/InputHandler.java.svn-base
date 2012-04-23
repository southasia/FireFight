import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

/**
 * Input handler for our game Sets up mapping from keyboard to actions
 */
public class InputHandler implements ActionListener {
	InputManager inputManager;
	Graphics graphics;
	Player player;

	/**
	 * Constructor
	 * 
	 * @param app the game object
	 * @param graphicsIn the graphics object
	 * @param playerIn the player object
	 */
	public InputHandler(Game app, Graphics graphicsIn, Player playerIn) {
		inputManager = app.getInputManager();
		graphics = graphicsIn;
		player = playerIn;

		setupKeys();
	}

	/**
	 * Adds keyboard mapping to the inputManager, and add listeners for them
	 */
	private void setupKeys() {
		
		//For functional Keys
		inputManager.addMapping("RShift", new KeyTrigger(KeyInput.KEY_F));
		inputManager.addListener(this, "RShift");
		// For using ADWS
		inputManager.addMapping("CharLeft", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("CharRight", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("CharUp", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("CharDown", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addListener(this, "CharLeft");
		inputManager.addListener(this, "CharRight");
		inputManager.addListener(this, "CharUp");
		inputManager.addListener(this, "CharDown");

		// For using arrow keys
		inputManager.addMapping("ArrowLeft", new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addMapping("ArrowRight", new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addMapping("ArrowUp", new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping("ArrowDown", new KeyTrigger(KeyInput.KEY_DOWN));
		inputManager.addListener(this, "ArrowLeft");
		inputManager.addListener(this, "ArrowRight");
		inputManager.addListener(this, "ArrowUp");
		inputManager.addListener(this, "ArrowDown");

		// Shooting
		inputManager.addMapping("Shoot", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener(this, "Shoot");
		
		inputManager.addMapping("Fullscreen", new KeyTrigger(KeyInput.KEY_F1));
		inputManager.addListener(this, "Fullscreen");
	}

	/**
	 * ActionListener for inputs handles keyboard events to actions for the
	 * game.
	 */
	@Override
	public void onAction(String binding, boolean isPressed, float tpf) {
		if (binding.equals("CharLeft") || binding.equals("ArrowLeft"))
			player.updateControl("left", isPressed);
		else if (binding.equals("CharRight") || binding.equals("ArrowRight"))
			player.updateControl("right", isPressed);
		else if (binding.equals("CharUp") || binding.equals("ArrowUp"))
			player.updateControl("up", isPressed);
		else if (binding.equals("CharDown") || binding.equals("ArrowDown"))
			player.updateControl("down", isPressed);
		else if (binding.equals("Shoot") && !isPressed) {
			if (player.shoot())
				graphics.shoot(player.getLocation(), player.getDirection());
		}
		else if(binding.equals("Fullscreen")) {
			graphics.fullScreen = !graphics.fullScreen;
			graphics.changeScreen();
		}
		
	}
}
