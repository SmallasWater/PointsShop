package net.playershop.commands.shop;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import net.playershop.PlayerShop;
import net.playershop.commands.BaseSubCommand;
import net.playershop.shop.ShopClass;

import java.io.File;
import java.util.Map;

/**
 * @author SmallasWater
 * Create on 2021/2/2 12:39
 * Package net.playershop.commands.shop
 */
public class ReloadSubCommand extends BaseSubCommand {
    public ReloadSubCommand(PlayerShop plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("point.shop.reload");
    }

    @Override
    public String getName() {
        return "重新读取";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"reload"};
    }


    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(!new File(PlayerShop.getInstance().getDataFolder()+"/Shops").exists()){
            if(!new File(PlayerShop.getInstance().getDataFolder()+"/Shops").mkdirs()){
                Server.getInstance().getLogger().info("文件夹Shops创建失败");
            }
        }
        if(!new File(PlayerShop.getInstance().getDataFolder()+"/Players").exists()){
            if(!new File(PlayerShop.getInstance().getDataFolder()+"/Players").mkdirs()){
                Server.getInstance().getLogger().info("文件夹Players创建失败");
            }
        }
        PlayerShop.getInstance().saveDefaultConfig();
        PlayerShop.getInstance().reloadConfig();
        for(String name:PlayerShop.getInstance().getShops().keySet()){
            if(!new File(PlayerShop.getInstance().getDataFolder()+"/Shops/"+name).exists()){
                if(!new File(PlayerShop.getInstance().getDataFolder()+"/Shops/"+name).mkdirs()){
                    PlayerShop.getInstance().getLogger().info("创建商城 "+name+"失败");
                }
            }
        }
        PlayerShop.getInstance().shop.clear();
        Config config = PlayerShop.getInstance().getConfig();
        Map map = (Map) config.get("商城");
        for(Object s:map.keySet()){
            PlayerShop.getInstance().shop.add(new ShopClass(s.toString()));
        }

//        PlayerShop.getInstance().playerFiles = PlayerShop.getInstance().getPlayerFiles();
        sender.sendMessage("配置文件重新加载完成");
        return true;
    }

    @Override
    public String getHelp() {
        return "§a/pshop reload §7重新读取配置文件";
    }
}
