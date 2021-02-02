package net.playershop.commands.shop;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import net.playershop.PlayerShop;
import net.playershop.commands.BaseSubCommand;
import net.playershop.shop.BaseSubClass;
import net.playershop.shop.Items;
import net.playershop.shop.ShopClass;

public class DownSubCommand extends BaseSubCommand {
    public DownSubCommand(PlayerShop plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("point.shop.down");
    }

    @Override
    public String getName() {
        return "下架";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"down"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length > 3) {
            String shop = args[1];
            String items = args[2];
            String name = args[3];
            ShopClass shopClass = ShopClass.getShop(shop);
            if(shopClass != null){
                Items<BaseSubClass> items1 = shopClass.getItemByName(items );
                if(items1 != null){
                    for(BaseSubClass subClass:items1.getSubClass()){
                        if(subClass.getName().equals(name)){
                            if(items1.removeSubClass(subClass)){
                                sender.sendMessage("下架货物: "+name+"成功");
                                return true;
                            }else{
                                sender.sendMessage("下架货物: "+name+"失败");
                                return true;
                            }
                        }
                    }
                    sender.sendMessage("不存在货物: "+name);
                    return true;

                }else{
                    sender.sendMessage("不存在货物 "+items);
                }
            }else{
                sender.sendMessage("不存在商城 "+shop);
            }
        }

        return false;
    }

    @Override
    public String getHelp() {
        return "§a/pshop down <商城> <商品名称> <货物名称> §7下架货物";
    }
}
