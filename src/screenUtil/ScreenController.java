package screenUtil;

import data.GameData;
import main.Game;

public class ScreenController {

    private IGameRenderer renderer;
    private boolean consoleMode = true;

    private GameData gameData;

    private ScreenType openScreen;

/*
    public void move (MoveDirection direction){
        boolean focus = screens[selectedScreen].move(direction);
        if (focus) {selectedScreen = selectedScreen == 0 ? openScreen : 0;
        System.out.println("Switching scrin");}

        printScreen();
    }
 */

    public void changeScreen (ScreenType type){
        openScreen = type;
        renderer.setOpenScreen(type);
    }

    public ScreenType getOpenScreen (){
        return openScreen;
    }

    public void setConsoleMode(boolean set){
        if (consoleMode != set){
            consoleMode = set;
            if (set) {
                if (renderer != null) {
                    renderer.close();
                }
                renderer = new ConsoleScreenController();
                renderer.initialize(this);
            }
            else {
                if (renderer != null) {
                    renderer.close();
                }
            }
        }
    }

    public void render(){
        renderer.render();
    }

    public void initialize(boolean b) {
        this.gameData = Game.gameData;
        consoleMode = false;
        setConsoleMode(true);
    }

    /*
    public void action (){
        int result = screens[selectedScreen].action();
        System.out.println("Action");
        if (selectedScreen == 0) {
            selectedScreen = result;
        }
        if (result == -2) {
            System.out.println("Ending game");
            gameData.endGame();
        }

        printScreen();
        this.printGrid.newGrid(width, height);
    }

    public void changeRes (int width, int height){
        this.width = width;
        this.height = height;
        this.printGrid.newGrid(width, height);
        printScreen();
    }

    public void printScreen () {
        int cursorLocation = 0;
        cursorLocation = screens[0].printScreen(0, width, height, printGrid);
        screens[openScreen].printScreen(cursorLocation, width - cursorLocation, height, printGrid);

        printGrid.printScreen();
        this.printGrid.newGrid(width, height);
    }
     */
}
