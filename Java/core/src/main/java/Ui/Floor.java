package Ui;

import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.graphics.Graphics;

import java.util.List;

public class Floor {
    private final Texture texture;
    private final float x, y;
    private static final int TEXTURE_WIDTH = 17;
    private static final int TEXTURE_HEIGHT = 16;

    public Floor(String texturePath, float x, float y) {
        this.texture = new Texture(texturePath);
        this.x = x;
        this.y = y;
    }

    public void render(Graphics g) {
        g.drawTexture(texture, x, y);
    }

    public static void createHorizontalFloor(List<Floor> grounds, String texturePath, float startX, float startY, int count) {
        for (int i = 0; i < count; i++) {
            grounds.add(new Floor(texturePath, startX + (i * TEXTURE_WIDTH), startY));
        }
    }

    public static void createFloor(List<Floor> grounds, String texturePath, float startX, float startY, int count) {
        createHorizontalFloor(grounds, texturePath, startX, startY, count);
    }

    public static void createVerticalFloor(List<Floor> grounds, String texturePath, float startX, float startY, int count) {
        for (int i = 0; i < count; i++) {
            grounds.add(new Floor(texturePath, startX, startY + (i * TEXTURE_HEIGHT)));
        }
    }

    public static void createSquareFloor(List<Floor> grounds, String texturePath, float startX, float startY, int size) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                grounds.add(new Floor(texturePath,
                                     startX + (col * TEXTURE_WIDTH),
                                     startY + (row * TEXTURE_HEIGHT)));
            }
        }
    }

    public static void createRectangleFloor(List<Floor> grounds, String texturePath, float startX, float startY, int width, int height) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grounds.add(new Floor(texturePath,
                                     startX + (col * TEXTURE_WIDTH),
                                     startY + (row * TEXTURE_HEIGHT)));
            }
        }
    }

    public static float getTotalWidth(int floorCount) {
        return floorCount * TEXTURE_WIDTH;
    }

    public static float getTotalHeight() {
        return TEXTURE_HEIGHT;
    }

    public static float getTotalHeight(int floorCount) {
        return floorCount * TEXTURE_HEIGHT;
    }

    public void dispose() {
        texture.dispose();
    }

    public Texture getTexture() {
        return texture;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}