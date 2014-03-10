package pl.mwski.html5game;

import pl.mawe.games.commons.GameGlobalState;
import pl.mawe.games.commons.GameState;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.TimeUtils;

public class MyGdxGame extends ApplicationAdapter {

	private final Logger log = new Logger("MyGDXGame", Application.LOG_DEBUG);

	//	ImmediateModeRenderer10 ray;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture baloon;
	private Texture cloud;
	private Texture coin;
	private Texture bomb;
	private Texture heart;
	private final Rectangle sprite = new Rectangle();
	private Vector3 touchPos;
	private final Array<Rectangle> clouds = new Array<Rectangle>();
	private final Array<Rectangle> coins = new Array<Rectangle>();
	private final Array<Rectangle> bombs = new Array<Rectangle>();
	private long lastCloudTime;
	private long lastCoinTime;
	private long lastBombTime;

	private Stage welcomeStage;
	private final Skin welcomeSkin = new Skin();

	private Stage hudStage;
	private final Skin hudSkin = new Skin();

	final GameGlobalState gameState = GameGlobalState.INSTANCE;

	private boolean paused = false;

	public enum MyGdxGameState implements GameState {
		WELCOME_SCREEN, LEVEL, BONUS_LEVEL, GAME_OVER;
	}

	public MyGdxGame() {

	}

