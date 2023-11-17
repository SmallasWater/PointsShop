package net.playershop.windows;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;
import net.playershop.PlayerShop;
import net.playershop.players.BuyItemData;
import net.playershop.players.PlayerFile;
import net.playershop.shop.Items;
import net.playershop.shop.ShopClass;


/**
 * @author 若水
 */
public class CreateWindow {

    static final int MENU = 0xAAB001;
    static final int LIST = 0xAAB002;
    static final int SHOP = 0xAAB003;
    static final int UP = 0xAAB004;

    public static void sendMenu(Player player){
        FormWindowSimple simple = new FormWindowSimple(PlayerShop.getInstance().getTitle(),"");
        for(String name:PlayerShop.getInstance().getShops().keySet()){
            simple.addButton(new ElementButton(name
                    ,new ElementButtonImageData("path",PlayerShop.getInstance().getShops().get(name))));
        }
        player.showFormWindow(simple, MENU);
    }


    static void sendList(Player player){
        ShopClass shopClass = PlayerShop.getInstance().clickShop.get(player);
        FormWindowSimple simple = new FormWindowSimple(PlayerShop.getInstance().getTitle()+" -- "+shopClass.getName(),"");
//        int buyCount = 0;
//        BuyItemData buyItemData;
//        PlayerFile file = PlayerShop.getInstance().getPlayerFileByName(player.getName(),false);
        for(Items items: shopClass.getItems()){
//            if(file != null){
//                buyItemData = file.getBuyItemData(items);
//                if(buyItemData != null){
//                    buyCount = buyItemData.getBuyCount();
//                }
//            }
            simple.addButton(new ElementButton(TextFormat.colorize('&',items.getName())
                    ,new ElementButtonImageData("path",items.getImage())));
        }
        simple.addButton(new ElementButton("返回",new ElementButtonImageData("path","textures/ui/refresh_light")));
        player.showFormWindow(simple, LIST);
    }

    static void sendShop(Player player){
        Items items = PlayerShop.getInstance().click.get(player);
//        PlayerFile file = PlayerShop.getInstance().getPlayerFileByName(player.getName(),true);
        FormWindowSimple simple = new FormWindowSimple(PlayerShop.getInstance().getTitle()+" -- "+items.getLastShop()+" -- "+items.getName(),"");
//        if(file != null){
//            int buyCount = 0;
//            BuyItemData buyItemData = file.getBuyItemData(items);
//            int outDay = 0;
//            if(buyItemData != null){
//                buyCount = buyItemData.getBuyCount();
//                outDay = BuyItemData.getTime(buyItemData.getLastBuyTime());
//            }
//            simple.setContent(TextFormat.colorize('&',"&2购买次数: "+buyCount+" &r/&b"+items.getBuyCount()+" "
//                    +(items.getBuyCount() == buyCount?"&c(到达上限)":"")+"\n\n"+"&7刷新时间: &2"+(items.getResetTime() - outDay)+" 天后刷新\n\n"));
//        }

        simple.setContent(simple.getContent()+items.toString());
        simple.addButton(new ElementButton("购买",new ElementButtonImageData("path","textures/ui/MCoin")));
        simple.addButton(new ElementButton("返回",new ElementButtonImageData("path","textures/ui/refresh_light")));
        player.showFormWindow(simple, SHOP);
    }

    public static void sendUp(Player player){
        FormWindowCustom custom = new FormWindowCustom(PlayerShop.getInstance().getTitle()+"-- 增加物品");
        custom.addElement(new ElementInput("请输入物品名称(自定义)")); //0
        custom.addElement(new ElementInput("请输入物品介绍(自定义)")); //1
        if(PlayerShop.getInstance().canCmd.get(player)){
            custom.addElement(new ElementInput("请输入执行指令 @p 代表玩家 ")); //2
        }else{
            Item item = player.getInventory().getItemInHand();
            custom.addElement(new ElementLabel("增加物品(手持) "+(item.hasCustomName()?item.getCustomName():item.getId()+":"+item.getDamage())+" * "+item.getCount())); //2
        }
        player.showFormWindow(custom,UP);

    }


}
