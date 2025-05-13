package logic;

public enum Textures {
    PLAYER("assets/chicken.png"),
    FLOOR("assets/scenario/floor.png"),
    CHICKEN("assets/items/r_chicken.png");

    private final String path;

    Textures(String path) {
        this.path = path;
    }
    public String getTexture() {
        return path;
    }
}
