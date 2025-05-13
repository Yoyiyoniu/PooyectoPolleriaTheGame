package logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.graphics.Graphics;

public class InteractableItem extends MapItem {
    private final Texture texture;
    private float x, y; // Posición del ítem
    private boolean debug; // Modo de depuración

    public InteractableItem(String displayName, String itemTexture, float[] hitBox) {
        super(displayName, itemTexture, hitBox,16, 16);
        this.texture = new Texture(itemTexture);
        this.debug = false; // Por defecto, debug está desactivado
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
        updateHitBox();
    }

    public void render(Graphics g) {
        g.drawTexture(texture, x, y, this.getWidth(), this.getHeight());

        // Dibujar la hitbox si debug está activado
        if (debug) {
            g.setColor(Color.RED);
            g.drawRect(x, y, this.getWidth(), this.getHeight());
            g.setColor(Color.WHITE); // Restaurar el color original
        }
    }

    public void setSize(int size) {
        this.setWidth(texture.getWidth() + size);
        this.setHeight(texture.getHeight() + size);
        updateHitBox(); // Actualizar la hitbox al cambiar el tamaño
    }

    private void updateHitBox() {
        this.setHitBox(new float[]{
            x, y, // Esquina superior izquierda
            x + this.getWidth(), y, // Esquina superior derecha
            x + this.getWidth(), y + this.getHeight(), // Esquina inferior derecha
            x, y + this.getHeight() // Esquina inferior izquierda
        });
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isDebug() {
        return debug;
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