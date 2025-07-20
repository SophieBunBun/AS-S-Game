package screenUtil.CScreenControllers;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import data.Lookup;
import main.Game;
import screenUtil.ConsoleScreenController;
import screenUtil.CustomWidgets.MenuButtonRenderer;
import screenUtil.MoveDirection;
import screenUtil.ScreenType;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CMenuScreenController implements ICScreenController{

    private Runnable killScreen;

    private ScreenType type = ScreenType.TITLE;

    private Dictionary<AbstractInteractableComponent<?>, BiConsumer<Boolean, MoveDirection>> cycler = new Hashtable<>();
    private AbstractInteractableComponent<?> focusedButton;

    private List<Consumer<TerminalSize>> updatePreferedSizeList = new ArrayList<>();
    private List<Consumer<ScreenType>> menuButtonsUpdateList = new ArrayList<>();
    private List<Runnable> menuUpdateList = new ArrayList<>();

    @Override
    public void updateScreen() {
        for (Consumer<ScreenType> update : menuButtonsUpdateList){
            update.accept(type);
        }

        for (Runnable update : menuUpdateList){
            update.run();
        }
    }

    @Override
    public Dictionary<AbstractInteractableComponent<?>, BiConsumer<Boolean, MoveDirection>> getCycler() {
        return cycler;
    }

    @Override
    public AbstractInteractableComponent<?> getSelected() {
        return focusedButton;
    }

    @Override
    public void returnFocus() {
        focusedButton.takeFocus();
    }

    @Override
    public List<Consumer<TerminalSize>> getUpdatePreferedSizeList() {
        return updatePreferedSizeList;
    }

    @Override
    public void killScreen() {
        killScreen.run();
    }

    public CMenuScreenController(ConsoleScreenController controller){
        Panel contentPanel = new Panel().setPreferredSize(controller.getMainPanel().getPreferredSize().withRelativeRows(-2).withColumns(19));

        killScreen = () -> {controller.getMainPanel().removeComponent(contentPanel);};

        updatePreferedSizeList.add((size) -> {
            contentPanel.setPreferredSize(controller.getScreen().getTerminalSize().withRelativeRows(-2).withColumns(19));
        });

       contentPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL).setSpacing(1));

        List<Button> menuButtons = new ArrayList<>();

        for (ScreenType option : ScreenType.values()){
            if (option != ScreenType.MENU){
                Panel optionPanel = new Panel().setSize(new TerminalSize(18, 1));

                Button optionButton = new Button(Lookup.screenTypeName.get(option), () -> {
                    type = option;
                    Game.screenController.changeScreen(option);
                });

                optionButton.setRenderer(new MenuButtonRenderer());

                menuButtonsUpdateList.add((screenType -> {
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

        configureMenuButtonCycling(controller, menuButtons);

        Label money = new Label("").setBackgroundColor(TextColor.ANSI.WHITE).setForegroundColor(TextColor.ANSI.BLACK)
                        .setLabelWidth(18);
        Label level = new Label("").setBackgroundColor(TextColor.ANSI.WHITE).setForegroundColor(TextColor.ANSI.BLACK)
                .setLabelWidth(18);

        menuUpdateList.add(() -> {
            int moneyCount = Game.gameData.getCash();
            StringBuilder sb = new StringBuilder();
            sb.append("Cash: ");
            sb.append(moneyCount);
            sb.append(" Y");
            sb.repeat(" ", 18 - sb.length());
            money.setText(sb.toString());
        });

        menuUpdateList.add(() -> {
            int levelCount = Game.gameData.getLevel();
            StringBuilder sb = new StringBuilder();
            sb.append("Level - ");
            sb.append(levelCount);
            sb.repeat(" ", 18 - sb.length());
            level.setText(sb.toString());
        });

        contentPanel.addComponent(money);
        contentPanel.addComponent(level);
        controller.getMainPanel().addComponent(contentPanel.withBorder(Borders.singleLine("Menu")));

        focusedButton = menuButtons.getFirst();
    }

    private void configureMenuButtonCycling(ConsoleScreenController controller, List<Button> menuButtons){

        for (int x = 0; x < menuButtons.size(); x++) {
            int finalX = x;
            cycler.put(menuButtons.get(x), (shift, direction) -> {
                switch (direction){
                    case DOWN -> focusedButton = menuButtons.get((finalX + 1) % menuButtons.size());
                    case UP -> focusedButton = menuButtons.get(finalX == 0 ? menuButtons.size() - 1 : finalX - 1 );
                    case RIGHT -> {if (shift) {controller.cycleScreenFocus();} return;}
                    default -> {return;}
                }

                if (!focusedButton.isEnabled()) {
                    cycler.get(focusedButton).accept(shift, direction);}
                else {focusedButton.takeFocus();}
            });
        }
    }
}
