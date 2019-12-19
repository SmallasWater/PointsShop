package net.playershop.commands.shop;

import cn.nukkit.command.CommandSender;
import net.playershop.PlayerShop;
import net.playershop.commands.BaseSubCommand;
import net.playershop.shop.ShopClass;

/**
 * @author 若水
 */
public class DelSubCommand extends BaseSubCommand {
    public DelSubCommand(PlayerShop plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("point.shop.del");
    }

    @Override
    public String getName() {
        return "删除";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"del"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 1){
            String name = args[1];
            ShopClass shopClass = ShopClass.getShop(name);
            if(shopClass != null){
                if(shopClass.delete()){
                    sender.sendMessage("§a"+name+"§e商城 删除成功");
                }else{
                    sender.sendMessage("§c"+name+"商城删除失败");
                }
            }else{
                sender.sendMessage("§c"+name+"商城不存在..");
            }
            return true;
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/ps del <商城> §7删除商城 (商品也会随之消失)";
    }
}
