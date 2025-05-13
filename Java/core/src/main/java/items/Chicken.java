package items;

import logic.InteractableItem;
import logic.Textures;

public class Chicken extends InteractableItem {

    public Chicken() {
        super("Pollo Crudo", Textures.CHICKEN.getTexture(), new float[]{20, 20});
        setSize(25);
    }

}
