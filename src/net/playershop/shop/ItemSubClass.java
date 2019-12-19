package net.playershop.shop;

import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import net.playershop.Tools;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 若水
 */
public class ItemSubClass extends BaseSubClass{

    private Item item;

    private static final String NOT = "not";

    public ItemSubClass(String name, String message, Item item) {
        super(name,message);
        this.item = item;
    }

    static ItemSubClass loadItem(String name, String message, Map item) {
        int id = (int) item.get("id");
        int damage = (int) item.get("damage");
        int count = (int) item.get("count");
        String nbt = (String) item.get("nbt");
        CompoundTag tag = null;
        if(!NOT.equals(nbt)){
            byte[] bytes = Tools.hexStringToBytes(nbt);
            if(bytes != null){
                tag = Item.parseCompoundTag(bytes);
            }

        }
        Item item1 = new Item(id,damage,count);
        if(tag != null){
            item1.setNamedTag(tag);
        }
        return new ItemSubClass(name,message,item1);
    }

    @Override
    public boolean isItemClass() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CmdSubClass){
            return getName().equals(((CmdSubClass) obj).getName())
                    && getMessage().equals(((CmdSubClass) obj).getMessage());
        }
        return false;
    }

    @Override
    public Item getItem() {
        return item;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public LinkedHashMap<String, Object> getConfig() {
        LinkedHashMap<String,Object> save = super.getConfig();
        LinkedHashMap<String,Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("id",item.getId());
        linkedHashMap.put("damage",item.getDamage());
        linkedHashMap.put("count",item.getCount());
        if(item.hasCompoundTag()){
            linkedHashMap.put("nbt",Tools.bytesToHexString(item.getCompoundTag()));
        }else{
            linkedHashMap.put("nbt",NOT);
        }
        save.put("item",linkedHashMap);
        return save;

    }


}
