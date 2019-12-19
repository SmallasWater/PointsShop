package net.playershop.commands.shop;

import cn.nukkit.command.CommandSender;
import net.playershop.PlayerShop;
import net.playershop.commands.BaseSubCommand;
import net.playershop.shop.ShopClass;

/**
 * @author 若水
 */
public class AddSubCommand extends BaseSubCommand {

    public AddSubCommand(PlayerShop plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("point.shop.add");
    }

    @Override
    public String getName() {
        return "添加";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"add"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 2){
            String name = args[1];
            String items = args[2];
            ShopClass shopClass = ShopClass.getShop(name);
            if(shopClass != null){
                if(!shopClass.createItems(items)){
                    sender.sendMessage("§c货物"+items+"已存在..");
                }else{
                    sender.sendMessage("§e货物"+items+"添加成功");
                }
            }else{
                sender.sendMessage("§c不存在商城.."+name);
            }
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/ps add <商城> <商品名> §7将商品添加至商城";
    }
}
