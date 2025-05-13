package items;

import logic.InteractuableItem;

public class Chicken extends InteractuableItem {

    public Chicken() {
        super("Pollo Crudo", "assets/items/r_chicken.png");
    }

    @Override
    public void MultiplySize(float factor) {
        super.MultiplySize(factor * 10); // Multiplica el tamaño por un factor adicional
    }
}
