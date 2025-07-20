package screenUtil.CustomWidgets;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.AbstractInteractableComponent;
import com.googlecode.lanterna.gui2.InteractableRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import data.ItemStack;
import data.Lookup;
import data.StoreEntry;
import screenUtil.MoveDirection;

import java.util.Dictionary;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class StoreEntryComp extends AbstractInteractableComponent<StoreEntryComp> {

    private final Consumer<Integer> action;
    private final StoreEntry entry;
    private ItemStack itemStack;
    private int flash = 0;

    public StoreEntryComp (StoreEntry entry, Consumer<Integer> action){
        this.action = action;
        this.entry = entry;
        this.itemStack = null;
    }

    public StoreEntryComp (ItemStack itemStack, Consumer<Integer> action){
        this.action = action;
        this.entry = null;
        this.itemStack = itemStack;
    }

    public synchronized void updateItemStack(ItemStack itemStack){
        this.itemStack = itemStack;
    }

    @Override
    public synchronized boolean isFocusable() {
        return true;
    }

    @Override
    protected synchronized Result handleKeyStroke(KeyStroke keyStroke) {
        this.action.accept((keyStroke.isShiftDown() ? 5 : 1) * (keyStroke.isCtrlDown() ? 5 : 1));
        flash = 10;
        return super.handleKeyStroke(keyStroke);
    }

    public synchronized boolean getFlash(){
        return flash-- > 0;
    }

    public synchronized StoreEntry getEntry() {
        return entry;
    }

    public synchronized ItemStack getItemStack() {
        return itemStack;
    }

    protected StoreEntryRenderer createDefaultRenderer() {
        return new StoreEntryRenderer();
    }

    @Override
    public String toString() {
        return "StoreEntryComp{" + entry.toString() + itemStack.toString() + "}";
    }

    public static class StoreEntryRenderer implements InteractableRenderer<StoreEntryComp>{

        @Override
        public TerminalPosition getCursorLocation(StoreEntryComp storeEntryComp) {
            return null;
        }

        @Override
        public TerminalSize getPreferredSize(StoreEntryComp storeEntryComp) {
            return new TerminalSize(15, 13);
        }

        @Override
        public void drawComponent(TextGUIGraphics textGUIGraphics, StoreEntryComp storeEntryComp) {

            //System.out.println(storeEntryComp.isFocused());

            if (storeEntryComp.getFlash()){
                textGUIGraphics.setBackgroundColor(TextColor.ANSI.WHITE);
                textGUIGraphics.fillRectangle(new TerminalPosition(-1 , -1), new TerminalSize(15, 13), ' ');
            }
            else {
                if (storeEntryComp.isFocused()){
                    textGUIGraphics.setBackgroundColor(TextColor.ANSI.WHITE);
                    textGUIGraphics.fillRectangle(new TerminalPosition(-1 , -1), new TerminalSize(15, 13), ' ');
                    textGUIGraphics.setBackgroundColor(TextColor.ANSI.BLACK);
                }

                textGUIGraphics.fill(' ');
                textGUIGraphics.drawLine(1, 9, 13, 9, '-');

                if (storeEntryComp.getEntry() != null){
                    StoreEntry entry = storeEntryComp.getEntry();
                    printName(Lookup.itemNames.get(entry.item()).split(" "), textGUIGraphics);
                    textGUIGraphics.putString(1, 12, "Price: " + entry.price() + "Y");
                }
                else {
                    ItemStack itemStack = storeEntryComp.getItemStack();
                    printName(Lookup.itemNames.get(itemStack.getType()).split(" "), textGUIGraphics);
                    textGUIGraphics.putString(1, 12, "Value: " + Lookup.itemValues.get(itemStack.getType()) + "Y");
                    textGUIGraphics.putString(1, 13, "Count: " + itemStack.getCount());
                }
            }
        }

        private void printName(String[] writeS, TextGUIGraphics textGUIGraphics){

            int i = 0, line = 0;
            while (i < writeS.length){
                StringBuilder acc = new StringBuilder();
                for (int i1 = i; i1 < writeS.length; i1++){

                    if (writeS[i1].length() <= 13 - acc.length()){
                        acc.append((acc.isEmpty()) ? writeS[i1] : " " + writeS[i1]);
                        i++;
                    }
                    else {i1 = writeS.length;}
                }
                textGUIGraphics.putString(6 - (acc.length() / 2), line + 10, acc.toString());
            }
        }
    }
}
