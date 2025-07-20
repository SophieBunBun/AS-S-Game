package screenUtil.CScreenControllers;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.AbstractInteractableComponent;
import screenUtil.ConsoleScreenController;
import screenUtil.MoveDirection;

import java.util.Dictionary;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CEmptyScreen implements ICScreenController{

    ConsoleScreenController controller;

    public CEmptyScreen(ConsoleScreenController controller){
        this.controller = controller;
    }

    @Override
    public void updateScreen() {

    }

    @Override
    public Dictionary<AbstractInteractableComponent<?>, BiConsumer<Boolean, MoveDirection>> getCycler() {
        return null;
    }

    @Override
    public AbstractInteractableComponent<?> getSelected() {
        return null;
    }

    @Override
    public void returnFocus() {
        controller.cycleScreenFocus();
    }

    @Override
    public List<Consumer<TerminalSize>> getUpdatePreferedSizeList() {
        return List.of();
    }

    @Override
    public void killScreen() {

    }
}
