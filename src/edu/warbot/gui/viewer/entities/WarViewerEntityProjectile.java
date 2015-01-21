package edu.warbot.gui.viewer.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import edu.warbot.agents.WarProjectile;
import edu.warbot.game.Game;
import edu.warbot.game.Team;
import edu.warbot.gui.viewer.WarViewerObject;
import edu.warbot.gui.viewer.animations.WarViewerAnimationExplosion;
import edu.warbot.gui.viewer.screens.WarViewerScreen;

public class WarViewerEntityProjectile extends WarViewerEntity implements WarViewerObject {
	
	private Team team;
	private Sprite sprite;
	
	public WarViewerEntityProjectile(int id, int patchX, int patchY, int width,
			int height, Team team) {
		this.id = id;
		this.patchX = patchX;
		this.patchY = patchY;
		this.width = width;
		this.height = height;
		this.team = team;
		this.sprite = new Sprite(
				WarViewerScreen.GAME_SCREEN.getTexture("rocket"));
		
		x = getXByPatches();
		y = getYByPatches();
	}

	@Override
	public void render(float delta, OrthographicCamera camera, SpriteBatch batch) {
		updatePosition();
		batch.begin();
			batch.draw(sprite.getTexture(), getXByPatches(), getYByPatches(), width, height);
		batch.end();
	}
	
	private void updatePosition() {
		Game g = Game.getInstance();
		for (WarProjectile projectile: g.getPlayerTeam(team.getName()).getProjectiles()) {
			if (projectile.getID() == id) {
				patchX = (int) projectile.getX();
				patchY = (int) projectile.getY();
			}
		}
		
		
	}
	
	public WarViewerAnimationExplosion explode() {
		return new WarViewerAnimationExplosion(getXByPatches(), getYByPatches(), 32, 32, false);
	}

	public Team getTeam() {
		return team;
	}
}
