package net.playershop.players;

import cn.nukkit.utils.Config;
import net.playershop.PlayerShop;
import net.playershop.shop.BaseSubClass;
import net.playershop.shop.Items;

import java.util.*;

/**
 * @author SmallasWater
 * Create on 2021/2/2 11:17
 * Package net.playershop.players
 */
public class PlayerFile {

    private String playerName;

    private LinkedList<BuyItemData> data = new LinkedList<>();

    public PlayerFile(String playerName){
        this.playerName = playerName;
    }

    private PlayerFile(String playerName, LinkedList<BuyItemData> data){
        this.playerName = playerName;
        this.data = data;
    }

    public BuyItemData getBuyItemData(Items items){
        BuyItemData buyItemData = new BuyItemData(items);
        if(data.contains(buyItemData)){
            return data.get(data.indexOf(buyItemData));
        }
        return null;
    }

    public void addBuyCount(Items items){
        BuyItemData buyItemData = new BuyItemData(items);
        if(data.contains(buyItemData)){
            buyItemData = data.get(data.indexOf(buyItemData));
            buyItemData.setBuyCount(buyItemData.getBuyCount() + 1);
        }else{
            buyItemData.setBuyCount(1);
            data.add(buyItemData);
        }
        save();
    }

    public void onRun(){
        for(BuyItemData buyItemData:data){
            buyItemData.onRunTime();
        }
    }

    public static PlayerFile getPlayerFile(Config config){
        String name = config.getString("playerName");
        List<Map> linkedList = config.getMapList("buyDataList");
        LinkedList<BuyItemData> linkedList1 = new LinkedList<>();
        for(Map map: linkedList){
            linkedList1.add(BuyItemData.getInstance(map));
        }
        return new PlayerFile(name,linkedList1);
    }

    private void save(){
        Config config = new Config(PlayerShop.getInstance().getDataFolder()+"/Players/"+playerName+".yml",Config.YAML);
        config.set("playerName",playerName);
        LinkedList<LinkedHashMap<String,Object>> maps = new LinkedList<>();
        for(BuyItemData buyItemData: data){
            maps.add(buyItemData.getSaveMap());
        }
        config.set("buyDataList",maps);
        config.save();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PlayerFile){
            return ((PlayerFile) obj).playerName.equalsIgnoreCase(playerName);
        }
        return false;
    }
}
