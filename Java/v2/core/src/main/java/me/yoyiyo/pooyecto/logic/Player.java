package me.yoyiyo.pooyecto.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player {

    private static final float PPM = 16f;

    private Body body;
    private Texture texture;
    private float speed = 2f; // En metros por segundo

    private boolean flipFace = false;

    public Player(World world, float x, float y) {
        texture = new Texture("player.png");

        // Definir el cuerpo físico
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PPM, y / PPM);

        body = world.createBody(bodyDef);
        body.setFixedRotation(true); // Evita que rote

        // Definir forma y fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8 / PPM, 8 / PPM); // 16x16 sprite en píxeles

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.4f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void update() {
        float dx = 0, dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            dx -= 1;
            if(!flipFace) flipFace = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            dx += 1;
            if(flipFace) flipFace = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            dy += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            dy -= 1;
        }

        // Normalizar si se mueve en diagonal
        if (dx != 0 && dy != 0) {
            float length = (float) Math.sqrt(dx * dx + dy * dy);
            dx /= length;
            dy /= length;
        }

        // NO multiplicar por delta - Box2D ya maneja el tiempo
        body.setLinearVelocity(dx * speed, dy * speed);
    }

    public void render(SpriteBatch batch) {
        Vector2 position = body.getPosition();
        batch.draw(texture,position.x * PPM - 8, position.y * PPM - 8,
            8, 8, 16, 16,
            1,1 , 0, 0,
            0, 16, 17, flipFace, false);

    }

    public void dispose() {
        texture.dispose();
    }

    public Body getBody() {
        return body;
    }

}
