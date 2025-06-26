package screenUtil;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import data.GameData;
import data.Lookup;
import main.Game;
import screenUtil.CustomWidgets.MenuButtonRenderer;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class ConsoleScreenController implements IGameRenderer{

    private Screen screen;
    private MultiWindowTextGUI gui;
    private BasicWindow mainWindow;
    private Panel mainPanel;

    private Panel openScreen;

    private Button lastScreenFocus;
    private Button focusedButton;
    private Dictionary<Button, Consumer<MoveDirection>> cycler;

    private List<Consumer<ScreenType>> menuUpdateList;
    private List<Consumer<GameData>> screenUpdateList;

    private List<Consumer<TerminalSize>> updatePreferedSizeList;

    public void initialize(ScreenController screenController){

        try {
            this.screen = new DefaultTerminalFactory().createScreen();
            this.gui = new MultiWindowTextGUI(screen);

            gui.setTheme(SimpleTheme.makeTheme(
                    true,
                    TextColor.ANSI.WHITE, TextColor.ANSI.BLACK,
                    TextColor.ANSI.WHITE, TextColor.ANSI.BLACK,
                    TextColor.ANSI.BLACK, TextColor.ANSI.WHITE,
                    TextColor.ANSI.BLACK
            ));

            this.mainWindow = new BasicWindow();
            this.mainWindow.setHints(Set.of(Window.Hint.EXPANDED, Window.Hint.FULL_SCREEN));

            new Thread(() -> {
                while (true) {
                    try {
                        KeyStroke key = screen.pollInput();
                        if (key != null) {
                            switch (key.getKeyType()){
                                case ArrowLeft -> cycler.get(focusedButton).accept(MoveDirection.LEFT);
                                case ArrowRight -> cycler.get(focusedButton).accept(MoveDirection.RIGHT);
                                case ArrowUp -> cycler.get(focusedButton).accept(MoveDirection.UP);
                                case ArrowDown -> cycler.get(focusedButton).accept(MoveDirection.DOWN);
                                case Enter -> focusedButton.handleKeyStroke(key);
                            }
                        }
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            this.mainPanel = new Panel();
            this.mainWindow.setComponent(mainPanel);

            this.mainWindow.addWindowListener(new WindowListener() {
                @Override
                public void onResized(Window window, TerminalSize terminalSize, TerminalSize terminalSize1) {
                    for (Consumer<TerminalSize> resize : updatePreferedSizeList){
                        resize.accept(terminalSize1);
                    }
                }

                @Override
                public void onMoved(Window window, TerminalPosition terminalPosition, TerminalPosition terminalPosition1) {

                }

                @Override
                public void onInput(Window window, KeyStroke keyStroke, AtomicBoolean atomicBoolean) {
                }

                @Override
                public void onUnhandledInput(Window window, KeyStroke keyStroke, AtomicBoolean atomicBoolean) {

                }
            });

            this.gui.addWindow(mainWindow);

            //CYCLER INIT
            cycler = new Hashtable<>();

            //RESIZING
            updatePreferedSizeList = new ArrayList<>();

            // MENU INITIALIZATION
            buildMenu();

            screen.startScreen();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void buildMenu(){
        Panel contentPanel = new Panel().setPreferredSize(screen.getTerminalSize().withRelativeRows(-2).withColumns(19));
        updatePreferedSizeList.add((size) -> {
           contentPanel.setPreferredSize(screen.getTerminalSize().withRelativeRows(-2).withColumns(19));
        });
        contentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL).setSpacing(1));

        menuUpdateList = new ArrayList<>();

        List<Button> menuButtons = new ArrayList<>();

        for (ScreenType option : ScreenType.values()){
            if (option != ScreenType.MENU){
                Panel optionPanel = new Panel().setSize(new TerminalSize(18, 1));

                Button optionButton = new Button(Lookup.screenTypeName.get(option), () -> {
                    Game.screenController.changeScreen(option);
                });

                optionButton.setRenderer(new MenuButtonRenderer());

                menuUpdateList.add((screenType -> {
                    if (screenType == option){
                        optionButton.setEnabled(false);
                    }
                    else {
                        optionButton.setEnabled(true);
                    }
                }));

                if (option.ordinal() == 1){
                    optionButton.takeFocus();
                    focusedButton = optionButton;
                }

                menuButtons.add(optionButton);

                optionPanel.addComponent(optionButton, BorderLayout.Location.CENTER);
                contentPanel.addComponent(optionPanel);
            }
        }

        configureMenuButtonCycling(menuButtons);

        mainPanel.addComponent(contentPanel.withBorder(Borders.singleLine("Menu")));
        Game.screenController.changeScreen(ScreenType.TITLE);
    }

    private void configureMenuButtonCycling(List<Button> menuButtons){
        cycler.put(menuButtons.get(0), (direction -> {
            switch (direction){
                case DOWN -> {
                    focusedButton = menuButtons.get(1);
                    focusedButton.takeFocus();
                }
                case RIGHT -> switchToScreenFocus();
            }
        }));

        cycler.put(menuButtons.get(6), (direction -> {
            switch (direction){
                case UP -> {
                    focusedButton = menuButtons.get(5);
                    focusedButton.takeFocus();
                }
                case RIGHT -> switchToScreenFocus();
            }
        }));

        for (int x = 1; x < menuButtons.size() - 1; x++) {
            int finalX = x;
            cycler.put(menuButtons.get(x), (direction -> {
                switch (direction){
                    case DOWN -> {
                        focusedButton = menuButtons.get(finalX + 1);
                        focusedButton.takeFocus();
                    }
                    case UP -> {
                        focusedButton = menuButtons.get(finalX - 1);
                        focusedButton.takeFocus();
                    }
                    case RIGHT -> switchToScreenFocus();
                }
            }));
        }
    }

    private void switchToScreenFocus(){
        Button lastMenuFocus = focusedButton;
        focusedButton = lastScreenFocus;
        lastScreenFocus = lastMenuFocus;
        focusedButton.takeFocus();
    }

    private void updateMenu (ScreenType type) {
        for (Consumer<ScreenType> update : menuUpdateList){
            update.accept(type);
        }
    }

    private void createPanel (ScreenType type) {
        if (openScreen != null) {mainPanel.removeComponent(openScreen);}
        Panel contentPanel = new Panel();
        openScreen = contentPanel;
        mainPanel.addComponent(openScreen);
    }

    public void updateCurrentScreen (GameData gameData) {
        for (Consumer<GameData> update : screenUpdateList){
            update.accept(gameData);
        }
    }

    @Override
    public void setOpenScreen(ScreenType type) {
        updateMenu(type);
        createPanel(type);
    }

    @Override
    public void close() {
        try {
            screen.close();
        }
        catch (IOException e){
            System.out.println("Failed to close console renderer - " + e.getLocalizedMessage());
        }
    }

    @Override
    public void render() {
        try {
            gui.updateScreen();
        }
        catch (IOException e){
            System.out.println("Failed to refresh console renderer - " + e.getLocalizedMessage());
        }
    }
}
