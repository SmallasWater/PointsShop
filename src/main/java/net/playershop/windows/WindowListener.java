package net.playershop.windows;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.item.Item;
import net.playershop.PlayerShop;
import net.playershop.shop.*;

/**
 * @author 若水
 */
public class WindowListener implements Listener {

    @EventHandler
    public void onPlayerFormResponded(PlayerFormRespondedEvent event) {
        if (event.wasClosed()) {
            return;
        }
        Player player = event.getPlayer();
        switch (event.getFormID()) {
            case CreateWindow.MENU:
                if (!(event.getResponse() instanceof FormResponseSimple)) {
                    return;
                }
                try {
                    int clickedButtonId = ((FormResponseSimple) event.getResponse()).getClickedButtonId();
                    ShopClass shopClass = PlayerShop.getInstance().shop.get(clickedButtonId);
                    if (shopClass != null) {
                        PlayerShop.getInstance().clickShop.put(player, shopClass);
                        CreateWindow.sendList(player);
                    }
                } catch (Exception e) {
                    player.sendMessage("抱歉, 出现异常，请重新打开");
                }
                break;
            case CreateWindow.LIST:
                if (!(event.getResponse() instanceof FormResponseSimple)) {
                    return;
                }
                int clickedButtonId = ((FormResponseSimple) event.getResponse()).getClickedButtonId();
                ShopClass shopClass = PlayerShop.getInstance().clickShop.get(player);
                if (clickedButtonId == shopClass.getItems().size()) {
                    CreateWindow.sendMenu(player);
                } else {
                    try {
                        Items<BaseSubClass> items = shopClass.getItems().get(clickedButtonId);
                        if (items != null) {
                            PlayerShop.getInstance().click.put(player, items);
                            CreateWindow.sendShop(player);
                        }
                    } catch (Exception e) {
                        player.sendMessage("抱歉, 出现异常，请重新打开");
                    }
                }
                break;
            case CreateWindow.SHOP:
                if (!(event.getResponse() instanceof FormResponseSimple)) {
                    return;
                }
                clickedButtonId = ((FormResponseSimple) event.getResponse()).getClickedButtonId();
                Items<BaseSubClass> items = PlayerShop.getInstance().click.get(player);
                if (clickedButtonId == 0) {
                    items.playerBuy(player);
                } else {
                    CreateWindow.sendList(player);
                }
                break;
            case CreateWindow.UP:
                if (!(event.getResponse() instanceof FormResponseCustom)) {
                    return;
                }
                FormResponseCustom response = (FormResponseCustom) event.getResponse();
                String name = response.getInputResponse(0);
                String message = response.getInputResponse(1);
                items = PlayerShop.getInstance().click.get(player);
                if (PlayerShop.getInstance().canCmd.get(player)) {
                    if (items.addSubClass(new CmdSubClass(name, message, response.getInputResponse(2)))) {
                        player.sendMessage("添加成功");
                    } else {
                        player.sendMessage("已存在" + name);
                    }
                } else {
                    Item i = player.getInventory().getItemInHand();
                    if (i.getId() != 0) {
                        if (items.addSubClass(new ItemSubClass(name, message, player.getInventory().getItemInHand()))) {
                            player.sendMessage("添加成功");
                        } else {
                            player.sendMessage("已存在" + name);
                        }
                    } else {
                        player.sendMessage("不能添加空气");
                    }
                }
                break;
            default:
                break;
        }
    }

}
