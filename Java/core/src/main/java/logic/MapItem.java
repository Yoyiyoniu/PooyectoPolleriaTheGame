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

    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }
}
