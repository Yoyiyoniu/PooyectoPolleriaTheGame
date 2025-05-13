package logic;

public class MapItem {
    private final String displayName;
    private final String itemTexture;
    private int itemWidth;
    private int itemHeight;

    public MapItem(String displayName, String itemTexture, int itemWidth, int itemHeight) {
        this.displayName = displayName;
        this.itemTexture = itemTexture;
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
    }

    public void MultiplySize(float factor) {
        this.itemWidth = Math.round(this.itemWidth * factor);
        this.itemHeight = Math.round(this.itemHeight * factor);
    }

    public int getItemWidth() {
        return itemWidth;
    }

    public int getItemHeight() {
        return itemHeight;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getItemTexture() {
        return itemTexture;
    }
}
