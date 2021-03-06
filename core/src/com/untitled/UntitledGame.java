package com.untitled;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.untitled.assets.AssassinAnimationName;
import com.untitled.assets.Assets;
import com.untitled.assets.Boss1AnimationName;
import com.untitled.assets.FontName;
import com.untitled.assets.MusicName;
import com.untitled.assets.RockAnimationName;
import com.untitled.assets.ShurikenAnimationName;
import com.untitled.assets.TankAnimationName;
import com.untitled.assets.TextureName;
import com.untitled.highscores.Highscores;
import com.untitled.screens.ScreenName;

import java.util.Locale;

/**
 * LibGDX {@link Game} object, created when the program starts.
 * <p>
 * Responsible for initializing Assets, Highscores, SpriteBatch and other instances which are
 * meant to be passed by reference to Screens.
 * <p>
 * Also contains some constant definitions and debug flags for the game.
 */
public abstract class UntitledGame extends Game {
	public static final String GAME_VERSION = "BETA 1.5";
	public static final String HIGHSCORE_VERSION = "BETA 1.5";

	// Camera Size
	public static final int CAMERA_WIDTH = 640;
	public static final int CAMERA_HEIGHT = 360;

	// Window Size
	public static final int WINDOW_WIDTH = CAMERA_WIDTH * 2;
	public static final int WINDOW_HEIGHT = CAMERA_HEIGHT * 2;

	// Button Size
	public static final float BUTTON_W = 85f;
	public static final float BUTTON_H = 25f;

	/* Debug Flags */
	// Set to view hitboxes
	public static final boolean DEBUG_HITBOXES = false;
	// Set to disable boss AI & enable controlling the boss
	public static final boolean DEBUG_BOSS_AI = false;
	// Set to force all LivingEntities to 1 health
	public static final boolean DEBUG_ONE_HEALTH = false;
	// Setting any debug flag will disable highscores.
	public static final boolean DEBUG = DEBUG_HITBOXES || DEBUG_BOSS_AI || DEBUG_ONE_HEALTH;

	public static final String SETTINGS_NAME = "name";
	public static final String SETTINGS_VSYNC = "vsync";
	public static final boolean SETTINGS_VSYNC_DEFAULT = true;
	public static final String SETTINGS_FULLSCREEN = "fullscreen";
	public static final boolean SETTINGS_FULLSCREEN_DEFAULT = false;
	public static final String SETTINGS_MUSIC_VOLUME = "music volume";
	public static final int SETTINGS_MUSIC_VOLUME_DEFAULT = 30;
	public static final String SETTINGS_SOUND = "sound volume";
	public static final int SETTINGS_SOUND_DEFAULT = 50;
	private static final String PREFERENCES_SETTINGS = "settings";

	private Assets assets;
	private Highscores highscores;
	private InputMultiplexer inputMultiplexer;
	private Preferences settings;
	private SpriteBatch batch;
	private ShapeRenderer renderer;

	private OrthographicCamera camera;
	private FitViewport viewport;

	@Override
	public void create() {
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
		renderer.setColor(Color.GOLD);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);
		viewport = new FitViewport(CAMERA_WIDTH, CAMERA_HEIGHT, camera);

		assets = new Assets();
		highscores = new Highscores("???");
		inputMultiplexer = new InputMultiplexer();
		settings = Gdx.app.getPreferences(PREFERENCES_SETTINGS);

		assets.loadTexture(TextureName.MENU_BACKGROUND);
		assets.loadTexture(TextureName.GAME_BACKGROUND);
		assets.loadTexture(TextureName.GAME_OVERLAY);

		assets.loadTexture(TextureName.DEBUFF_STUNNED);
		assets.loadTexture(TextureName.DEBUFF_WEAK_SPOT);

		assets.loadTexture(TextureName.BUTTON_NORMAL);
		assets.loadTexture(TextureName.BUTTON_HOVER);
		assets.loadTexture(TextureName.BUTTON_MENU_HOVER);

		assets.loadTexture(TextureName.HIGHSCORES_TITLE);
		assets.loadTexture(TextureName.HIGHSCORES_ODD);
		assets.loadTexture(TextureName.HIGHSCORES_EVEN);

