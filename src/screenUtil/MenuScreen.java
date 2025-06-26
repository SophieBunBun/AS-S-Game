package screenUtil;

import data.Lookup;

public class MenuScreen implements IAreaScreen{

    private int selectedOption = 1;
    private int option = 1;
    private static final int totalOptions = Lookup.menuScreenOptions.length;

    @Override
    public boolean move(MoveDirection dir) {
        switch (dir){
            case DOWN -> {if (++option > totalOptions) {option = totalOptions;} return false;}
            case UP -> {if (--option < 1) {option = 1;} return false;}
            case RIGHT -> {return true;}
            default -> {return false;}
        }
    }

    @Override
    public int action() {
        selectedOption = option;
        return option;
    }

    @Override
    public int printScreen(int start, int width, int height, ScreenRenderer grid) {

        PrintUtils.printBoxEmpty(start, 0, 20, height, grid);

        int y = 2;
        for (String option : Lookup.menuScreenOptions){
            grid.writeAt(start + 3, y, option);
            y += 2;
        }

        grid.selectAt(2, selectedOption * 2);
        grid.selectBlock(start + 1, option * 2, start + 18, option * 2);

        return 20;
    }
}
