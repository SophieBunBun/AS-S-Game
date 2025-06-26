package screenUtil;

public class TitleScreen implements IAreaScreen{

    @Override
    public boolean move(MoveDirection dir) {
        return true;
    }

    @Override
    public int action() {return -1;}

    @Override
    public int printScreen(int start, int width, int height, ScreenRenderer grid) {return 0;}
}
