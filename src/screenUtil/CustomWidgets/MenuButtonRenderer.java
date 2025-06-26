package screenUtil.CustomWidgets;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import data.Lookup;

public class MenuButtonRenderer implements Button.ButtonRenderer {

    @Override
    public TerminalPosition getCursorLocation(Button button) {
        return button.getPosition();
    }

    @Override
    public TerminalSize getPreferredSize(Button button) {
        return new TerminalSize(18, 1);
    }

    @Override
    public void drawComponent(TextGUIGraphics textGUIGraphics, Button button) {

        if (button.isEnabled()){
            textGUIGraphics.setForegroundColor(TextColor.ANSI.WHITE);
            textGUIGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        }
        else {
            textGUIGraphics.setForegroundColor(TextColor.ANSI.BLACK);
            textGUIGraphics.setBackgroundColor(TextColor.ANSI.WHITE);
        }

        textGUIGraphics.fill(' ');
        textGUIGraphics.putString(2, 0, button.getLabel());
        if (button.isFocused()){
            textGUIGraphics.setBackgroundColor(TextColor.ANSI.WHITE);
            textGUIGraphics.putString(0, 0, " ");
        }
    }
}
