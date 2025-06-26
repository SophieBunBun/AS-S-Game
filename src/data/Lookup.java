package data;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.graphics.ThemeDefinition;
import com.googlecode.lanterna.gui2.WindowDecorationRenderer;
import com.googlecode.lanterna.gui2.WindowPostRenderer;
import screenUtil.ScreenType;

import java.util.*;

public class Lookup {

    //Screens
    public static final Dictionary<ScreenType, String> screenTypeName;

    //Items
    public static final Dictionary<Enum, String> itemNames;
    //TODO itemDescriptions

    //Store entries
    public static final StoreEntry[] storeEntries;

    //Expeditions
    public static final Dictionary<ExpeditionZone, ExpeditionInfo> expeditionZoneInfo;

    static {
        screenTypeName = new Hashtable<>();
        screenTypeName.put(ScreenType.MENU, "Menu");
        screenTypeName.put(ScreenType.TITLE, "Title");
        screenTypeName.put(ScreenType.SHOP, "Shop");
        screenTypeName.put(ScreenType.EXPEDITIONS, "Expeditions");
        screenTypeName.put(ScreenType.GREENHOUSE, "Greenhouse");
        screenTypeName.put(ScreenType.PROCESSING, "Processing");
        screenTypeName.put(ScreenType.CUSTOMERS, "Customers");
        screenTypeName.put(ScreenType.SETTINGS, "Settings");

        itemNames = new Hashtable<>();
        itemNames.put(Item.FERTILIZER_1, "Basic fertilizer");
        itemNames.put(Item.FERTILIZER_2, "Advanced fertilizer");

        //TODO itemDescriptions

        storeEntries = new StoreEntry[]{
                new StoreEntry(Item.FERTILIZER_1, 5, 1),
                new StoreEntry(Item.FERTILIZER_2, 10, 5)
        };

        expeditionZoneInfo = new Hashtable<>();

        ExpeditionLoottable loottable = new ExpeditionLoottable();
        loottable.addEntry(new AdvancedItemStack(Item.EFFECT_PLANT, 0, PlantEffect.LIVER), 5, 1, 2);

        expeditionZoneInfo.put(ExpeditionZone.FOREST,
                new ExpeditionInfo("Forest", "Big ass forest with good liver medicine", loottable));
    }
}
