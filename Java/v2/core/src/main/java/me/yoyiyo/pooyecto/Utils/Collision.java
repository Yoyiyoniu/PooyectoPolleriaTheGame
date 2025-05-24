package me.yoyiyo.pooyecto.Utils;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;

import com.badlogic.gdx.physics.box2d.*;
import me.yoyiyo.pooyecto.logic.Player;

import java.util.ArrayList;

public class Collision {

    private TiledMap map;
    private World world;
    private ArrayList<String> collisionLayers = new ArrayList<>();


    public void setupCollisions(TiledMap map, World world) {
        this.world = world;
        this.map = map;

        // Crear el BodyDef para objetos estáticos
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        // Crear la forma y fixture para las colisiones
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.1f;

        for (var cll : collisionLayers) {
            processCollisionLayer(cll, bodyDef, fixtureDef, 1.0f);
        }

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

    public void setCollisionLayers(String colLayer) {
        collisionLayers.add(colLayer);
    }

    public void setMap(TiledMap map) {
        this.map = map;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public ArrayList<String> getCollisionLayers() {
        return collisionLayers;
    }
    public TiledMap getMap() {
        return map;
    }
    public World getWorld() {
        return world;
    }
}
