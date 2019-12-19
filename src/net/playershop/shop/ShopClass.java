package net.playershop.shop;

import cn.nukkit.Server;
import cn.nukkit.utils.Config;
import net.playershop.PlayerShop;


import java.io.File;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * @author 若水
 */
public class ShopClass {

    private LinkedList<Items<BaseSubClass> > items = new LinkedList<>();
    private String name;
    private File file;

    public ShopClass(File file){
        this.file = file;
        init();
        this.name = file.getName();

    }

    public ShopClass(String name){
        this(new File(PlayerShop.getInstance().getDataFolder()+"/Shops/"+name));

    }

    public static ShopClass getShop(String name){
        for(ShopClass shopClass:PlayerShop.getInstance().shop){
            if(shopClass.name.equals(name)){
                return shopClass;
            }
        }
        return null;
    }

    public static boolean createShopClass(String name){
        if(new File(PlayerShop.getInstance().getDataFolder()+"/Shops/"+name).exists()){
            return false;
        }else{
            if(!new File(PlayerShop.getInstance().getDataFolder()+"/Shops/"+name).mkdir()){
                Server.getInstance().getLogger().warning("创建商城 "+name+"失败");
                return false;
            }else{
                Config config = PlayerShop.getInstance().getConfig();
                LinkedHashMap<String,Object> m = new LinkedHashMap<>();
                Map map = (Map) config.get("商城");
                for(Object n:map.keySet()){
                   m.put(n.toString(),map.get(n));
                }
                m.put(name,"textures/ui/invite_pressed");
                config.set("商城",m);
                config.save();
            }
            return true;
        }
    }

    public boolean createItems(String name){
        if(items.contains(new Items(getName(),name))){
            return false;
        }else{
            PlayerShop.getInstance().saveResource("shop.yml","/Shops/"+this.name+"/"+name+".yml",false);
            Config config = new Config(this.file+"/"+name+".yml",Config.YAML);
            config.set("name",name);
            config.save();
            File file = new File(this.file+"/"+name+".yml");
            addItems(file);
            return true;
        }
    }

    public boolean removeItems(String name){
       return removeItems(new Items(getName(),name));
    }

    public boolean removeItems(Items name){
        if(!items.contains(name)){
            return false;
        }else{
            File file = new File(this.file+"/"+name+".yml");
            items.remove(name);
            if(file.exists()){
                if(!file.delete()){
                    Server.getInstance().getLogger().warning("删除文件: "+file+"失败 请手动删除");
                }
            }
            return true;
        }
    }
    public void addItems(Items<BaseSubClass> items){
        if(!this.items.contains(items)){
            this.items.add(items);
        }
    }


    private void addItems(File items){
        Items<BaseSubClass>  items1 = Items.loadFile(getName(),items);
        this.items.add(items1);
    }

    public Items<BaseSubClass> getItemByName(String name){
        for(Items<BaseSubClass> items: this.items){
            if(items.equals(new Items<>(this.name, name))){
                return items;
            }
        }
        return null;
    }



    private static final Pattern F =  Pattern.compile("^.yml$");
    private void init(){
        File[] files = file.listFiles();
        if(files != null){
            for(File file:files){
                if(file.isFile()){
                    if(F.matcher(file.getName()).matches()){
                        items.add(Items.loadFile(getName(),file));
                    }
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public File getFile(){
        return file;
    }

    public boolean delete() {
        if (!file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            for(Items i:items){
                this.removeItems(i);
            }
        }
        PlayerShop.getInstance().shop.remove(this);
        Config config = PlayerShop.getInstance().getConfig();
        Map map = (Map) config.get("商城");
        map.remove(name);
        config.set("商城",map);
        config.save();
        return file.delete();
    }

    public LinkedList<Items<BaseSubClass> > getItems() {
        return items;
    }
}
