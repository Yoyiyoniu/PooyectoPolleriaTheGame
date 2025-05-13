package me.yoyiyo.pooyecto.desktop;

import org.mini2Dx.desktop.DesktopMini2DxConfig;

import com.badlogic.gdx.backends.lwjgl.DesktopMini2DxGame;

import me.yoyiyo.pooyecto.GameMain;

public class DesktopLauncher {
	 public static void main (String[] arg) {
		  DesktopMini2DxConfig config = new DesktopMini2DxConfig(GameMain.GAME_IDENTIFIER);
		  config.vSyncEnabled = true;

		  config.fullscreen = true;
		  config.forceExit = true;
		  config.title = "Pooyecto";

		  java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

		  int posX = screenSize.width;
		  int posY = screenSize.height;

		  config.width = posX;
		  config.height = posY;

		  new DesktopMini2DxGame(new GameMain(), config);
	 }
}