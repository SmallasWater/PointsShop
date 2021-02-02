package net.playershop.windows;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.ModalFormResponsePacket;
import net.playershop.PlayerShop;
import net.playershop.Tools;
import net.playershop.shop.*;

/**
 * @author 若水
 */
public class WindowListener implements Listener {

    @EventHandler
    public void getUI(DataPacketReceiveEvent event){
        String data;
        ModalFormResponsePacket ui;
        Player player = event.getPlayer();
        if((event.getPacket() instanceof ModalFormResponsePacket)){
            ui = (ModalFormResponsePacket)event.getPacket();
            data = ui.data.trim();
            int fromId = ui.formId;
            switch (fromId){
                case CreateWindow.MENU:
                    if("null".equals(data)){
                        return;
                    }else {
                        try {
                            ShopClass shopClass = PlayerShop.getInstance().shop.get(Integer.parseInt(data));
                            if(shopClass != null){
                                PlayerShop.getInstance().clickShop.put(player,shopClass);
                                CreateWindow.sendList(player);
                            }
                        }catch (Exception e){
                            player.sendMessage("抱歉, 出现异常，请重新打开");
                        }
                    }
                    break;
                case CreateWindow.LIST:
                    if("null".equals(data)){
                        return;
                    }else {
                        ShopClass shopClass = PlayerShop.getInstance().clickShop.get(player);
                        if (Integer.parseInt(data) == shopClass.getItems().size()) {
                            CreateWindow.sendMenu(player);
                        } else {
                            try {
                                Items<BaseSubClass>  items = shopClass.getItems().get(Integer.parseInt(data));
                                if (items != null) {
                                    PlayerShop.getInstance().click.put(player, items);
                                    CreateWindow.sendShop(player);
                                }
                            } catch (Exception e) {
                                player.sendMessage("抱歉, 出现异常，请重新打开");
                            }
                        }
                    }
                    break;
                case CreateWindow.SHOP:
                    if("null".equals(data)){
                        return;
                    }else {
                        Items items = PlayerShop.getInstance().click.get(player);
                        if(Integer.parseInt(data) == 0){
                            items.playerBuy(player);
                        }else{
                            CreateWindow.sendList(player);
                        }
                    }
                    break;
                case CreateWindow.UP:
                    if("null".equals(data)) {
                        return;
                    }else{
                        Object[] datas = Tools.decodeData(data);
                        if(datas == null || datas.length < 1){
                            return;
                        }
                        String name = (String) datas[0];
                        String message = (String) datas[1];
                        Items<BaseSubClass> items = PlayerShop.getInstance().click.get(player);
                        if(PlayerShop.getInstance().canCmd.get(player)){
                            if(items.addSubClass(new CmdSubClass(name,message, (String) datas[2]))){
                                player.sendMessage("添加成功");
                            }else{
                                player.sendMessage("已存在"+name);
                            }
                        }else{
                            Item i = player.getInventory().getItemInHand();
                            if(i.getId() != 0){
                                if(items.addSubClass(new ItemSubClass(name,message,player.getInventory().getItemInHand()))){
                                    player.sendMessage("添加成功");
                                }else{
                                    player.sendMessage("已存在"+name);
                                }
                            }else{
                                player.sendMessage("不能添加空气");
                            }
                        }
                    }
                    break;
                    default:break;
            }
        }
    }







}
