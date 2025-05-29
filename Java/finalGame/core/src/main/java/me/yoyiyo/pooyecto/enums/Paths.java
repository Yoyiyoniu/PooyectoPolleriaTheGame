package me.yoyiyo.pooyecto.enums;

public enum Paths {
    MAP("TiledMap/Kitchen.tmx"),
    PLAYER("player.png"),
    CHICKEN("items/r_chicken.png"),;

    private String path;
    Paths(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
