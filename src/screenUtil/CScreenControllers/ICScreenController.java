package screenUtil.CScreenControllers;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.AbstractInteractableComponent;
import screenUtil.MoveDirection;

import java.util.Dictionary;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface ICScreenController {

    void updateScreen();
    Dictionary<AbstractInteractableComponent<?>, BiConsumer<Boolean, MoveDirection>> getCycler();
    AbstractInteractableComponent<?> getSelected();
    void returnFocus();
    List<Consumer<TerminalSize>> getUpdatePreferedSizeList();
    void killScreen();
}
