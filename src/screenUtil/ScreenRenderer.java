package screenUtil;

public class ScreenRenderer {
    /*
    private char[][] grid;
    private boolean[][] selectedGrid;
    private int width;
    private int height;

    private boolean console;
    private GameWindow window;

    public ScreenRenderer(boolean console){

        this.console = console;
        if (console){

        } else {
            this.window = window;
        }
    }

    public void newGrid(int width, int height){
        this.width = width;
        this.height = height;

        grid = new char[width][height];
        selectedGrid = new boolean[width][height];

        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                grid[x][y] = ' ';
            }
        }
    }

    public void writeAt(int x, int y, String text){
        for (int x1 = 0; x1 < text.length(); x1++){
            grid[x1 + x][y] = text.charAt(x1);
        }
    }

    public void writeBlock(int x, int y, int x1, int y1, char c){
        for (int x2 = x; x2 <= x1; x2++){
            for (int y2 = y; y2 <= y1; y2++){
                grid[x2][y2] = c;
            }
        }
    }

    public void selectAt(int x, int y){
        selectedGrid[x][y] = true;
    }

    public void selectBlock(int x, int y, int x1, int y1){
        for (int x2 = x; x2 <= x1; x2++){
            for (int y2 = y; y2 <= y1; y2++){
                selectedGrid[x2][y2] = true;
            }
        }
    }

    public void printScreen(){
        StringBuilder sb = new StringBuilder();
        sb.append("<html><pre>");
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                String addon = selectedGrid[x][y] ?
                        "<font color='#ffe1b5' bgcolor='#ab7c37'>" :
                        "<font color='#ab7c37' bgcolor='#ffe1b5'>";
                sb.append(addon + grid[x][y] + "</font>");
            }
            sb.append("<br>");
        }
        sb.append("</pre></html>");
        window.setText(sb.toString());
    }

     */
}
