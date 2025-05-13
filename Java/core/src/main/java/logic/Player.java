package logic;

import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.graphics.Graphics;

public class Player {
    private final Texture texture;
    private float x, y;
    private static final int DRAW_WIDTH = 34;
    private static final int DRAW_HEIGHT = 32;
    private boolean facingLeft = false;
    private InteractableItem itemHand;

    public Player(String texturePath, float startX, float startY) {
        texture = new Texture(texturePath);
        this.x = startX;
        this.y = startY;
    }

    public void render(Graphics g) {
        if (facingLeft) {
            g.drawTexture(texture,
                x + DRAW_WIDTH, y,
                -DRAW_WIDTH, DRAW_HEIGHT);
        } else {
            g.drawTexture(texture, x, y, DRAW_WIDTH, DRAW_HEIGHT);
        }

        if (itemHand != null) {
            itemHand.render(g, x, y, facingLeft);
        }
    }

    public void update(float delta) {
        // Lógica adicional si es necesario
    }

    public void dispose() {
        texture.dispose();
        if (itemHand != null) {
            itemHand.dispose();
        }
    }

    public void move(float dx, float dy) {
        if (dx < 0) {
            facingLeft = true;
        } else if (dx > 0) {
            facingLeft = false;
        }

        x += dx;
        y += dy;
    }

    public void takeItem(InteractableItem item) {
        float distance = (float) Math.sqrt(Math.pow(item.getX() - x, 2) + Math.pow(item.getY() - y, 2));
        if (distance < 20) {
            itemHand = item;
            item.setPosition(-100, -100);
        }
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
}