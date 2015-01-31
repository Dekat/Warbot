package edu.warbot.gui.viewer.screens;


import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import edu.warbot.game.WarGame;
import edu.warbot.gui.viewer.WarViewerGdx;
import edu.warbot.gui.viewer.WarViewerMap;
import edu.warbot.gui.viewer.WarViewerWorld;
import edu.warbot.gui.viewer.animations.WarViewerAnimationExplosion;
import edu.warbot.gui.viewer.entities.WarViewerEntityControllable;
import edu.warbot.gui.viewer.entities.WarViewerEntityFood;
import edu.warbot.gui.viewer.entities.WarViewerEntityProjectile;
import edu.warbot.gui.viewer.util.WarViewerKeyListener;

public class WarViewerScreen implements Screen, Observer {

	public static WarViewerScreen GAME_SCREEN;
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private WarViewerKeyListener keyListener;
	private HashMap<String, Texture> textures;
	private WarViewerWorld world;
	
	private WarViewerGdx gameGdx;
	
	private boolean newTick;
	
	private boolean showInfo;
	private boolean showInitialsUnities;

	public WarViewerScreen(WarViewerGdx gameGdx) {
		batch = new SpriteBatch();

		camera = new OrthographicCamera(WarViewerGdx.WIDTH, WarViewerGdx.HEIGHT);
		camera.setToOrtho(false, WarViewerGdx.WIDTH, WarViewerGdx.HEIGHT);

		keyListener = new WarViewerKeyListener(camera);
		
		this.gameGdx = gameGdx;

		GAME_SCREEN = this;
		
		Gdx.input.setInputProcessor(keyListener);
		
		loadTextures();
		
		gameGdx.getGame().addObserver(this);
		
		newTick = false;
		
		showInfo = true;
		showInitialsUnities = true;
	}

	private void loadTextures() {
		textures = new HashMap<String, Texture>();
		textures.put(
				"base_blue",
				new Texture(Gdx.files
						.internal("textures/agents/base_blue.png")));
		textures.put(
				"base_red",
				new Texture(Gdx.files
						.internal("textures/agents/base_red.png")));
		textures.put(
				"turret_blue",
				new Texture(Gdx.files
						.internal("textures/agents/turret_blue.png")));
		textures.put(
				"turret_red",
				new Texture(Gdx.files
						.internal("textures/agents/turret_red.png")));
		textures.put(
				"sheet_blue",
				new Texture(Gdx.files
						.internal("textures/agents/sheet_blue.png")));
		textures.put(
				"sheet_red",
				new Texture(Gdx.files.internal("textures/agents/sheet_red.png")));
		textures.put("food",
				new Texture(Gdx.files.internal("textures/resources/food.png")));
		textures.put(
				"rocket",
				new Texture(Gdx.files
						.internal("textures/projectiles/rocket.png")));
		textures.put(
				"explosion",
				new Texture(Gdx.files
						.internal("textures/animations/explosion.png")));
		textures.put(
				"explosion_mark",
				new Texture(Gdx.files
						.internal("textures/animations/explosion_mark.png")));
		
		textures.put(
				"health_bar",
				new Texture(Gdx.files
						.internal("textures/info/health_bar.png")));
		
		textures.put(
				"food_indicator",
				new Texture(Gdx.files
						.internal("textures/info/food_indicator.png")));
		
		textures.put(
				"unities_info",
				new Texture(Gdx.files
						.internal("textures/info/unities_info.png")));
		
	}

	@Override
	public void dispose() {
		world.disposeEntities();
		batch.dispose();	
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}
	
	@Override
	public void show() {
		world = new WarViewerWorld(gameGdx.getGame());
		world.initEntities();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1); // fond en blanc
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		if (!endGame()) {
			if (newTick) {
				world.updateWorld();
				newTick = false;
			}
			
			world.getMap().render(delta, camera, batch);
			
			for (WarViewerAnimationExplosion entity: world.getEntitiesControllableExplosions()) {
				if (entity.isFinished()) { //affichage des traces d explosion en premier
					entity.render(delta, camera, batch);
				}
			}
			
			for (WarViewerEntityFood entity : world.getEntitiesResource()) {
				entity.render(delta, camera, batch);
			}
			
			for (WarViewerEntityControllable entity: world.getEntitiesControllable()) {
				entity.render(delta, camera, batch);
			}
			for (WarViewerEntityProjectile entity: world.getEntitiesProjectile()) {
				entity.render(delta, camera, batch);
			}
			
			if (showInfo) {
				for (WarViewerEntityControllable entity: world.getEntitiesControllable()) {
					entity.getHealthBar().render(delta, camera, batch);
					entity.getFoodIndicator().render(delta, camera, batch);	
				}
			}
			
			if (showInitialsUnities) {
				for (WarViewerEntityControllable entity: world.getEntitiesControllable()) {
					entity.getUnityInfo().render(delta, camera, batch);
				}
			}
			
			for (WarViewerAnimationExplosion entity: world.getEntitiesControllableExplosions()) {
				if (!entity.isFinished()) { // affichage des explosions au dessus du reste
					entity.render(delta, camera, batch);
				}
			}
		}
	}


	private boolean endGame() {
		// TODO utiliser le pattern observer (cette classe observe Game de warbot)
		return gameGdx.getGame().getPlayerTeams().size() < 2;
	}

	@Override
	public void resize(int arg0, int arg1) {

	}

	@Override
	public void resume() {

	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public Texture getTexture(String key) {
		return textures.get(key);
	}

	public WarViewerGdx getGameGdx() {
		return gameGdx;
	}
	
	public WarViewerMap getMap() {
		return world.getMap();
	}
	
	public void setShowInfo(boolean showInfo) {
		this.showInfo = showInfo;
	}

	public boolean isShowInfo() {
		return showInfo;
	}

	public boolean isShowInitialsUnities() {
		return showInitialsUnities;
	}

	public void setShowInitialsUnities(boolean showInitialsUnities) {
		this.showInitialsUnities = showInitialsUnities;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if ((Integer)arg1 == WarGame.NEW_TICK) {
			newTick = true;
		}
	}
}
