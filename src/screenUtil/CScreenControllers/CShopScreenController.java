package screenUtil.CScreenControllers;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import data.ItemStack;
import data.StoreEntry;
import main.Game;
import screenUtil.ConsoleScreenController;
import screenUtil.CustomWidgets.StoreEntryComp;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CShopScreenController extends AGridScreenController {

    private Runnable killScreen;

    private AbstractInteractableComponent<?> focusedTopButton;
    private boolean onTop = true;
    private int currentPage = 0;

    private List<Runnable> screenUpdateList = new ArrayList<>();
    private List<Consumer<TerminalSize>> updatePreferedSizeList = new ArrayList<>();

    @Override
    public AbstractInteractableComponent<?> getSelected() {
        return onTop ? focusedTopButton : super.getSelected();
    }

    @Override
    public List<Consumer<TerminalSize>> getUpdatePreferedSizeList() {
        return updatePreferedSizeList;
    }

    @Override
    public void killScreen() {
        killScreen.run();
    }

    public void updateScreen(){
        System.out.println(focusedButton.isFocused());
    }

    @Override
    public void returnFocus() {
        if (onTop) {focusedTopButton.takeFocus();}
        else {focusedButton.takeFocus();}
    }

    public CShopScreenController (ConsoleScreenController controller) {
        Panel storeMainPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        killScreen = () -> {controller.getMainPanel().removeComponent(storeMainPanel);};

        Panel storeInfoPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Panel contentPanel = new Panel();

        updatePreferedSizeList.add((size) ->
                storeMainPanel.setPreferredSize(size.withRelativeColumns(-20)));
        updatePreferedSizeList.add((size) ->
                contentPanel.setPreferredSize(storeMainPanel.getPreferredSize().withRelativeRows(-3)));
        updatePreferedSizeList.add((size) ->
                storeInfoPanel.setPreferredSize(storeMainPanel.getPreferredSize().withRows(3)));

        for (Consumer<TerminalSize> update : updatePreferedSizeList){
            update.accept(controller.getScreen().getTerminalSize());
        }

        Label infoPut = new Label("");
        infoPut.setPreferredSize(new TerminalSize(10, 1));

        Consumer<BiConsumer<Integer[], List<List<AbstractInteractableComponent<?>>>>> showBasic = (inp) -> {
            int totalRows = contentPanel.getPreferredSize().getRows() / 17;
            int totalColumns = contentPanel.getPreferredSize().getColumns() / 17;

            GridLayout layout = new GridLayout(totalColumns);
            contentPanel.removeAllComponents();
            contentPanel.setLayoutManager(layout);

            List<List<AbstractInteractableComponent<?>>> totalComponents = new ArrayList<>();
            totalComponents.add(new ArrayList<>());

            inp.accept(new Integer[]{totalColumns, totalRows}, totalComponents);

            for (AbstractInteractableComponent<?> comp : totalComponents.getFirst()){
                contentPanel.addComponent(comp.withBorder(Borders.singleLine()));
            }

            clearCycler();
            createShopCycler(controller, contentPanel,
                    () -> {return List.of(
                            (Button) storeInfoPanel.getChildren().toArray()[0],
                            (Button) storeInfoPanel.getChildren().toArray()[1]);},
                    totalComponents, totalRows, totalColumns);

            currentPage = 0;

            screenUpdateList.clear();
            screenUpdateList.add(() -> {
                contentPanel.removeAllComponents();
                for (AbstractInteractableComponent<?> comp : totalComponents.get(currentPage)){
                    contentPanel.addComponent(comp.withBorder(Borders.singleLine()));
                }
            });
            focusedButton = totalComponents.get(currentPage).getFirst();
        };

        BiConsumer<Integer[], List<List<AbstractInteractableComponent<?>>>> showShopInp = (size, totalComponents) -> {
            int row = 0, col = 0, page = 0;

            for (StoreEntry entry : Game.gameData.shopOptions()){

                totalComponents.get(page).add( new StoreEntryComp(entry, (count) -> {
                    int purchasableAmount = Game.gameData.amountPurchasable(entry);
                    if (purchasableAmount != 0){
                        int trimCount = purchasableAmount >= count ? count : purchasableAmount;
                        Game.gameData.buy(entry, trimCount);
                        infoPut.setText("Bought " + trimCount + "!");
                    }
                    else {
                        infoPut.setText("Not enough cash");
                    }
                }));

                col++;
                if (col == size[0]) {col = 0; row++;}
                if (row == size[1]) {row = 0; page++; totalComponents.add(new ArrayList<>());}
            }
        };

        BiConsumer<Integer[], List<List<AbstractInteractableComponent<?>>>> showSellInp = (size, totalComponents) -> {

            int row = 0, col = 0, page = 0;

            for (ItemStack item : Game.gameData.getSellable()){

                totalComponents.get(page).add( new StoreEntryComp(item, (count) -> {
                    int sellableAmount = item.getCount();
                    if (sellableAmount != 0){
                        int trimCount = sellableAmount >= count ? count : sellableAmount;
                        int earned = Game.gameData.sell(new ItemStack(item.getType(), trimCount));
                        infoPut.setText("Got " + (earned) + "Y!");
                    }
                    else {
                        infoPut.setText("None left");
                    }
                }));

                col++;
                if (col == size[0]) {col = 0; row++;}
                if (row == size[1]) {row = 0; page++; totalComponents.add(new ArrayList<>());}
            }
        };

        Runnable shopButtonActivation = () -> {
            showBasic.accept(showShopInp);
            updatePreferedSizeList.set(3, (size) -> {showBasic.accept(showShopInp);});
        };

        Runnable sellButtonActivation = () -> {
            showBasic.accept(showSellInp);
            updatePreferedSizeList.set(3, (size) -> {showBasic.accept(showSellInp);});
        };

        storeInfoPanel.addComponent(new Button("Buy", shopButtonActivation));
        storeInfoPanel.addComponent(new Button("Sell", sellButtonActivation));
        storeInfoPanel.addComponent(infoPut);

        showBasic.accept(showShopInp);
        updatePreferedSizeList.add((size) -> {showBasic.accept(showShopInp);});

        focusedTopButton = (AbstractInteractableComponent<?>) storeInfoPanel.getChildren().toArray()[0];

        storeMainPanel.addComponent(storeInfoPanel.withBorder(Borders.singleLine("Shop")));
        storeMainPanel.addComponent(contentPanel);
        controller.addToRunQueue(() -> {controller.getMainPanel().addComponent(storeMainPanel);});

    }

    private void createShopCycler(ConsoleScreenController controller, Panel contentPanel, Supplier<List<Button>> shopButtons, List<List<AbstractInteractableComponent<?>>> options, int totalRows, int totalCol){

        List<Button> buttons = shopButtons.get();
        screenCycler.put(buttons.get(0), (shift, direction) -> {
            switch (direction) {
                case RIGHT -> {
                    focusedTopButton = buttons.get(1);
                    focusedTopButton.takeFocus();
                }
                case LEFT -> {if (shift) {controller.cycleScreenFocus();}}
                case DOWN -> {if (shift) {onTop = false; controller.getGUI().getActiveWindow().setFocusedInteractable(focusedButton);}}
            }
        });

        screenCycler.put(buttons.get(1), (shift, direction) -> {
            switch (direction) {
                case LEFT -> {
                    if (shift) {controller.cycleScreenFocus(); return;}
                    focusedTopButton = buttons.get(0);
                    focusedTopButton.takeFocus();
                }
                case DOWN -> {if (shift) {onTop = false; controller.getGUI().getActiveWindow().setFocusedInteractable(focusedButton);}}
            }
        });

        createGridCycler(controller, contentPanel, options, totalRows, totalCol);
    }

    @Override
    protected void shiftTop() {
        onTop = true;
        focusedTopButton.takeFocus();
    }
}
