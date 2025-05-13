package me.yoyiyo.pooyecto;

import logic.Floor;
import logic.Movement;
import logic.Player;
import logic.utils.Barrier;
import logic.utils.Scenario;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;

import java.util.ArrayList;
import java.util.List;

public class GameMain extends BasicGame {
    public static final String GAME_IDENTIFIER = "me.yoyiyo.pooyecto";

    private Player p;
    private List<Floor> grounds;
    private List<Barrier> barriers;
    private Scenario scenario;

    @Override
    public void initialise() {
        p = new Player("chicken.png", 0, 0);
        grounds = new ArrayList<Floor>();
        barriers = new ArrayList<Barrier>();

        String groundPath = "scenario/floor.png";

        scenario = new Scenario(groundPath, grounds, 55, 25, true, barriers);
        scenario.centerPositionPlayer(p);

    }

    @Override
    public void update(float delta) {
        new Movement(p, delta, barriers);
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