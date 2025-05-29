package me.yoyiyo.pooyecto.logic.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import me.yoyiyo.pooyecto.logic.Player;

public class InteractuableItem {
    private Body body;
    private Texture texture;
    private boolean isPickedUp;
    private boolean isInteractable;
    private String name;
    private Rectangle interactionArea;

    // Nuevas variables para dirección
    private int facingDirection = 0; // 0: derecha, 1: arriba, 2: izquierda, 3: abajo
    private final float PLACEMENT_DISTANCE = 20f;

    // PPM debe coincidir con la del jugador
    private static final float PPM = Player.PPM;

    // Offset vertical para posicionar el objeto sobre la cabeza del jugador
    private static final float HEAD_OFFSET_Y = 18f;
    private static final float SIDE_OFFSET_X = 18f;

    public InteractuableItem(World world, float x, float y, String texturePath, String name) {
        this.texture = new Texture(texturePath);
        this.name = name;
        this.isPickedUp = false;
        this.isInteractable = true;

        // Crear cuerpo físico
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PPM, y / PPM);
        // Reducir velocidad lineal para menos movimiento
        bodyDef.linearDamping = 5.0f; // Aumentar la amortiguación

        body = world.createBody(bodyDef);
        body.setFixedRotation(true);

        // Definir forma y fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(texture.getWidth() / 2f / PPM, texture.getHeight() / 2f / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        // Object config
        fixtureDef.density = 3.0f;
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0.1f;

        body.createFixture(fixtureDef);
        shape.dispose();

        // Área de interacción (un poco más grande que el objeto)
        float interactionRange = 20f;
        interactionArea = new Rectangle(
                x - texture.getWidth()/2f - interactionRange,
                y - texture.getHeight()/2f - interactionRange,
                texture.getWidth() + interactionRange*2,
                texture.getHeight() + interactionRange*2
        );
    }

    public void update(Player player) {
        if (isPickedUp) {
            // Si está recogido, el item sigue al jugador pero no se ve en el mundo físico
            Vector2 playerPos = player.getBody().getPosition();
            body.setTransform(playerPos.x, playerPos.y, 0);
            body.setActive(false); // Desactivar físicas cuando está recogido

            // Actualizar la dirección basada en el movimiento del jugador
            updateFacingDirection(player);
        } else {
            body.setActive(true);

            Vector2 vel = body.getLinearVelocity();
            float maxVelocity = 2.0f;
            if (vel.len() > maxVelocity) {
                vel.nor().scl(maxVelocity);
                body.setLinearVelocity(vel);
            }

            // Actualizar área de interacción
            Vector2 position = body.getPosition();
            float interactionRange = 20f;
            interactionArea.setPosition(
                    position.x * PPM - texture.getWidth()/2f - interactionRange,
                    position.y * PPM - texture.getHeight()/2f - interactionRange
            );
        }
    }

    private void updateFacingDirection(Player player) {
        Vector2 velocity = player.getBody().getLinearVelocity();

        // Solo actualiza dirección si se está moviendo
        if (velocity.len2() > 0.1f) {
            // Determinar dirección predominante
            if (Math.abs(velocity.x) > Math.abs(velocity.y)) {
                // Movimiento horizontal predominante
                facingDirection = velocity.x > 0 ? 0 : 2; // derecha o izquierda
            } else {
                // Movimiento vertical predominante
                facingDirection = velocity.y > 0 ? 1 : 3; // arriba o abajo
            }
        }
    }

    public void render(SpriteBatch batch) {
        Vector2 position = body.getPosition();

        if (!isPickedUp) {
            // Dibuja el objeto en el suelo si no está recogido
            batch.draw(texture,
                    position.x * PPM - texture.getWidth() / 2f,
                    position.y * PPM - texture.getHeight() / 2f);
        } else {
            // Dibujar el objeto según la dirección del jugador
            float offsetX = 0;
            float offsetY = 0;

            switch (facingDirection) {
                case 0: // derecha
                    offsetX = SIDE_OFFSET_X;
                    break;
                case 1: // arriba
                    offsetY = HEAD_OFFSET_Y;
                    break;
                case 2: // izquierda
                    offsetX = -SIDE_OFFSET_X;
                    break;
                case 3: // abajo
                    offsetY = -HEAD_OFFSET_Y/2;
                    break;
            }

            batch.draw(texture,
                    position.x * PPM - texture.getWidth() / 2f + offsetX,
                    position.y * PPM - texture.getHeight() / 2f + offsetY);
        }
    }

    public boolean canInteract(Vector2 playerPosition) {
        return isInteractable && interactionArea.contains(playerPosition.x * PPM, playerPosition.y * PPM);
    }

    public void pickup() {
        if (!isPickedUp && isInteractable) {
            isPickedUp = true;
            // Detener completamente el movimiento al recoger
            body.setLinearVelocity(0, 0);
        }
    }

    public void place(float x, float y, Vector2 playerVelocity) {
        if (isPickedUp) {
            isPickedUp = false;

            // Determinar la posición de colocación según la dirección del jugador
            float placeX = x;
            float placeY = y;

            switch (facingDirection) {
                case 0: // derecha
                    placeX += PLACEMENT_DISTANCE;
                    break;
                case 1: // arriba
                    placeY += PLACEMENT_DISTANCE;
                    break;
                case 2: // izquierda
                    placeX -= PLACEMENT_DISTANCE;
                    break;
                case 3: // abajo
                    placeY -= PLACEMENT_DISTANCE;
                    break;
            }

            body.setTransform(placeX / PPM, placeY / PPM, 0);
            body.setLinearVelocity(0, 0);
        }
    }

    // Sobrecarga del método place para compatibilidad
    public void place(float x, float y) {
        place(x, y, new Vector2(0, 0));
    }

    public boolean isPickedUp() {
        return isPickedUp;
    }

    public String getName() {
        return name;
    }

    public void dispose() {
        texture.dispose();
    }

    public Body getBody() {
        return body;
    }

    public int getFacingDirection() {
        return facingDirection;
    }
}
