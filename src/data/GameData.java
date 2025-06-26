package data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GameData implements IGameData {

    private boolean running;
    private int key = 0;

    private int cash = 0;
    private int level = 1;

    private Inventory inventory;

    //TODO finish gamedata


    public Inventory getInv(){
        return inventory;
    }

    public boolean canBuy(int price, int quantity){
        int total = price * quantity;
        if (cash >= total){
            return true;
        }
        return false;
    }

    public void setKeyPressed(int key){
        this.key = key;
    }

    public int getKeyPressed(){
        int key = this.key;
        this.key = 0;
        return key;
    }

    public GameData() {
        running = true;
    }

    public void tick() {

    }

    public void forwardTicks(int count) {
        for(int i = 0; i < count; i++){
            tick();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void endGame() {
        running = false;
    }

    @Override
    public List<StoreEntry> shopOptions() {
        List<StoreEntry> options = new ArrayList<>();
        for (StoreEntry entry : Lookup.storeEntries){
            if (entry.level() <= this.level){
                options.add(entry);
            }
        }
        return options;
    }
}
