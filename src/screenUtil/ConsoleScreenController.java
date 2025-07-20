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
import data.StoreEntry;
import main.Game;
import screenUtil.CScreenControllers.CEmptyScreen;
import screenUtil.CScreenControllers.CMenuScreenController;
import screenUtil.CScreenControllers.CShopScreenController;
import screenUtil.CScreenControllers.ICScreenController;
import screenUtil.CustomWidgets.MenuButtonRenderer;
import screenUtil.CustomWidgets.StoreEntryComp;

import javax.sound.sampled.Line;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConsoleScreenController implements IGameRenderer{

    private Screen screen;
    private MultiWindowTextGUI gui;
    private BasicWindow mainWindow;
    private Panel mainPanel;

    private List<ICScreenController> screens = new ArrayList<>();
    private int focusedScreen = 0;

    private boolean lock;

    private Window alert;
    private boolean resizeLock;

    private Runnable winSizeCheck = () -> {
        if ((screen.getTerminalSize().getColumns() < 80 || screen.getTerminalSize().getRows() < 30)){
            if (!resizeLock){
                this.gui.addWindow(alert);
                resizeLock = true;
            }
        }
        else if (resizeLock){
            this.gui.removeWindow(alert);
            resizeLock = false;
        }
    };

    private Queue<Runnable> runQueue = new ArrayDeque<>();

    public void addToRunQueue(Runnable runnable){runQueue.add(runnable);}

    public void initialize(ScreenController screenController){

        try {
            this.screen = new DefaultTerminalFactory().setInitialTerminalSize(new TerminalSize(80, 30)).createScreen();
            this.gui = new MultiWindowTextGUI(screen);

            gui.setTheme(SimpleTheme.makeTheme(
                    true,
                    TextColor.ANSI.WHITE, TextColor.ANSI.BLACK,
                    TextColor.ANSI.WHITE, TextColor.ANSI.BLACK,
                    TextColor.ANSI.BLACK, TextColor.ANSI.WHITE,
                    TextColor.ANSI.BLACK
            ));

            this.mainWindow = new BasicWindow();
            this.mainWindow.setHints(Set.of(Window.Hint.FULL_SCREEN));

            this.alert = new BasicWindow();
            this.alert.setComponent(new Label("Resize window!"));

            new Thread(() -> {
                while (true) {

                    try {
                        if (!resizeLock) {
                            KeyStroke key = screen.pollInput();
                            if (key != null) {
                                AbstractInteractableComponent<?> focusedButton = screens.get(focusedScreen).getSelected();
                                Dictionary<AbstractInteractableComponent<?>, BiConsumer<Boolean, MoveDirection>> cycler =
                                        screens.get(focusedScreen).getCycler();

                                switch (key.getKeyType()) {
                                    case ArrowLeft ->
                                            cycler.get(focusedButton).accept(key.isShiftDown(), MoveDirection.LEFT);
                                    case ArrowRight ->
                                            cycler.get(focusedButton).accept(key.isShiftDown(), MoveDirection.RIGHT);
                                    case ArrowUp ->
                                            cycler.get(focusedButton).accept(key.isShiftDown(), MoveDirection.UP);
                                    case ArrowDown ->
                                            cycler.get(focusedButton).accept(key.isShiftDown(), MoveDirection.DOWN);
                                    case Enter -> focusedButton.handleInput(key);
                                }
                            }
                        }
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            this.mainPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
            this.mainPanel.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill, LinearLayout.GrowPolicy.CanGrow));
            this.mainWindow.setComponent(mainPanel);

            this.mainWindow.addWindowListener(new WindowListener() {
                @Override
                public void onResized(Window window, TerminalSize terminalSize, TerminalSize terminalSize1) {

                    winSizeCheck.run();

                    if (!resizeLock){
                        for (ICScreenController controller : screens){
                            for (Consumer<TerminalSize> resize : controller.getUpdatePreferedSizeList()){
                                resize.accept(terminalSize1);
                            }
                        }
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

            // MENU INITIALIZATION
            screens.add(new CMenuScreenController(this));
            focusedScreen = 0;
            Game.screenController.changeScreen(ScreenType.TITLE);

            screen.startScreen();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Panel getMainPanel() {
        return mainPanel;
    }

    public Screen getScreen() {
        return screen;
    }

    public MultiWindowTextGUI getGUI() {
        return gui;
    }

    public void cycleScreenFocus(){
        focusedScreen++;
        if (focusedScreen == screens.size()) {focusedScreen = 0;}
        screens.get(focusedScreen).returnFocus();
    }

    public void setOpenScreen (ScreenType type) {
        lock = true;
        if (screens.size() > 1){
            ICScreenController screenToKill = screens.removeLast();
            screenToKill.killScreen();
        }

        switch (type) {
            case TITLE -> screens.add(new CEmptyScreen(this));
            case SHOP -> screens.add(new CShopScreenController(this));
            case EXPEDITIONS -> screens.add(new CEmptyScreen(this));
            case GREENHOUSE -> screens.add(new CEmptyScreen(this));
            case PROCESSING -> screens.add(new CEmptyScreen(this));
            case CUSTOMERS -> screens.add(new CEmptyScreen(this));
            case SETTINGS -> screens.add(new CEmptyScreen(this));
        }
        lock = false;
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

    private void updateScreens(){
        for (ICScreenController screen : screens){
            screen.updateScreen();
        }
    }

    @Override
    public void render() {
        try {
            if (!lock && !resizeLock){

                while (!runQueue.isEmpty()){
                    runQueue.remove().run();
                }

                updateScreens();
            }
            winSizeCheck.run();
            gui.updateScreen();
        }
        catch (IOException e){
            System.out.println("Failed to refresh console renderer - " + e.getLocalizedMessage());
        }
    }
}
