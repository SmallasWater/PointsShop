package net.playershop.shop;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.utils.Config;
import net.player.api.Point;
import net.playershop.PlayerShop;
import net.playershop.players.BuyItemData;
import net.playershop.players.PlayerFile;
import net.playershop.utils.LoadMoney;

import java.io.File;
import java.util.Date;
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

//    private int buyCount = 10;
//
//    private int resetTime = 3;


    private LinkedList<T> subClass = new LinkedList<>();


    private LoadMoney loadMoney;

    private String moneyName;



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

    public void setMoneyName(String moneyName) {
        this.moneyName = moneyName;
    }

    public String getMoneyName() {
        return moneyName;
    }

    public LoadMoney getLoadMoney() {
        return loadMoney;
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
//        int buyCount = config.getInt("buyCount",10);
//        int resetTime = config.getInt("resetTime",3);
        Items<BaseSubClass> items = new Items<>(lastName,name,point);
        if(map != null){
            for(Map s:map){
                String n = (String) s.get("name");
                String m = (String) s.get("message");
                Object i = s.get("items");
                if(i instanceof Map){
                    Map item = (Map) i;
                    items.subClass.add(ItemSubClass.loadItem(n,m,item));
                }else {
                    items.subClass.add(new CmdSubClass(n,m,((String) i)));
                }
            }
        }
        items.setImage(config.getString("button-image"));
        items.setFile(file);
        if(!"".equalsIgnoreCase(config.getString("loadMoney"))){
            items.setLoadMoney(PlayerShop.getInstance().loadEconomy(config.getString("loadMoney"),false));
        }else{
            items.setLoadMoney(PlayerShop.getInstance().getLoadMoney());
        }
        if(!"".equalsIgnoreCase(config.getString("moneyName"))){
            items.setMoneyName(config.getString("moneyName"));
        }else{
            items.setMoneyName(PlayerShop.getInstance().getMoneyName());
        }


//        items.setBuyCount(buyCount);
//        items.setResetTime(resetTime);

        return items;
    }

    private void setLoadMoney(LoadMoney loadMoney) {
        this.loadMoney = loadMoney;
    }

    //
//    private void setBuyCount(int buyCount) {
//        this.buyCount = buyCount;
//    }
//
//    private void setResetTime(int resetTime) {
//        this.resetTime = resetTime;
//    }

    private void setImage(String image) {
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

//    public int getBuyCount() {
//        return buyCount;
//    }
//
//    public int getResetTime() {
//        return resetTime;
//    }



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
        save();
    }

    public void setName(String name) {
        this.name = name;
        save();
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
//        config.set("buyCount",buyCount);
//        config.set("resetTime",resetTime);
        LinkedList<Object> items = new LinkedList<>();
        for(BaseSubClass sub:subClass){
            items.add(sub.getConfig());
        }
        config.set("items",items);
        config.save();
    }



    public boolean delete(){
        try {
            if (file.exists()) {
                if (file.delete()) {
                    ShopClass shopClass = ShopClass.getShop(lastShop);
                    if (shopClass != null) {
                        shopClass.removeItems(name);
                    }
                }
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void playerBuy(Player player){
        double point = PlayerShop.getInstance().getLoadMoney().myMoney(player);
        if(point >= this.point){
//            PlayerFile file = PlayerShop.getInstance().getPlayerFileByName(player.getName(),true);
//            if(file.getBuyItemData(this) != null){
//                BuyItemData data = file.getBuyItemData(this);
//                if(data.getBuyCount() >= getBuyCount()){
//                    player.sendMessage("§e["+PlayerShop.getInstance().getTitle()+"] §c 抱歉，您的购买次数已达到上限");
//                    return;
//                }
//            }
            for(BaseSubClass baseSubClass:subClass){
                if(baseSubClass.givePlayer(player)) {
                    player.sendMessage("§e["+PlayerShop.getInstance().getTitle()+"] §a 成功购买  " + name);
                    player.sendMessage("§c获得 §d" + baseSubClass.getName());
                }else{
                    player.sendMessage("§e["+PlayerShop.getInstance().getTitle()+"] §c 购买 " + name+"时 "+baseSubClass.getName()+" 给予失败 请联系管理员解决此问题");
                }
            }
//            file.addBuyCount(this);
            getLoadMoney().reduceMoney(player, this.point);
        }else{
            player.sendMessage("§e["+PlayerShop.getInstance().getTitle()+"] §c 抱歉，您的"+getMoneyName()+"不足..");
        }
    }



    @Override
    public String toString() {
        return "§6名称: "+name+"\n\n"+(subClass.size() > 0?"§a货物: §r\n\n"+subClass.toString():"§a货物: §c暂无")
                .replace("[","").replace(", ","\n")
                .replace("]","")+"\n"+"§b价格: §e"+point+" §6"+ getMoneyName();
    }
}
