package me.yoyiyo.pooyecto.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import me.yoyiyo.pooyecto.Utils.Collision;
import me.yoyiyo.pooyecto.enums.Paths;
import me.yoyiyo.pooyecto.logic.Player;
import me.yoyiyo.pooyecto.logic.item.Chicken;
import me.yoyiyo.pooyecto.logic.item.InteractuableItem;

public class GameScreen implements Screen {

    private TiledMap map;
    private OrthogonalTiledMapRenderer render;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;

    private Player player;
    private Array<InteractuableItem> interactableItems;

    @Override
    public void show() {

        map = new TmxMapLoader().load(Paths.MAP.getPath());
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

        var col = new Collision();
        col.setCollisionLayers("limit");
        col.setCollisionLayers("limit2");
        col.setCollisionLayers("Tables");

        col.setupCollisions(map, world);

        player = new Player(world, mapPixelWidth / 2f, mapPixelHeight / 2f);

        interactableItems = new Array<>();
        interactableItems.add(new Chicken(world, 100, 100, "Pollo"));

    }

    @Override
    public void render(float delta) {
        // Avanzar la simulación física
        world.step(delta, 6, 2);

        camera.update();
        render.setView(camera);
        render.render();

        // Actualizar jugador
        player.update();

        for (InteractuableItem item : interactableItems) {
            item.update(player);
        }
        player.interact(interactableItems);


        // Dibujar jugador
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (InteractuableItem item : interactableItems) {
            item.render(batch);
        }

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
        for (InteractuableItem item : interactableItems) {
            item.dispose();
        }
        player.dispose();
        world.dispose();
    }

}
