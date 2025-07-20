package data;

import java.util.*;

public class GameData implements IGameData {

    private boolean running;

    private int cash = 2000;
    private int level = 1;

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
    public int getCash() {
        return cash;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public boolean buy(StoreEntry entry, int count) {
        cash -= entry.price() * count;
        return true;
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

    @Override
    public int amountPurchasable(StoreEntry entry) {
        return cash / entry.price();
    }

    @Override
    public List<ItemStack> getSellable() {
        return List.of(new ItemStack(Item.FERTILIZER_1, 5000));
    }

    @Override
    public int sell(ItemStack item) {
        int earned = Lookup.itemValues.get(item.getType());
        earned *= item.getCount();
        cash += earned;
        return earned;
    }

    @Override
    public int startExpedition(ExpeditionZone zone, int days) {
        return 0;
    }

    @Override
    public List<PlantData> getPlantData() {
        return List.of();
    }

    @Override
    public boolean setBreeding(int slot, AdvancedItemStack main, AdvancedItemStack addon, List<ItemStack> extras) {
        return false;
    }

    @Override
    public boolean setGrowing(int slot, AdvancedItemStack seed, List<ItemStack> extras) {
        return false;
    }

    @Override
    public boolean collectPlant(int slot) {
        return false;
    }

    @Override
    public List<Map.Entry<PlantEffect, Boolean>> getPillsList(int count) {
        return List.of();
    }

    @Override
    public void makePill(PlantEffect effect, int count) {

    }

    @Override
    public List<PillRequest> getRequests() {
        return List.of();
    }

    @Override
    public List<Boolean> availablePills(PillRequest request) {
        return List.of();
    }

    @Override
    public boolean fulfillRequest(PillRequest request) {
        return false;
    }
}
