package net.playershop;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import net.playershop.commands.ShopCommand;
import net.playershop.shop.BaseSubClass;
import net.playershop.shop.Items;
import net.playershop.shop.ShopClass;
import net.playershop.windows.WindowListener;
import updata.AutoData;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author 若水
 */
public class PlayerShop extends PluginBase {
    private static PlayerShop instance;

    public LinkedList<ShopClass> shop = new LinkedList<>();

    public LinkedHashMap<Player,Boolean> canCmd = new LinkedHashMap<>();

    public LinkedHashMap<Player,ShopClass> clickShop = new LinkedHashMap<>();

    public LinkedHashMap<Player, Items<BaseSubClass>> click = new LinkedHashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        this.getLogger().info("点券商店开始加载");
        if(AutoData.defaultUpData(this,getFile(),"SmallasWater","PointsShop")){
            return;
        }
        if(!new File(this.getDataFolder()+"/Shops").exists()){
            if(!new File(this.getDataFolder()+"/Shops").mkdirs()){
                Server.getInstance().getLogger().info("文件夹Shops创建失败");
            }
        }
        this.saveDefaultConfig();
        this.reloadConfig();
        for(String name:getShops().keySet()){
            if(!new File(this.getDataFolder()+"/Shops/"+name).exists()){
                if(!new File(this.getDataFolder()+"/Shops/"+name).mkdirs()){
                    this.getLogger().info("创建商城 "+name+"失败");
                }
            }
        }

        Config config = PlayerShop.getInstance().getConfig();
        Map map = (Map) config.get("商城");
        for(Object s:map.keySet()){
            shop.add(new ShopClass(s.toString()));
        }

        this.getServer().getCommandMap().register("PointsShop",new ShopCommand(this));
        this.getServer().getPluginManager().registerEvents(new WindowListener(),this);
        this.getLogger().info("插件启动完成");
    }

    public static PlayerShop getInstance() {
        return instance;
    }

    public LinkedHashMap<String,String> getShops(){
        LinkedHashMap<String,String> stringStringLinkedHashMap = new LinkedHashMap<>();
        Map m = (Map) getConfig().get("商城");
        for(Object o:m.keySet()){
            stringStringLinkedHashMap.put((String) o, (String) m.get(o));
        }
        return stringStringLinkedHashMap;
    }

    public String getTitle(){
        return getConfig().getString("GUI标题");
    }
}
