package logic;

import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.graphics.Graphics;

public class InteractableItem extends MapItem {
    private final Texture texture;
    private float x, y; // Declarar las variables para la posición del ítem

    public InteractableItem(String displayName, String itemTexture) {
        super(displayName, itemTexture, 16, 16);
        this.texture = new Texture(itemTexture);
    }

    public void render(Graphics g, float playerX, float playerY, boolean facingLeft) {
        if (facingLeft) {
            g.drawTexture(texture,
                playerX + Player.getWidth(), playerY,
                -Player.getWidth() / 2, Player.getHeight() / 2);
        } else {
            g.drawTexture(texture,
                playerX, playerY,
                Player.getWidth() / 2, Player.getHeight() / 2);
        }
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void render(Graphics g) {
        g.drawTexture(texture, x, y, Player.getWidth() / 2, Player.getHeight() / 2);
    }

    public void dispose() {
        texture.dispose();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}