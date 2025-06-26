package screenUtil;

import data.GameData;

public class Screen {

    private GameWindow gameWindow;
    private GameData gameData;

    private IAreaScreen[] screens;

    private int selectedScreen;
    private int openScreen = 1;

    private ScreenRenderer printGrid;

    private int width;
    private int height;

    public Screen(boolean console, GameData data){


        gameWindow = customWindow;
        gameData = data;

        screens = new IAreaScreen[]{
                new MenuScreen(),
                new TitleScreen(),
                new TitleScreen(),
                new TitleScreen(),
                new TitleScreen(),
                new TitleScreen(),
                new TitleScreen(),
                new SettingsScreen()
        };

        printGrid = new ScreenRenderer(customWindow);
    }

    public void move (MoveDirection direction){
        boolean focus = screens[selectedScreen].move(direction);
        if (focus) {selectedScreen = selectedScreen == 0 ? openScreen : 0;
        System.out.println("Switching scrin");}

        printScreen();
    }

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

    public void close(){
        gameWindow.close();
    }
}
