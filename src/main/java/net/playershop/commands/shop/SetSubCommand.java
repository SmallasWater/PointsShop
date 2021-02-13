package net.playershop.commands.shop;

import cn.nukkit.command.CommandSender;
import net.playershop.PlayerShop;
import net.playershop.commands.BaseSubCommand;
import net.playershop.shop.BaseSubClass;
import net.playershop.shop.Items;
import net.playershop.shop.ShopClass;

/**
 * @author SmallasWater
 */
public class SetSubCommand extends BaseSubCommand {
    public SetSubCommand(PlayerShop plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("point.shop.set");
    }

    @Override
    public String getName() {
        return "设置";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"set"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length > 4) {
            try {
                String shop = args[1];
                String items = args[2];
                boolean can = "价格".equals(args[3]);
                String i = args[4];
                ShopClass shopClass = ShopClass.getShop(shop);
                if(shopClass != null) {
                    Items<BaseSubClass> items1 = shopClass.getItemByName(items );
                    if(items1 != null) {
                        if (can) {
                            sender.sendMessage("修改货物" + items + "价格为 " + i);
                            items1.setPoint(Integer.parseInt(i));
                        } else {
                            sender.sendMessage("修改货物" + items + "名称为 " + i);
                            items1.setName(i);
                        }
                    }else{
                            sender.sendMessage("不存在货物 "+items);
                    }
                }else{
                    sender.sendMessage("不存在商城 "+shop);
                }
            }catch (Exception e){
                return false;
            }
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/pshop set <商城> <商品名称> <价格/名称> <内容>§7设置货物";
    }
}
