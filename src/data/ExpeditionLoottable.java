package data;

public class ExpeditionLoottable implements ILoottable {

    @Override
    public void addEntry(AdvancedItemStack item, int weight, int min, int max) {

    }

    @Override
    public AdvancedItemStack rollLoot() {
        return null;
    }

    //TODO implement this class


    private record ExpeditionLootEntry(
            AdvancedItemStack item,
            int weight,
            int min,
            int max
    ) {}
}
