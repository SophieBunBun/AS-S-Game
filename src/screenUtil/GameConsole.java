package screenUtil;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;

public class GameConsole {

    private Screen screen;
    private MultiWindowTextGUI gui;

    public GameConsole(){

        try {
            this.screen = new DefaultTerminalFactory().createScreen();
            this.gui = new MultiWindowTextGUI(screen);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addWindow (int width){
        
    }
}
