import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Screen Controller for the Start Menu screen
 */
public class MyStartScreen implements ScreenController {

	private Nifty nifty;
	private Screen screen;
	private Game app;
	private int width;

	public MyStartScreen(Game app, int width) {
		this.app = app;
		this.width = width;
	}

	/**
	 * Binds the nifty GUI objects
	 */
	public void bind(Nifty nifty, Screen screen) {
		this.nifty = nifty;
		this.screen = screen;
	}

	/**
	 * Switch to the actual game screen and start the game
	 * 
	 * @param nextScreen
	 */
	public void startGame(String nextScreen) {
		nifty.gotoScreen(nextScreen);
		app.startGame();
	}

	public void restartGame() {
		nifty.gotoScreen("hud");
		app.restartGame();
	}
	
	public void toScreen(String nextScreen){
		nifty.gotoScreen(nextScreen);
	}
	/**
	 * Quit the game
	 */
	public void quitGame() {
		nifty.exit();
		app.stop();
	}
	
	public void setGameoverText(boolean winner){
		if(winner){
			Element niftyElement = nifty.getCurrentScreen().findElementByName("gotext");
			niftyElement.getRenderer(TextRenderer.class).setText("You have defeated the alien invasion and brought peace to the galaxy. Hooray!");
		}
		
	}
	public void setGameoverScore(int score){
			Element niftyElement = nifty.getCurrentScreen().findElementByName("scoretext");
			niftyElement.getRenderer(TextRenderer.class).setText("Score: "+score);
	}
	public void resetProgress(){
		Element niftyElement = nifty.getCurrentScreen().findElementByName("player");
		niftyElement.setWidth(0);
	}
	
	public Element getNiftyElement(String name){
		return nifty.getCurrentScreen().findElementByName(name);
	}
	public void setAmmo(int ammo){
		if(nifty != null && nifty.getCurrentScreen()!=null){
			Element niftyElement = nifty.getCurrentScreen().findElementByName("ammotext");
			niftyElement.getRenderer(TextRenderer.class).setText("Ammo: "+Integer.toString(ammo));
		}
	}
	public void setScore(int score){
		if(nifty != null && nifty.getCurrentScreen()!=null){
			Element niftyElement = nifty.getCurrentScreen().findElementByName("scoretext");
			niftyElement.getRenderer(TextRenderer.class).setText("Score: "+Integer.toString(score));
		}
	}
	public void setHealth(float health){
		if(nifty != null && nifty.getCurrentScreen()!=null && nifty.getCurrentScreen().getScreenId().equals("hud")){
			Element niftyElement = nifty.getCurrentScreen().findElementByName("health");
			int ratio = (int) ((health / Common.MAX_HEALTH) * (width*.33));
			niftyElement.setWidth(ratio);
		}
	}
	public int updateDist(float dist){
		if(dist < 0) dist = 0;
		int ratio = (int) ((dist / Common.LEVEL_LENGTH) * (width*.33));

		if(nifty != null && nifty.getCurrentScreen()!=null){
			Element niftyElement = nifty.getCurrentScreen().findElementByName("player");
			niftyElement.setWidth(ratio);
		} 
		
		if (ratio < 100)
			return ratio;
		if(ratio < 0)
			return 0;
		else return 100;
		
	}
	@Override
	public void onEndScreen() {

	}

	@Override
	public void onStartScreen() {

	}
}