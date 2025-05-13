package me.yoyiyo.pooyecto;

import Ui.Floor;
import Ui.utils.CalculateCords;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import items.Chicken;
import logic.InteractableItem;
import logic.Movement;
import logic.Player;
import Ui.utils.Barrier;
import Ui.utils.Scenario;
import logic.Textures;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;

import java.util.ArrayList;

public class GameMain extends BasicGame {
    public static final String GAME_IDENTIFIER = "me.yoyiyo.pooyecto";

    private Player p;
    private final ArrayList<Floor> grounds = new ArrayList<>();
    private final ArrayList<Barrier> barriers = new ArrayList<>();
    private final ArrayList<InteractableItem> items = new ArrayList<>();

    @Override
    public void initialise() {
        p = new Player(Textures.PLAYER.getTexture(), 0, 0);

        String groundPath = Textures.FLOOR.getTexture();

        Scenario scenario = new Scenario(groundPath, grounds, 55, 25, true, barriers);
        scenario.centerPositionPlayer(p);

        items.add(new Chicken());
        float[] realCoords = CalculateCords.calculateRealCoordinates(10, 10, scenario.getOffsetX(), scenario.getOffsetY());

        items.get(0).setPosition(realCoords[0], realCoords[1]);

    }

    @Override
    public void update(float delta) {

        new Movement(p, delta, barriers,items);

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            if(p.getItemHand() == null){
                for (InteractableItem item : items) {
                    p.takeItem(item);
                }
            }else {
                for (InteractableItem item : items) {
                    if (p.getItemHand() == item) {
                        p.dropItem();
                        break;
                    }
                }
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

        for (InteractableItem item : items) {
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