package screenUtil.CScreenControllers;

import com.googlecode.lanterna.gui2.AbstractInteractableComponent;
import com.googlecode.lanterna.gui2.Panel;
import screenUtil.ConsoleScreenController;
import screenUtil.MoveDirection;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class AGridScreenController implements ICScreenController {

    protected AbstractInteractableComponent<?> focusedButton;
    protected Dictionary<AbstractInteractableComponent<?>, BiConsumer<Boolean, MoveDirection>> screenCycler;

    protected void clearCycler(){
        screenCycler = new Hashtable<>();
    }

    public Dictionary<AbstractInteractableComponent<?>, BiConsumer<Boolean, MoveDirection>> getCycler() {
        return screenCycler;
    }

    public AbstractInteractableComponent<?> getSelected() {
        return focusedButton;
    }

    public void returnFocus() {
        focusedButton.takeFocus();
    }

    protected void shiftTop(){};

    protected void shiftBottom(){};

    protected void createGridCycler(ConsoleScreenController controller, Panel contentPanel, List<List<AbstractInteractableComponent<?>>> options, int totalRows, int totalCol){

        int row = 0, col = 0, page = 0;
        boolean run = true;

        while(run){

            int pageFinal = page;
            int colFinal = col + 1;
            int rowFinal = row;

            screenCycler.put(options.get(page).get(col + ((totalCol - 1) * row)), (shift, direction) -> {
                switch (direction) {
                    case LEFT -> {
                        if (shift) {controller.cycleScreenFocus();}
                        if (colFinal != 1)  {
                            focusedButton = options.get(pageFinal).get(colFinal - 1 + (totalCol * rowFinal) - 1);
                            focusedButton.takeFocus();
                        }
                    }

                    case RIGHT -> {
                        if ((colFinal != totalCol) && (colFinal + ((totalCol - 1) * rowFinal)) < options.get(pageFinal).size()) {
                            focusedButton = options.get(pageFinal).get(colFinal + 1 + (totalCol * rowFinal) - 1);
                            focusedButton.takeFocus();
                        }
                    }

                    case UP -> {
                        if (shift) {shiftTop();}
                        else if ((pageFinal == 0) && (rowFinal == 0)) {return;}
                        else if (rowFinal == 0) {
                            contentPanel.removeAllComponents();
                            for (AbstractInteractableComponent<?> comp : options.get(pageFinal - 1)) {
                                contentPanel.addComponent(comp);
                            }
                            focusedButton = options.get(pageFinal - 1).get(colFinal + (totalCol * (totalRows - 1)) - 1);
                            focusedButton.takeFocus();
                        }
                        else {
                            focusedButton = options.get(pageFinal).get(colFinal + (totalCol * (rowFinal - 1)) - 1);
                            focusedButton.takeFocus();
                        }
                    }

                    case DOWN -> {
                        if (shift) {shiftBottom();}
                        else if ((pageFinal == options.size() - 1) && (colFinal + ((totalCol - 1) * rowFinal)) < options.get(pageFinal).size()) {return;}
                        else if ((pageFinal == options.size() - 1) && (rowFinal == totalRows - 1)) {return;}
                        else if (rowFinal == totalRows - 1){
                            contentPanel.removeAllComponents();
                            for (AbstractInteractableComponent<?> comp : options.get(pageFinal + 1)) {
                                contentPanel.addComponent(comp);
                            }
                            focusedButton = options.get(pageFinal - 1).get(colFinal + (totalCol * (totalRows - 1)) - 1);
                            focusedButton.takeFocus();
                        }
                        else {
                            focusedButton = options.get(pageFinal).get(colFinal + ((totalCol - 1) * (rowFinal + 1)));
                            focusedButton.takeFocus();
                        }
                    }
                }
            });

            col++;
            if (col == totalCol) {col = 0; row++;}
            if (row == totalRows) {row = 0; page++;}
            run = !(page >= options.size() || col + (row * totalCol) >= options.get(page).size());
        }
    }
}
