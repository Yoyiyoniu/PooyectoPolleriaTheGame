package me.yoyiyo.pooyecto.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import me.yoyiyo.pooyecto.logic.Player;

public class GameScreen implements Screen {

    private TiledMap map;
    private OrthogonalTiledMapRenderer render;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private Player player;

    @Override
    public void show() {
        map = new TmxMapLoader().load("TiledMap/Kitchen.tmx");
        render = new OrthogonalTiledMapRenderer(map);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        int mapWidth = map.getProperties().get("width", Integer.class);
        int mapHeight = map.getProperties().get("height", Integer.class);
        int tilePixelWidth = map.getProperties().get("tilewidth", Integer.class);
        int tilePixelHeight = map.getProperties().get("tileheight", Integer.class);

        float mapPixelWidth = mapWidth * tilePixelWidth;
        float mapPixelHeight = mapHeight * tilePixelHeight;

        camera.position.set(mapPixelWidth / 2f, mapPixelHeight / 2f, 0);
        camera.zoom = 0.5f;
        camera.update();

        batch = new SpriteBatch();

        // Crear el mundo de Box2D
        world = new World(new Vector2(0, 0), true);

        player = new Player(world, mapPixelWidth / 2f, mapPixelHeight / 2f);
    }

    @Override
    public void render(float delta) {
        // Avanzar la simulación física (IMPORTANTE)
        world.step(delta, 6, 2);

        camera.update();
        render.setView(camera);
        render.render();

        // Actualizar jugador
        player.update();

        // Dibujar jugador
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        map.dispose();
        render.dispose();
        batch.dispose();
        player.dispose();
        world.dispose();
    }
}
