package net.playershop.shop;

import cn.nukkit.Server;
import cn.nukkit.utils.Config;
import net.playershop.PlayerShop;


import java.io.File;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;


/**
 * @author 若水
 */
public class ShopClass {

    private LinkedList<Items<BaseSubClass> > items = new LinkedList<>();
    private String name;
    private File file;

    public ShopClass(File file){
        this.file = file;
        this.name = file.getName();
        init();


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
            if(!new File(PlayerShop.getInstance().getDataFolder()+"/Shops/"+name).mkdirs()){
                Server.getInstance().getLogger().warning("创建商城 "+name+"失败");
                return false;
            }else{
                PlayerShop.getInstance().shop.add(new ShopClass(name));
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

    private boolean exitItems(String name){
        for(Items items:items){
            if(items.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
    public boolean createItems(String name){
        if(exitItems(name)){
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
       return removeItems(getItemByName(name));
    }

    public boolean removeItems(Items name){
        if(name == null){
            return false;
        }else{
            File file = new File(this.file+"/"+name.getName()+".yml");
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
        Items<BaseSubClass>  items1 = Items.loadFile(this.name,items);
        this.items.add(items1);
    }

    public Items<BaseSubClass> getItemByName(String name){
        for(Items<BaseSubClass> items: this.items){
            if(items.getName().equals(name)){
                return items;
            }
        }
        return null;
    }



    private void init(){
        File[] files = file.listFiles();
        if(files != null){
            for(File file:files){
                if(file.isFile()){
                    items.add(Items.loadFile(this.name,file));

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
        try {
            if (!file.exists()) {
                return true;
            }
            if (file.isDirectory()) {
                for (Items i : items) {
                    this.removeItems(i);
                }
            }
            deleteDirectory(file);
            PlayerShop.getInstance().shop.remove(this);
            Config config = PlayerShop.getInstance().getConfig();
            Map map = (Map) config.get("商城");
            map.remove(name);
            config.set("商城", map);
            config.save();
            if(!file.delete()){
                PlayerShop.getInstance().getLogger().info("删除"+file+"文件失败");
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }

    private void deleteDirectory(File file){
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] f = file.listFiles();
            if(f != null){
                for(File file1:f){
                    deleteDirectory(file1);
                }
            }
        }
        file.delete();
    }

    public LinkedList<Items<BaseSubClass> > getItems() {
        return items;
    }
}
