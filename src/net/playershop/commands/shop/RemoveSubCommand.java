package net.playershop.commands.shop;

import cn.nukkit.command.CommandSender;

import net.playershop.PlayerShop;
import net.playershop.commands.BaseSubCommand;
import net.playershop.shop.ShopClass;


/**
 * @author 若水
 */
public class RemoveSubCommand extends BaseSubCommand {
    public RemoveSubCommand(PlayerShop plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("point.shop.remove");
    }

    @Override
    public String getName() {
        return "移除";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"remove"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 2){
            String name = args[1];
            String items = args[2];
            ShopClass shopClass = ShopClass.getShop(name);
            if(shopClass != null){
                if(!shopClass.removeItems(items)){
                    sender.sendMessage("§c货物"+items+"移除失败");
                }else{
                    sender.sendMessage("§e货物"+items+"移除成功");
                }
            }else{
                sender.sendMessage("§c不存在商城.."+name);
            }
        }

        return false;
    }

    @Override
    public String getHelp() {
        return "§a/ps remove <商城> <商品名称> §7从商城中删除商品";
    }
}
