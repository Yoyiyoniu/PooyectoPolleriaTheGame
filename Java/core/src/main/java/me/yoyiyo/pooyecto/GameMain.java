package me.yoyiyo.pooyecto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import logic.InteractableItem;
import logic.Movement;
import logic.Player;
import logic.Textures;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;

import java.util.ArrayList;

public class GameMain extends BasicGame {
    public static final String GAME_IDENTIFIER = "me.yoyiyo.pooyecto";

    private Player p;
    private final ArrayList<InteractableItem> items = new ArrayList<>();

    @Override
    public void initialise() {
        p = new Player(Textures.PLAYER.getTexture(), 0, 0);

    }

    @Override
    public void update(float delta) {

        new Movement(p, delta, items);


        // Take items Events
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

        for (InteractableItem item : items) {
            item.render(g);
        }

        p.render(g);
    }

    public void dispose() {
        p.dispose();
    }
}