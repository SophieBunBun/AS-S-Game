package data;

import java.util.List;

public class GameData {

    private boolean running;
    private int key = 0;

    private int cash = 0;

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

    public void tick() {}

    public boolean isRunning() {
        return running;
    }

    public void endGame() {
        running = false;
    }
}
