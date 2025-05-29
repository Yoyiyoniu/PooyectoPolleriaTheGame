package me.yoyiyo.pooyecto.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import me.yoyiyo.pooyecto.enums.Paths;
import me.yoyiyo.pooyecto.logic.item.InteractuableItem;

public class Player {

    public static final float PPM = 16f;

    private Body body;
    private Texture texture;
    private float speed = 2.5f; // En metros por segundo

    private InteractuableItem carriedItem;

    // Estado de izquierda/derecha
    private boolean flipFace = false;

    // Propiedades del dash :]
    private boolean canDash = true;
    private boolean isDashing = false;

    private float dashTimer = 0;
    private float cooldownTimer = 0;

    private float dashPower = 4.5f;
    private float dashDuration = 0.2f;
    private float dashCooldown = 0.2f;



    // Shader para el efecto de dash
    private ShaderProgram dashShader;
    private ShaderProgram defaultShader;
    private float elapsedTime = 0f;

    public Player(World world, float x, float y) {
        texture = new Texture(Paths.PLAYER.getPath());

        // Definir el cuerpo físico
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PPM, y / PPM);

        body = world.createBody(bodyDef);
        body.setFixedRotation(true); // Evita que rote

        // Definir forma y fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(7 / PPM, 7 / PPM); // 16x16 sprite en píxeles

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.4f;

        body.createFixture(fixtureDef);
        shape.dispose();

        // Configurar los shaders
        ShaderProgram.pedantic = false;
        String vertexShader = Gdx.files.internal("shader/default.vert").readString();
        String fragmentShader = Gdx.files.internal("shader/player.frag").readString();
        dashShader = new ShaderProgram(vertexShader, fragmentShader);

        if (!dashShader.isCompiled()) {
            Gdx.app.error("Shader", "Error compiling shader: " + dashShader.getLog());
            // Usar shader por defecto en caso de error
            dashShader = defaultShader;
        }

        if (!Gdx.files.internal("shader/default.vert").exists()) {
            Gdx.app.error("Shader", "El archivo de shader vertex no existe");
        }
        if (!Gdx.files.internal("shader/player.frag").exists()) {
            Gdx.app.error("Shader", "El archivo de shader fragment no existe");
        }

        defaultShader = SpriteBatch.createDefaultShader();
        carriedItem = null;
    }

    public void update() {
        float dx = 0, dy = 0;
        float deltaTime = Gdx.graphics.getDeltaTime();
        elapsedTime += deltaTime;

        // Actualizar temporizadores
        if (isDashing) {
            dashTimer -= deltaTime;


            if (dashTimer <= 0) {
                isDashing = false;
            }
        } else if (!canDash) {
            cooldownTimer -= deltaTime;
            if (cooldownTimer <= 0) {
                canDash = true;
            }
        }

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

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && canDash && (dx != 0 || dy != 0)) {
            isDashing = true;
            canDash = false;
            dashTimer = dashDuration;
            cooldownTimer = dashCooldown;
        }

        if (dx != 0 && dy != 0) {
            float length = (float) Math.sqrt(dx * dx + dy * dy);
            dx /= length;
            dy /= length;
        }

        float currentSpeed = isDashing ? speed * dashPower : speed;
        body.setLinearVelocity(dx * currentSpeed, dy * currentSpeed);
    }

    public void render(SpriteBatch batch) {
        Vector2 position = body.getPosition();
        int width = texture.getWidth();
        int height = texture.getHeight();

        ShaderProgram currentShader = batch.getShader();

        if (isDashing) {
            batch.setShader(dashShader);
            dashShader.setUniformf("u_time", elapsedTime);

            Vector2 velocity = body.getLinearVelocity();
            dashShader.setUniformf("u_direction", velocity.x, velocity.y);
        }

        float originX = width / 2f;
        float originY = height / 2f;

        // Dibujar con rotación y escala si está haciendo dash
        batch.draw(texture,
                position.x * PPM - originX, position.y * PPM - originY,
                originX, originY,
                width, height,
                1, 1,
                0,
                0, 0, width, height,
                flipFace, false);

        // Restaurar el shader original
        batch.setShader(currentShader);
    }

    public void dispose() {
        texture.dispose();
        dashShader.dispose();
    }

    public Body getBody() {
        return body;
    }

    public void interact(Array<InteractuableItem> items){
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            if (carriedItem == null) {
                // Intentar recoger un objeto
                for (InteractuableItem item : items) {
                    if (item.canInteract(body.getPosition()) && !item.isPickedUp()) {
                        item.pickup();
                        carriedItem = item;
                        break;
                    }
                }
            } else {
                // Soltar el objeto que lleva en la dirección que mira el jugador
                carriedItem.place(body.getPosition().x * PPM, body.getPosition().y * PPM, body.getLinearVelocity());
                carriedItem = null;
            }
        }
    }

}
