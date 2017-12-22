package com.zodiac;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zodiac.Screens.GameScreen;
import com.zodiac.Screens.Menu;
import com.zodiac.Screens.ZodiacScreen;
import com.zodiac.Support.AM;
import com.zodiac.Support.Messagable;
import com.zodiac.Support.NetMaster;
import com.zodiac.Support.SoundMaster;
import com.zodiac.UI.ShipGroup;

import java.util.ArrayList;

public class ZodiacReprisal extends Game implements Messagable {

	private SpriteBatch batch;
	private NetMaster netMaster;
	//private ZodiacScreen currentScreen;
	private Menu menu;
	private GameScreen gameScreen;
	private boolean gameStarted;
	private ArrayList<ShipGroup> shipGroups;
	private int playerCount;

	/**Things todo:
	 * Smooth zooming by setting target zoom and transitioning to it
	 * Add condition to make sure player has at least one ship DONE
	 * Make sure chat works well in new game screen DONE
	 * Fog of war
	 * Spawn ships of every player DONE
	 * Decide who spawns where randomly DONE
	 * Spawn random powerups
	 * Determine map size and check out zoom DONE
	 * List of players and how many ships they have left DONE
	 * Ship movement and clicking and orders DONE
	 * Special orders e.g. shut off all power, teleport
	 * Turrets
	 * Collisions
	 * Restart server on disconnect
	 * Voices when ships clicked
	 * More music depending on faction
	 * More factions because yolo*/

	@Override
	public void create () {
		batch = new SpriteBatch();
		SoundMaster.initialize();
		AM.loadAll();

		boolean skipMenu = true;

		if(skipMenu){
			gameScreen = new GameScreen(this);
			setScreen(gameScreen);
		}
		else{
			netMaster = new NetMaster(this);
			menu = new Menu(this);
			setScreen(menu);
		}
	}

	@Override
	public void render () {
		super.render();
		if(gameStarted&&gameScreen==null) {
			gameScreen = new GameScreen(this, playerCount, shipGroups);
			setScreen(gameScreen);
			menu = null;
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	/*public void swapScreens(ZodiacScreen newScreen, ZodiacScreen oldScreen){
		this.setScreen(newScreen);
		currentScreen = newScreen;
		oldScreen.dispose();
	}*/

	public SpriteBatch getBatch(){
		return batch;
	}

	public void startGame(int playerCount, ArrayList<ShipGroup> shipGroups){
		gameStarted = true;
		this.playerCount = playerCount;
		this.shipGroups = shipGroups;
	}

	@Override
	public void messageReceived(int type,String message) {
		if(gameScreen==null)
			menu.messageReceived(type,message);
		else
			gameScreen.messageReceived(type,message);
	}

	@Override
	public void sendMessage(int type, String message) {
		netMaster.sendMessage(type, message);
	}
}
