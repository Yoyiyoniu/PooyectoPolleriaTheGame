package logic.utils;

import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.graphics.Graphics;

public class Barrier {
    protected Texture texture;
    protected float x, y;
    protected int width, height;
    protected boolean isVisible;
    protected boolean onlyBlockBorders;

    public Barrier(String texturePath, float x, float y, int width, int height, boolean isVisible, boolean onlyBlockBorders) {
        if (isVisible && texturePath != null) {
            this.texture = new Texture(texturePath);
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isVisible = isVisible;
        this.onlyBlockBorders = onlyBlockBorders;
    }

    public Barrier(String texturePath, float x, float y, int width, int height, boolean isVisible) {
        this(texturePath, x, y, width, height, isVisible, false);
    }

    public void render(Graphics g) {
        if (isVisible && texture != null) {
            g.drawTexture(texture, x, y, width, height);
        }
    }

    public boolean collidesWith(float playerX, float playerY, int playerWidth, int playerHeight) {
        if (!onlyBlockBorders) {
            // Comportamiento normal - bloquea completamente
            return (playerX < x + width &&
                    playerX + playerWidth > x &&
                    playerY < y + height &&
                    playerY + playerHeight > y);
        } else {
            // Solo bloquea en los bordes
            boolean touchingLeftBorder = (playerX <= x && playerX + playerWidth > x);
            boolean touchingRightBorder = (playerX < x + width && playerX + playerWidth >= x + width);
            boolean touchingTopBorder = (playerY <= y && playerY + playerHeight > y);
            boolean touchingBottomBorder = (playerY < y + height && playerY + playerHeight >= y + height);

            boolean withinVerticalRange = (playerY < y + height && playerY + playerHeight > y);
            boolean withinHorizontalRange = (playerX < x + width && playerX + playerWidth > x);

            return (touchingLeftBorder && withinVerticalRange) ||
                   (touchingRightBorder && withinVerticalRange) ||
                   (touchingTopBorder && withinHorizontalRange) ||
                   (touchingBottomBorder && withinHorizontalRange);
        }
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isOnlyBlockBorders() {
        return onlyBlockBorders;
    }
}