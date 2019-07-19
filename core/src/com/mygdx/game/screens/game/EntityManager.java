package com.mygdx.game.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * ADT to store all entities and render them.
 */
public class EntityManager {
	public static final int ROCK_RENDER_PRIORITY = 4;
	public static final int SHURIKEN_RENDER_PRIORITY = 3;
	public static final int CHARACTER_RENDER_PRIORITY = 2;
	public static final int BOSS_RENDER_PRIORITY = 1;

	private static final float DISPOSE_SPEED = 2f;

	private TreeMap<Integer, HashSet<Entity>> entitiesPriorityMap;

	public EntityManager() {
		entitiesPriorityMap = new TreeMap<>();
	}

	public EntityManager add(Entity entity, Integer priority) {
		HashSet<Entity> entities = entitiesPriorityMap.get(priority);
		if (entities == null) {
			entities = new HashSet<>();
			entitiesPriorityMap.put(priority, entities);
		}

		entities.add(entity);
		return this;
	}

	public void render(SpriteBatch batch) {
		for (HashSet<Entity> entities : entitiesPriorityMap.values()) {
			Iterator<Entity> iterator = entities.iterator();
			while (iterator.hasNext()) {
				Entity e = iterator.next();

				if (e.isDispose()) {
					float alpha = e.getAlpha().get() - DISPOSE_SPEED * Gdx.graphics.getRawDeltaTime();
					e.getAlpha().set(alpha);
					if (alpha <= 0) {
						iterator.remove();
						continue;
					}
				}

				e.render(batch);
			}
		}
	}

	public void renderDebugAll(ShapeRenderer renderer) {
		for (HashSet<Entity> entities : entitiesPriorityMap.values()) {
			for (Entity e : entities) {
				e.renderDebug(renderer);
			}
		}
	}
}
