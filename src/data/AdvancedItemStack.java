package data;

public class AdvancedItemStack {

    private final Item type;
    private int count;
    private final PlantEffect effect;
    private int effectLevel;

    public AdvancedItemStack(Item type){
        this.type = type;
        this.count = 1;
        this.effect = PlantEffect.NONE;
    }

    public AdvancedItemStack(Item type, int count){
        this.type = type;
        this.count = count;
        this.effect = PlantEffect.NONE;
    }

    public AdvancedItemStack(Item type, int count, PlantEffect effect){
        this.type = type;
        this.count = count;
        this.effect = effect;
        this.effectLevel = 1;
    }

    public AdvancedItemStack(Item type, int count, PlantEffect effect, int effectLevel){
        this.type = type;
        this.count = count;
        this.effect = effect;
        this.effectLevel = effectLevel;
    }

    public int getCount(){
        return count;
    }

    public Item getType(){
        return type;
    }

    public void setCount(int count){
        this.count = count;
    }

    public int getEffectLevel() {
        return effectLevel;
    }

    public PlantEffect getEffect() {
        return effect;
    }

    public void setEffectLevel(int effectLevel) {
        this.effectLevel = effectLevel;
    }
}
