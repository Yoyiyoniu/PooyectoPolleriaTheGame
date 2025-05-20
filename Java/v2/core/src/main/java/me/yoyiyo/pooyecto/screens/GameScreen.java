package me.yoyiyo.pooyecto.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
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

        setupCollisions();

        player = new Player(world, mapPixelWidth / 2f, mapPixelHeight / 2f);

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

    private void setupCollisions() {
        // Crear el BodyDef para objetos estáticos
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        // Crear la forma y fixture para las colisiones
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.1f;

        // Procesamos las capas de límites
        processCollisionLayer("limit", bodyDef, fixtureDef, 1.0f);
        processCollisionLayer("limit2", bodyDef, fixtureDef, 1.0f);
        processCollisionLayer("Tables", bodyDef, fixtureDef, 0.7f); // Reducimos la zona de colisión para mesas
    }

    private void processCollisionLayer(String layerName, BodyDef bodyDef, FixtureDef fixtureDef, float collisionScale) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
        if (layer == null) {
            System.out.println("Capa " + layerName + " no encontrada");
            return;
        }

        int tileWidth = map.getProperties().get("tilewidth", Integer.class);
        int tileHeight = map.getProperties().get("tileheight", Integer.class);

        // Recorremos todas las celdas de la capa
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                // Solo crear cuerpos para celdas que tienen contenido
                if (cell != null && cell.getTile() != null) {
                    // Crear cuerpo para esta celda
                    bodyDef.position.set(
                        (x * tileWidth + tileWidth / 2f) / Player.PPM,
                        (y * tileHeight + tileHeight / 2f) / Player.PPM
                    );

                    Body body = world.createBody(bodyDef);

                    // Ajustamos el tamaño del colisionador según el tipo de objeto
                    float halfWidth = (tileWidth / 2f) * collisionScale / Player.PPM;
                    float halfHeight = (tileHeight / 2f) * collisionScale / Player.PPM;

                    // Crear forma rectangular para la celda
                    PolygonShape shape = new PolygonShape();
                    shape.setAsBox(halfWidth, halfHeight);

                    fixtureDef.shape = shape;
                    body.createFixture(fixtureDef);

                    shape.dispose();
                }
            }
        }
    }

}
