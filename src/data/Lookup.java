package data;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class Lookup {

    public static final String[] menuScreenOptions;

    public static final Dictionary<Enum, String> itemNames;
    //TODO itemDescriptions

    public static final StoreEntry[] storeEntries;

    static {
        menuScreenOptions = new String[]{
                "Title",
                "Shop",
                "Expeditions",
                "Greenhouse",
                "Processing",
                "Customers",
                "Settings"
        };

        itemNames = new Hashtable<>();
        itemNames.put(Item.FERTILIZER_1, "Basic fertilizer");
        itemNames.put(Item.FERTILIZER_2, "Advanced fertilizer");

        //TODO itemDescriptions

        storeEntries = new StoreEntry[]{
                new StoreEntry(Item.FERTILIZER_1, 5, 1),
                new StoreEntry(Item.FERTILIZER_2, 10, 5)
        };
    }
}
