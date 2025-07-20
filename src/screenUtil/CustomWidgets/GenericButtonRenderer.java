package screenUtil.CustomWidgets;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class GenericButtonRenderer implements Button.ButtonRenderer {

    @Override
    public TerminalPosition getCursorLocation(Button button) {
        return button.getPosition();
    }

    @Override
    public TerminalSize getPreferredSize(Button button) {
        return new TerminalSize(button.getLabel().length(), 1);
    }

    @Override
    public void drawComponent(TextGUIGraphics textGUIGraphics, Button button) {

        if (!button.isFocused()){
            textGUIGraphics.setForegroundColor(TextColor.ANSI.WHITE);
            textGUIGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
        }
        else {
            textGUIGraphics.setForegroundColor(TextColor.ANSI.BLACK);
            textGUIGraphics.setBackgroundColor(TextColor.ANSI.WHITE);
        }

        textGUIGraphics.fill(' ');
        textGUIGraphics.putString(2, 0, button.getLabel());
    }
}
