package net.playershop.commands.shop;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import net.playershop.PlayerShop;
import net.playershop.commands.BaseSubCommand;
import net.playershop.shop.BaseSubClass;
import net.playershop.shop.Items;
import net.playershop.shop.ShopClass;
import net.playershop.windows.CreateWindow;

/**
 * @author ZXR
 */
public class UpSubCommand extends BaseSubCommand {

    public UpSubCommand(PlayerShop plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("point.shop.up") && sender.isPlayer();
    }

    @Override
    public String getName() {
        return "上架";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"up"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length > 3) {
            String shop = args[1];
            String items = args[2];
            boolean can = "cmd".equals(args[3]);
            ShopClass shopClass = ShopClass.getShop(shop);
            if(shopClass != null){
                Items<BaseSubClass> items1 = shopClass.getItemByName(items );
               if(items1 != null){
                   PlayerShop.getInstance().canCmd.put((Player) sender,can);
                   PlayerShop.getInstance().click.put((Player) sender,items1);
                   CreateWindow.sendUp((Player) sender);
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
        return "§a/pshop up <商城> <商品名称> <cmd/item> §7上架货物";
    }
}
