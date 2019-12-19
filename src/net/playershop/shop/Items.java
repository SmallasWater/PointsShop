package net.playershop.shop;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.utils.Config;
import net.player.api.Point;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author 若水
 */
public class Items<T extends BaseSubClass> {

    private String name;

    private double point;

    private File file;

    private String image;

    private String lastShop;

    private LinkedList<T> subClass = new LinkedList<>();

    Items(String last,String name){
        this(last,name,0.0D);
    }

    private Items(String last,String name,double point){
        this.name = name;
        this.point = point;
        this.lastShop = last;
    }

    public String getLastShop() {
        return lastShop;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Items){
            return name.equals(((Items) obj).getName());
        }
        return false;
    }



    private void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    static Items<BaseSubClass>  loadFile(String lastName,File file){
        Config config = new Config(file,Config.YAML);
        String name = config.getString("name");
        double point = config.getDouble("point");
        List<Map> map = config.getMapList("items");
        Items<BaseSubClass> items = new Items<>(lastName,name,point);
        if(map != null){
            for(Map s:map){
                String n = (String) s.get("name");
                String m = (String) s.get("message");
                Object i = s.get("item");
                if(i instanceof String){
                    items.addSubClass(new CmdSubClass(n,m,((String) i)));
                }else{
                    Map item = (Map) i;
                    items.addSubClass(ItemSubClass.loadItem(n,m,item));
                }
            }
        }
        items.setImage(config.getString("button-image"));
        items.setFile(file);
        return items;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public boolean addSubClass(T subclass){
        if(this.subClass.contains(subclass)){
            return false;
        }
        this.subClass.add(subclass);
        this.save();
        return true;
    }

    public boolean removeSubClass(T subclass){
        if(!this.subClass.contains(subclass)){
            return false;
        }
        this.subClass.remove(subclass);
        this.save();
        return true;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public double getPoint() {
        return point;
    }

    public String getName() {
        return name;
    }

    public LinkedList<T> getSubClass() {
        return subClass;
    }

    private void save(){
        Config config = new Config(file,Config.YAML);
        config.set("name",name);
        config.set("point",point);
        config.set("button-image",image);
        LinkedList<Object> items = new LinkedList<>();
        for(BaseSubClass sub:subClass){
            items.add(sub.getConfig());
        }
        config.set("items",items);
        config.save();
    }



    public boolean delete(){
        if(file.exists()){
            if(file.delete()){
                ShopClass shopClass = ShopClass.getShop(lastShop);
                if(shopClass != null){
                    shopClass.removeItems(name);
                }
            }
        }
        return false;
    }

    public void playerBuy(Player player){
        double point = Point.myPoint(player);
        if(point >= this.point){
            Point.reducePoint(player,this.point);
            player.sendMessage("§e[点券商店] §a 成功购买  "+name);
            for(BaseSubClass baseSubClass:subClass){
                player.sendMessage("§c获得 §d"+baseSubClass.getName());
                if(baseSubClass.isItemClass()){
                    player.getInventory().addItem(baseSubClass.getItem());
                }else if(baseSubClass.isCommandClass()){
                    Server.getInstance().dispatchCommand(new ConsoleCommandSender(),baseSubClass.getCmd().replace("@p",player.getName()));
                }
            }
        }else{
            player.sendMessage("§e[点券商店] §c 抱歉，您的点券不足..");
        }
    }



    @Override
    public String toString() {
        return "§6名称: "+name+"\n"+(subClass.size() > 0?"§a货物: §r\n"+subClass.toString():"§a货物: §c暂无")
                .replace("[","").replace(",","\n")
                .replace("]","")+"\n"+"§b价格: §e"+point+" §6"+ Point.getPointName();
    }
}
