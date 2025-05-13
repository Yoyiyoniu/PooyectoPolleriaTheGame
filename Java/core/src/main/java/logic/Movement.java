package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import Ui.utils.Barrier;

import java.util.List;

public class Movement {
    private static final float SPEED = 140f;

    public Movement(Player player, float delta, List<Barrier> barriers) {
        float dx = 0, dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            dx -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            dx += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            dy -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            dy += 1;
        }

        if (dx != 0 && dy != 0) {
            float length = (float) Math.sqrt(dx * dx + dy * dy);
            dx /= length;
            dy /= length;
        }

        // Calculamos la nueva posición
        float newX = player.getX() + dx * SPEED * delta;
        float newY = player.getY() + dy * SPEED * delta;

        // Verificamos colisiones con barreras
        boolean collisionX = false;
        boolean collisionY = false;

        for (Barrier barrier : barriers) {
            if (barrier.collidesWith(newX, player.getY(), Player.getWidth(), Player.getHeight())) {
                collisionX = true;
            }
            if (barrier.collidesWith(player.getX(), newY, Player.getWidth(), Player.getHeight())) {
                collisionY = true;
            }
        }

        // Movemos el jugador solo si no hay colisión
        if (!collisionX) {
            player.moveX(dx * SPEED * delta);
        }
        if (!collisionY) {
            player.moveY(dy * SPEED * delta);
        }
    }
}