	@Override
	public void create() {		
		//		log.setLevel(Level.ALL);
		//		ray = new ImmediateModeRenderer10();

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		hudStage = new Stage();
		welcomeStage = new Stage();

		final Table welcome = new Table();
		welcome.setFillParent(true);
		welcomeStage.addActor(welcome);

		//		welcomeSkin.
		welcomeSkin.addRegions(new TextureAtlas("data/startBtn.pack"));
		final Button startButton = new Button(welcomeSkin.getDrawable("startBtn"), welcomeSkin.getDrawable("startBtn"),
				welcomeSkin.getDrawable("startBtn"));
		startButton.setWidth(256);
		startButton.setHeight(66);
		startButton.setOrigin(0, 0);
		startButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				gameState.setState(MyGdxGameState.LEVEL);
				return true;
			}
		});
		Gdx.input.setInputProcessor(welcomeStage);

		welcome.center();
		welcome.add(startButton);

		final Table hud = new Table();
		hud.setFillParent(true);
		hudStage.addActor(hud);
		hud.debug();

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = new BitmapFont(Gdx.files.internal("data/fonts/KomicaAxis-32.fnt"), 
				Gdx.files.internal("data/fonts/KomicaAxis-32_0.png"), false);
		labelStyle.fontColor = com.badlogic.gdx.graphics.Color.WHITE;
		hudSkin.add("default", labelStyle);

		final Label hudScore = new Label(String.format("Score: %08d", 0), hudSkin); 
		hud.left().top();
		hud.add(hudScore).padLeft(5);

		final Label hudLives = new Label(String.format("Lives:", 0), hudSkin);
		heart = new Texture(Gdx.files.internal("data/heart.png"));
		final Image[] hudHearts = new Image[] { new Image(heart), new Image(heart), new Image(heart), new Image(heart), new Image(heart) };
		hud.add(hudLives).padLeft(350);
		hud.add(hudLives).padRight(5);
		for (Image image : hudHearts) {
			image.setVisible(true);
			hud.add(image).padRight(5);
		}


		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		batch = new SpriteBatch();

		baloon = new Texture(Gdx.files.internal("data/ballooning.png"));
		baloon.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		cloud = new Texture(Gdx.files.internal("data/cloud.png"));
		cloud.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		coin = new Texture(Gdx.files.internal("data/super_mario_coin.png"));
		coin.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bomb = new Texture(Gdx.files.internal("data/clanbomber.png"));
		bomb.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		sprite.width = baloon.getWidth();
		sprite.height = baloon.getHeight();
		sprite.setX(w / 2);
		sprite.setY(h / 2);

		gameState.setState(MyGdxGameState.WELCOME_SCREEN);

	}

	@Override
	public void dispose() {
		log.info(getClass().getName() + "dispose()" + "*****Disposing...*****");
		batch.dispose();
		baloon.dispose();
		cloud.dispose();
		coin.dispose();
		bomb.dispose();
		heart.dispose();
		welcomeStage.dispose();
		welcomeSkin.dispose();
		hudStage.dispose();
		hudSkin.dispose();
		log.info(getClass().getName() + "dispose()" + "*****Disposed.*****");
	}

	@Override
	public void render() {		
		if (paused) {
			return;
		}

		//		ray.begin(GL10.GL_QUADRATIC_ATTENUATION);
		//		ray.color(1.0f, 0.0f, 0.0f, 1.0f);
		//		ray.vertex(-1.0f ,-1.0f, 1.0f);
		//		ray.vertex(1.0f, -1.0f, 1.0f);
		//		ray.color(0.0f ,0.0f ,1.0f, 1.0f);
		//		ray.vertex(1.0f, 1.0f, 1.0f);
		//		ray.vertex(-1.0f, 1.0f, 1.0f);
		//		ray.end();

		switch ((MyGdxGameState)gameState.getState()) {
		case WELCOME_SCREEN:
			renderWelcome();
			break;
		case LEVEL:
			renderLevel();
			break;
		case BONUS_LEVEL:
			renderBonusLevel();
			break;
		case GAME_OVER:
			renderGameOver();
			break;
		}

	}

	private void renderWelcome() {
		Gdx.gl.glClearColor(0.5f, 0.75f, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		welcomeStage.draw();
	}

	private void renderLevel() {
		Gdx.gl.glClearColor(0.5f, 0.75f, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (TimeUtils.nanoTime() - lastCloudTime > 550000000 || lastCloudTime == 0) {
			spawnCloud();
		}

		if (TimeUtils.nanoTime() - lastCoinTime > 1000000000 || lastCoinTime == 0) {
			spawnCoin();
		}

		if (TimeUtils.nanoTime() - lastBombTime > 1200000000 || lastBombTime == 0) {
			spawnBomb();
		}

		camera.update();
		batch.begin();
		batch.draw(baloon, sprite.getX(), sprite.getY());
		for (Rectangle rec : coins) {
			batch.draw(coin, rec.getX(), rec.getY());
		}
		for (Rectangle rec : bombs) {
			batch.draw(bomb, rec.getX(), rec.getY());
		}
		for (Rectangle rec : clouds) {
			batch.draw(cloud, rec.getX(), rec.getY());
		}
		batch.end();

		if(Gdx.input.isTouched()) {
			touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			sprite.x = touchPos.x - sprite.width / 2;
			sprite.y = touchPos.y - sprite.height / 2;
		}

		if(Gdx.input.isKeyPressed(Keys.LEFT)) { 
			sprite.x -= 200 * Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) { 
			sprite.x += 200 * Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN)) { 
			sprite.y -= 200 * Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Keys.UP)) { 
			sprite.y += 200 * Gdx.graphics.getDeltaTime();
		}

		for (Rectangle rec : clouds) {
			rec.x -= 200 * Gdx.graphics.getDeltaTime();
			if(rec.x + 128 < 0) { 
				clouds.removeValue(rec, false);
			}
		}

		for (Rectangle rec : coins) {
			rec.x -= 400 * Gdx.graphics.getDeltaTime();
			if(rec.x + 48 < 0) { 
				coins.removeValue(rec, false);
			}
		}

		for (Rectangle rec : bombs) {
			rec.x -= 400 * Gdx.graphics.getDeltaTime();
			if(rec.x + 48 < 0) { 
				bombs.removeValue(rec, false);
			}
		}

		hudStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1/30f));
		hudStage.draw();
		//		Table.drawDebug(hudStage);
	}

	private void renderBonusLevel() {

	}

	private void renderGameOver() {

	}

	@Override
	public void resize(int width, int height) {
		log.info(getClass().getName() + "resize()" + "New size: w:" + width + " h:" + height);
	}

	@Override
	public void pause() {
		log.info(getClass().getName() + "pause()" + "*****PAUSED!*****");
	}

	@Override
	public void resume() {
		log.info(getClass().getName() + "resume()" + "*****RESUMED...*****");
	}

	private void spawnCloud() {
		Rectangle cloudRec = new Rectangle();
		cloudRec.y = MathUtils.random(50, 600);
		cloudRec.x = 800;
		cloudRec.width = 128;
		cloudRec.height = 128;
		clouds.add(cloudRec);
		lastCloudTime = TimeUtils.nanoTime();
	}

	private void spawnCoin() {
		Rectangle coinRec = new Rectangle();
		coinRec.y = MathUtils.random(75, 550);
		coinRec.x = 800;
		coinRec.width = 48;
		coinRec.height = 48;
		coins.add(coinRec);
		lastCoinTime = TimeUtils.nanoTime();
	}

	private void spawnBomb() {
		Rectangle bombRec = new Rectangle();
		bombRec.y = MathUtils.random(75, 550);
		bombRec.x = 800;
		bombRec.width = 48;
		bombRec.height = 48;
		bombs.add(bombRec);
		lastBombTime = TimeUtils.nanoTime();
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

}
