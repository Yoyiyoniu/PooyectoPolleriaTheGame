package me.yoyiyo.pooyecto;

import Ui.Floor;
import Ui.utils.CalculeCords;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import items.Chicken;
import logic.InteractuableItem;
import logic.Movement;
import logic.Player;
import Ui.utils.Barrier;
import Ui.utils.Scenario;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;

import java.util.ArrayList;
import java.util.List;

public class GameMain extends BasicGame {
    public static final String GAME_IDENTIFIER = "me.yoyiyo.pooyecto";

    private Player p;
    private ArrayList<Floor> grounds = new ArrayList<Floor>();
    private ArrayList<Barrier> barriers = new ArrayList<Barrier>();
    private Scenario scenario;
    private ArrayList<InteractuableItem> items = new ArrayList<InteractuableItem>();

    @Override
    public void initialise() {
        p = new Player("chicken.png", 0, 0);

        String groundPath = "scenario/floor.png";

        scenario = new Scenario(groundPath, grounds, 55, 25, true, barriers);
        scenario.centerPositionPlayer(p);

        items.add(new Chicken()); // Ejemplo: Agregar un pollo
        float[] realCoords = CalculeCords.calculateRealCoordinates(5, 10, scenario.getOffsetX(), scenario.getOffsetY());
        items.get(0).setPosition(realCoords[0], realCoords[1]); // Posición inicial del ítem
    }

    @Override
    public void update(float delta) {

        new Movement(p, delta, barriers);

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            for (InteractuableItem item : items) {
                p.takeItem(item);
            }
        }
    }

    @Override
    public void interpolate(float alpha) {
    }

    @Override
    public void render(Graphics g) {
        for (Floor ground : grounds) {
            ground.render(g);
        }

        for (Barrier barrier : barriers) {
            barrier.render(g);
        }

        // Renderizar los ítems
        for (InteractuableItem item : items) {
            item.render(g);
        }

        p.render(g);
    }

    public void dispose() {
        p.dispose();
        for (Floor ground : grounds) {
            ground.dispose();
        }
        for (Barrier barrier : barriers) {
            barrier.dispose();
        }
    }
}