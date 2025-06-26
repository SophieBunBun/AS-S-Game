package main;

import data.GameData;
import screenUtil.ScreenController;


public class Game {

    public static final GameData gameData = new GameData();
    public static final ScreenController screenController = new ScreenController();

    public static void main(String[] args) throws InterruptedException {

        screenController.initialize(true);

        while (gameData.isRunning()) {

            /*
            switch (gameData.getKeyPressed()){
                case (KeyEvent.VK_ENTER) -> {screenController.action();}
                case (KeyEvent.VK_LEFT) -> {screenController.move(MoveDirection.LEFT);}
                case (KeyEvent.VK_RIGHT) -> {screenController.move(MoveDirection.RIGHT);}
                case (KeyEvent.VK_UP) -> {screenController.move(MoveDirection.UP);}
                case (KeyEvent.VK_DOWN) -> {screenController.move(MoveDirection.DOWN);}
            }
            */

            gameData.tick();
            screenController.render();

            Thread.sleep(5);
        };
    }
}
