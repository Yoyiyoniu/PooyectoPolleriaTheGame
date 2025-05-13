package logic;

import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.graphics.Graphics;

public class Player {
    private final Texture texture;
    private float x, y;
    private static final int DRAW_WIDTH = 34;
    private static final int DRAW_HEIGHT = 32;
    private boolean facingLeft = false;

    public Player(String texturePath, float startX, float startY) {
        texture = new Texture(texturePath);
        this.x = startX;
        this.y = startY;
    }

    public void render(Graphics g) {
        // Automatic flip texture
        if (facingLeft) {
            g.drawTexture(texture,
                x + DRAW_WIDTH, y,
                -DRAW_WIDTH, DRAW_HEIGHT);
        } else {
            g.drawTexture(texture, x, y, DRAW_WIDTH, DRAW_HEIGHT);
        }
    }

    public void update(float delta) {
    }

    public void dispose() {
        texture.dispose();
    }

    public void move(float dx, float dy) {
        // Actualizamos la dirección según el movimiento horizontal
        if (dx < 0) {
            facingLeft = true;
        } else if (dx > 0) {
            facingLeft = false;
        }

        x += dx;
        y += dy;
    }

    public static int getHeight() {
        return DRAW_HEIGHT;
    }

    public static int getWidth() {
        return DRAW_WIDTH;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void moveX(float dx) {
        if (dx < 0) {
            facingLeft = true;
        } else if (dx > 0) {
            facingLeft = false;
        }
        x += dx;
    }

    public void moveY(float dy) {
        y += dy;
    }
}