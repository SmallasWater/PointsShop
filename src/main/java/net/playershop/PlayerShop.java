package net.playershop;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import net.playershop.commands.ShopCommand;
import net.playershop.players.PlayerFile;
import net.playershop.shop.BaseSubClass;
import net.playershop.shop.Items;
import net.playershop.shop.ShopClass;
import net.playershop.utils.LoadMoney;
import net.playershop.windows.WindowListener;
import updata.AutoData;

import java.io.File;
import java.util.*;

/**
 * @author 若水
 */
public class PlayerShop extends PluginBase {
    private static PlayerShop instance;

    private LoadMoney loadMoney;

    public LinkedList<ShopClass> shop = new LinkedList<>();

    public LinkedHashMap<Player,Boolean> canCmd = new LinkedHashMap<>();

    public LinkedHashMap<Player,ShopClass> clickShop = new LinkedHashMap<>();

    public LinkedList<PlayerFile> playerFiles = new LinkedList<>();

    public LinkedHashMap<Player, Items<BaseSubClass>> click = new LinkedHashMap<>();

    @Override
    public void onEnable() {
        shop.clear();
        instance = this;

        this.getLogger().info("点券商店开始加载");
        if(Server.getInstance().getPluginManager().getPlugin("AutoUpData") != null){
            if(AutoData.defaultUpData(this,getFile(),"SmallasWater","PointsShop")){
                return;
            }
        }
        if(!new File(this.getDataFolder()+"/Shops").exists()){
            if(!new File(this.getDataFolder()+"/Shops").mkdirs()){
                Server.getInstance().getLogger().info("文件夹Shops创建失败");
            }
        }
        if(!new File(PlayerShop.getInstance().getDataFolder()+"/Players").exists()){
            if(!new File(PlayerShop.getInstance().getDataFolder()+"/Players").mkdirs()){
                Server.getInstance().getLogger().info("文件夹Players创建失败");
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

//        playerFiles = getPlayerFiles();
        loadMoney = loadEconomy(getConfig().getString("使用经济核心","default"),true);
        this.getServer().getCommandMap().register("PointsShop",new ShopCommand(this));
        this.getServer().getPluginManager().registerEvents(new WindowListener(),this);
//        this.getServer().getScheduler().scheduleRepeatingTask(this,new PluginTask<PlayerShop>(this) {
//            @Override
//            public void onRun(int i) {
//                for(PlayerFile file: playerFiles){
//                    file.onRun();
//                }
//            }
//        },100);
        this.getLogger().info("插件启动完成");
    }

    public LoadMoney getLoadMoney() {
        return loadMoney;
    }

    public LoadMoney loadEconomy(String economy,boolean canSendMessage){
        LoadMoney loadMoney = new LoadMoney();
        if(loadMoney.getMoney() != -1) {
            if(canSendMessage) {
                if ("default".equalsIgnoreCase(economy)) {
                    getLogger().info(getTitle() + "经济核心已启用:" + TextFormat.GREEN + " 自动");
                }
            }
            if ("money".equalsIgnoreCase(economy)) {
                loadMoney.setMoney(LoadMoney.MONEY);
                if(canSendMessage) {
                    getLogger().info(getTitle() + " 经济核心已启用:" + TextFormat.GREEN + " Money");
                }
            }
            if ("playerpoint".equalsIgnoreCase(economy)) {
                loadMoney.setMoney(LoadMoney.PLAYER_POINT);
                if(canSendMessage) {
                    getLogger().info(getTitle() + " 经济核心已启用:" + TextFormat.GREEN + " PlayerPoints");
                }
            } else {
                loadMoney.setMoney(LoadMoney.ECONOMY_API);
                if(canSendMessage) {
                    getLogger().info(getTitle() + " 经济核心已启用:" + TextFormat.GREEN + " EconomyAPI");
                }
            }
        }
        return loadMoney;

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



    public PlayerFile getPlayerFileByName(String playerName,boolean canAdd){
        PlayerFile file;
        if(canAdd){
            if(!playerFiles.contains(new PlayerFile(playerName))){
                playerFiles.add(new PlayerFile(playerName));
            }
        }else {
            if (!playerFiles.contains(new PlayerFile(playerName))) {
                return null;
            }
        }
        file = playerFiles.get(playerFiles.indexOf(new PlayerFile(playerName)));
        return file;
    }

    public String getTitle(){
        return TextFormat.colorize('&',getConfig().getString("GUI标题"));
    }

    public String getMoneyName(){
        return TextFormat.colorize('&',getConfig().getString("货币名称","§e点券§r"));
    }

    /** 获取全部玩家文件*/
    public LinkedList<PlayerFile> getPlayerFiles(){
        LinkedList<File> linkedList = new LinkedList<>();
        LinkedList<PlayerFile> playerNames = new LinkedList<>();
        File dir = new File(this.getDataFolder()+"/Players");
        if(dir.exists()){
            List<File> list = getAllFiles(dir,linkedList);
            for(File names:list){
                int dot = names.getName().lastIndexOf('.');
                if ((dot >-1) && (dot < (names.getName().length()))) {
                    Config config = new Config(names,Config.YAML);
                    playerNames.add(PlayerFile.getPlayerFile(config));
                }
            }
        }
        return playerNames;
    }

    private static List<File> getAllFiles(File dir, List<File> filelist){
        File[] fs = dir.listFiles();
        if(fs != null){
            for (File f : fs) {
                if (f.getAbsolutePath().matches(".*\\.yml$")) {
                    filelist.add(f);
                }
                if (f.isDirectory()) {
                    try {
                        getAllFiles(f, filelist);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return filelist;
    }


}
