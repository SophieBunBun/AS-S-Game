package screenUtil;

public class SettingsScreen implements IAreaScreen{

    @Override
    public boolean move(MoveDirection dir) {
        return true;
    }

    @Override
    public int action() {return -2;}

    @Override
    public int printScreen(int start, int width, int height, ScreenRenderer grid) {return 0;}
}
