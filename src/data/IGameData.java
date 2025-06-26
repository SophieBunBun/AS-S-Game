package data;

import com.googlecode.lanterna.gui2.Border;

import java.util.List;
import java.util.Map;

public interface IGameData {

    //Returns how much cash you have
    int getCash();

    //Returns true if successful purchase
    boolean buy(StoreEntry entry, int count);

    //Returns a list of all the things you can
    List<StoreEntry> shopOptions();

    //Returns true if purchasable
    boolean canBuy(StoreEntry entry, int count);

    //Returns inventory
    Inventory getInventory();

    //Returns nothing
    void sell(List<ItemStack> items);

    //Returns wait time
    int startExpedition(ExpeditionZone zone, int days);

    //Return list with each breeding slot info, if full, give plant info, if empty give null
    List<PlantData> getPlantData();

    //Returns true if planting was successful
    boolean setBreeding(int slot, AdvancedItemStack main, AdvancedItemStack addon, List<ItemStack> extras);

    //Returns true if plating was successful
    boolean setGrowing(int slot, AdvancedItemStack seed, List<ItemStack> extras);

    //Returns true if plant collection was successful
    boolean collectPlant(int slot);

    //Count is the number to make at a time, returns alls pills you can make and if you have enough to make them (boolean)
    List<Map.Entry<PlantEffect, Boolean>> getPillsList(int count);

    //Makes a pill from the effect identifier and the count
    void makePill(PlantEffect effect, int count);

    //Returns a list of all requests
    List<PillRequest> getRequests();

    //Returns a list of the availability of all the pills needed
    List<Boolean> availablePills(PillRequest request);

    //Returns true if the request was fulfilled
    boolean fulfillRequest(PillRequest request);
}
