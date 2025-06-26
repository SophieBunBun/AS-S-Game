package screenUtil;

public interface IAreaScreen {

    public boolean move(MoveDirection dir);
    public int action();

    public int printScreen(int start, int width, int height, ScreenRenderer grid);
}
