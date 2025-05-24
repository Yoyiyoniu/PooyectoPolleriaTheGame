package me.yoyiyo.pooyecto.logic.item;

import com.badlogic.gdx.physics.box2d.World;
import me.yoyiyo.pooyecto.enums.Paths;

public class Chicken extends InteractuableItem{

    public Chicken(World world, float x, float y, String name) {
        super(world, x, y, Paths.CHICKEN.getPath(), name);
    }
}
