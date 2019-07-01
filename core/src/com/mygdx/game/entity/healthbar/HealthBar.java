package com.mygdx.game.entity.healthbar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.entity.LivingEntity;

public abstract class HealthBar {
	private static final int BORDER = 2;
	private LivingEntity entity;
	private Texture texture;
	private Texture background;

	private float x;
	private float y;
	private float width;

	public HealthBar(LivingEntity entity, Texture texture) {
		this.entity = entity;
		this.texture = texture;
		this.background = new Texture(Gdx.files.internal("HealthBar/Background.png"));
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void render(SpriteBatch batch) {
		float width = entity.getHealth() / entity.getMaxHealth() * this.width;
		if (width < 0) {
			width = 0;
		}

		batch.draw(background, x, y - BORDER, this.width, background.getHeight());
		batch.draw(texture, x, y, width, texture.getHeight());
	}
}
