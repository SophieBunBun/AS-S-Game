package data;

public interface ILoottable {

    void addEntry(AdvancedItemStack item, int weight, int min, int max);
    AdvancedItemStack rollLoot();

}
