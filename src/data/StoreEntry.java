package data;

public record StoreEntry(
        Item item,
        int price,
        int level
) {
    @Override
    public String toString() {
        return "StoreEntry{" +
                "item=" + item +
                ", price=" + price +
                ", level=" + level +
                '}';
    }
}
