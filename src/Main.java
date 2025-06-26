import data.GameData;
import screenUtil.GameWindow;
import screenUtil.MoveDirection;
import screenUtil.Screen;

import java.awt.event.KeyEvent;

public class Main {

    private static final GameData gameData = new GameData();
    private static final Screen screenController = new Screen(false, gameData);

    public static void main(String[] args) throws InterruptedException {

        screenController.changeRes(80, 30);

        while (gameData.isRunning()) {

            switch (gameData.getKeyPressed()){
                case (KeyEvent.VK_ENTER) -> {screenController.action();}
                case (KeyEvent.VK_LEFT) -> {screenController.move(MoveDirection.LEFT);}
                case (KeyEvent.VK_RIGHT) -> {screenController.move(MoveDirection.RIGHT);}
                case (KeyEvent.VK_UP) -> {screenController.move(MoveDirection.UP);}
                case (KeyEvent.VK_DOWN) -> {screenController.move(MoveDirection.DOWN);}
            }

            gameData.tick();
            screenController.printScreen();

            Thread.sleep(5);
        };

        screenController.close();
    }
}
