package screenUtil;

public class PrintUtils {

    public static void printBoxEmpty(int x, int y, int widthr, int depthr, ScreenRenderer grid){

        int width = widthr - 1;
        int depth = depthr - 1;

        grid.selectBlock(x, y, x + width, y);
        grid.selectBlock(x, y + 1, x, y + depth);
        grid.selectBlock(x + width, y + 1, x + width, y + depth);
        grid.selectBlock(x + 1, y + depth, x + width - 1, y + depth);
    }
}
