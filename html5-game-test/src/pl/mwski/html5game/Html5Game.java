package pl.mwski.html5game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Html5Game implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;

	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		this.camera = new OrthographicCamera(1, h/w);
		this.batch = new SpriteBatch();

		this.texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		this.texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		TextureRegion region = new TextureRegion(this.texture, 0, 0, 512, 275);

		this.sprite = new Sprite(region);
		this.sprite.setSize(0.9f, 0.9f * this.sprite.getHeight() / this.sprite.getWidth());
		this.sprite.setOrigin(this.sprite.getWidth()/2, this.sprite.getHeight()/2);
		this.sprite.setPosition(-this.sprite.getWidth()/2, -this.sprite.getHeight()/2);
	}

	@Override
	public void dispose() {
		this.batch.dispose();
		this.texture.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		this.batch.setProjectionMatrix(this.camera.combined);
		this.batch.begin();
		this.sprite.draw(this.batch);
		this.batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
