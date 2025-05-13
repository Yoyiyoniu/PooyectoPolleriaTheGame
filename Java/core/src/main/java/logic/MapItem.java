package logic;

public class MapItem {
    private final String displayName;
    private final String itemTexture;

    private float[] hitBox;

    private int width;
    private int height;

    public MapItem(String displayName, String itemTexture, float[] hitBox, int itemWidth, int itemHeight) {
        this.displayName = displayName;
        this.itemTexture = itemTexture;
        this.hitBox = hitBox;
        this.width = itemWidth;
        this.height = itemHeight;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public float[] getHitBox() {
        return hitBox;
    }

    public void setHitBox(float[] hitBox) {
        this.hitBox = hitBox;
    }
}