		assets.loadTexture(TextureName.INFO_BAR_BACKGROUND);
		assets.loadTexture(TextureName.HEALTH_BAR_TANK);
		assets.loadTexture(TextureName.HEALTH_BAR_ASSASSIN);
		assets.loadTexture(TextureName.STACK_BAR_ASSASSIN);
		assets.loadTexture(TextureName.HEALTH_BAR_BOSS);

		assets.loadFont(FontName.MINECRAFT_8);
		assets.loadFont(FontName.MINECRAFT_16);
		assets.loadFont(FontName.MINECRAFT_24);
		assets.loadFont(FontName.MINECRAFT_32);

		assets.loadMusic(MusicName.MAIN_MENU);
		assets.loadMusic(MusicName.BOSS);

		assets.loadTankAnimation(TankAnimationName.STANDING);
		assets.loadTankAnimation(TankAnimationName.WALKING);
		assets.loadTankAnimation(TankAnimationName.BLOCK);
		assets.loadTankAnimation(TankAnimationName.HAMMER_SWING);
		assets.loadTankAnimation(TankAnimationName.FORTRESS);
		assets.loadTankAnimation(TankAnimationName.FORTRESS_STANDING);
		assets.loadTankAnimation(TankAnimationName.FORTRESS_WALKING);
		assets.loadTankAnimation(TankAnimationName.FORTRESS_BLOCK);
		assets.loadTankAnimation(TankAnimationName.FORTRESS_IMPALE);

		assets.loadAssassinAnimation(AssassinAnimationName.STANDING);
		assets.loadAssassinAnimation(AssassinAnimationName.WALKING);
		assets.loadAssassinAnimation(AssassinAnimationName.DASH);
		assets.loadAssassinAnimation(AssassinAnimationName.SHURIKEN_THROW);
		assets.loadAssassinAnimation(AssassinAnimationName.CLEANSE);

		assets.loadBoss1Animation(Boss1AnimationName.STANDING);
		assets.loadBoss1Animation(Boss1AnimationName.GROUND_SMASH);
		assets.loadBoss1Animation(Boss1AnimationName.EARTHQUAKE);
		assets.loadBoss1Animation(Boss1AnimationName.ROLL);

		assets.loadShurikenAnimation(ShurikenAnimationName.FLYING);
		assets.loadRockAnimation(RockAnimationName.ERUPT);

		loadAssets(assets);
		assets.load();

		assets.getTexture(TextureName.MENU_BACKGROUND).setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.input.setInputProcessor(inputMultiplexer); // set processor for input.

		createAbstract();
	}

	/**
	 * Load assets in this method.
	 *
	 * @param A Untitled {@link Assets}
	 */
	protected abstract void loadAssets(Assets A);

	/**
	 * Libgdx game create() logic for child classes.
	 */
	protected abstract void createAbstract();

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
		renderer.dispose();
	}

	public void setScreen(ScreenName screen) {
		inputMultiplexer.clear();
		setScreenAbstract(screen);
	}

	protected abstract void setScreenAbstract(ScreenName screen);

	/**
	 * @param level Level received from {@link Highscores} database
	 * @return Formatted level to 2 decimal places.
	 */
	public static String formatLevel(int level) {
		return String.format(Locale.US, "%.2f", level / 100f);
	}

	/**
	 * @param time Time received from {@link Highscores} in seconds.
	 * @return Formatted time to HH:MM:SS.
	 */
	public static String formatTime(int time) {
		int s = time;
		int m = s / 60;
		int h = m / 24;

		s %= 60;
		m %= 60;
		if (h > 99) {
			return "OVER 9000!";
		} else {
			return String.format(Locale.US, "%02d:%02d:%02d", h, m, s);
		}
	}

	/* SETTERS */
	public void setName(String name) {
		highscores = new Highscores(name);
	}

	/* GETTERS */
	public Assets getAssets() {
		return this.assets;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public Highscores getHighscores() {
		return highscores;
	}

	public InputMultiplexer getInputMultiplexer() {
		return this.inputMultiplexer;
	}

	public Preferences getSettings() {
		return settings;
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public ShapeRenderer getShapeRenderer() {
		return renderer;
	}

	public FitViewport getViewport() {
		return viewport;
	}
}
