package net.playershop.commands.shop;


import cn.nukkit.command.CommandSender;
import net.playershop.PlayerShop;
import net.playershop.commands.BaseSubCommand;
import net.playershop.shop.ShopClass;

public class CreateSubCommand extends BaseSubCommand {

    public CreateSubCommand(PlayerShop plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("point.shop.create");
    }

    @Override
    public String getName() {
        return "创建";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"create"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 1){
            String name = args[1];
            if(ShopClass.createShopClass(name)){
                sender.sendMessage("§a"+name+"§e商城 创建成功");
            }else{
                sender.sendMessage("§c"+name+"商城创建失败");
            }
            return true;
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/ps create <商城名> §7创建商城";
    }
}
