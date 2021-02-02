package net.playershop.players;

import net.playershop.shop.BaseSubClass;
import net.playershop.shop.Items;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author SmallasWater
 * Create on 2021/2/2 11:42
 * Package net.playershop.players
 */
public class FakeItems{
    private int buyCount;

    private int resetTime;

    private String name;

    private FakeItems(String name,int buyCount,int resetTime){
        this.name = name;
        this.buyCount = buyCount;
        this.resetTime = resetTime;
    }

    public FakeItems(Items items){
//        this.buyCount = items.getBuyCount();
//        this.resetTime = items.getResetTime();
        this.name = items.getName();
    }

    public static FakeItems getFakeItems(Map map){

        int buyCount = (int) map.get("buyCount");
        int resetTime = (int) map.get("resetTime");
        String name = map.get("name").toString();
        return new FakeItems(name,buyCount,resetTime);
    }

    public LinkedHashMap<String,Object> getSaveMap(){
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        map.put("name",name);
        map.put("buyCount",buyCount);
        map.put("resetTime",resetTime);
        return map;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public int getResetTime() {
        return resetTime;
    }

    @Override
    public String toString() {
        return "items:{name: "+getName()+" resetCount:"
                +getBuyCount()+" resetTime:"+getResetTime()+"}";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FakeItems){
            return ((FakeItems) obj).name.equalsIgnoreCase(name) && ((FakeItems) obj).buyCount == buyCount
                    && ((FakeItems) obj).resetTime == resetTime;
        }
        return false;
    }

    public String getName() {
        return name;
    }
}
