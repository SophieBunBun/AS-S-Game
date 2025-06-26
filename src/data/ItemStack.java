package data;

public class ItemStack {

    private final Item type;
    private int count;

    public ItemStack (Item type){
        this.type = type;
        this.count = 1;
    }

    public ItemStack (Item type, int count){
        this.type = type;
        this.count = count;
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
}
