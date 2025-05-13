package logic.utils;

import com.badlogic.gdx.Gdx;
import logic.Floor;
import logic.Player;

import java.util.List;

public class Scenario {
    private static final int TEXTURE_WIDTH = 17;
    private static final int TEXTURE_HEIGHT = 16;

    private float offsetX;
    private float offsetY;
    private final float totalWidth;
    private final float totalHeight;

    public Scenario(String floorPath, List<Floor> grounds, int width, int height) {
        this(floorPath, grounds, width, height, false, null);
    }

    public Scenario(String floorPath, List<Floor> grounds, int width, int height, boolean blockWalls, List<Barrier> barriers) {
        this.totalWidth = getTotalWidth(width);
        this.totalHeight = getTotalHeight(height);

        calculateOffsets();
        createBasicScenario(grounds, floorPath, offsetX, offsetY, width, height);

        if (blockWalls && barriers != null) {
            createBoundaryBarriers(barriers);
        }
    }

    private void calculateOffsets() {
        offsetX = (Gdx.graphics.getWidth() - totalWidth) / 2f;
        offsetY = (Gdx.graphics.getHeight() - totalHeight) / 2f;
    }

    private void createBoundaryBarriers(List<Barrier> barriers) {
        int barrierThickness = 10;

        // left barrier
        barriers.add(new Barrier(null,
                offsetX - barrierThickness,
                offsetY - barrierThickness,
                barrierThickness,
                (int)totalHeight + (2 * barrierThickness),
                false,
                true));

        // right barrier
        barriers.add(new Barrier(null,
                offsetX + totalWidth,
                offsetY - barrierThickness,
                barrierThickness,
                (int)totalHeight + (2 * barrierThickness),
                false,
                true));

        // top barrier
        barriers.add(new Barrier(null,
                offsetX - barrierThickness,
                offsetY - barrierThickness,
                (int)totalWidth + (2 * barrierThickness),
                barrierThickness,
                false,
                true));

        // bottom barrier
        barriers.add(new Barrier(null,
                offsetX - barrierThickness,
                offsetY + totalHeight,
                (int)totalWidth + (2 * barrierThickness),
                barrierThickness,
                false,
                true));
    }

    public static void createBasicScenario(List<Floor> grounds, String groundPath, float offsetX, float offsetY, int width, int height) {
        createRectangleFloor(grounds, groundPath, offsetX, offsetY, width, height);
    }

    public static void createPlatform(List<Floor> grounds, String groundPath, float startX, float startY, int width) {
        createHorizontalFloor(grounds, groundPath, startX, startY, width);
    }

    public static void createColumn(List<Floor> grounds, String groundPath, float startX, float startY, int height) {
        createVerticalFloor(grounds, groundPath, startX, startY, height);
    }

    private static void createHorizontalFloor(List<Floor> grounds, String texturePath, float startX, float startY, int count) {
        for (int i = 0; i < count; i++) {
            grounds.add(new Floor(texturePath, startX + (i * TEXTURE_WIDTH), startY));
        }
    }

    private static void createVerticalFloor(List<Floor> grounds, String texturePath, float startX, float startY, int count) {
        for (int i = 0; i < count; i++) {
            grounds.add(new Floor(texturePath, startX, startY + (i * TEXTURE_HEIGHT)));
        }
    }

    private static void createRectangleFloor(List<Floor> grounds, String texturePath, float startX, float startY, int width, int height) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grounds.add(new Floor(texturePath,
                        startX + (col * TEXTURE_WIDTH),
                        startY + (row * TEXTURE_HEIGHT)));
            }
        }
    }

    public static float getTotalWidth(int width) {
        return width * TEXTURE_WIDTH;
    }

    public static float getTotalHeight(int height) {
        return height * TEXTURE_HEIGHT;
    }

    public void centerPositionPlayer(Player player) {
        float playerX = offsetX + (totalWidth / 2f) - (Player.getWidth() / 2f);
        float playerY = offsetY + (totalHeight / 2f) - (Player.getHeight() / 2f);
        player.move(playerX, playerY);
    }
}