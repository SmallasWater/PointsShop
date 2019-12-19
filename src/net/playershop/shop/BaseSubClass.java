package net.playershop.shop;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.item.Item;

import java.util.LinkedHashMap;

/**
 * @author 若水
 */
public abstract class BaseSubClass{

    public BaseSubClass(String name,String message){
        this.name = name;
        this.message = message;
    }

    private String name;


    protected String message;

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 保存
     * @return 内容
     */
    public LinkedHashMap<String,Object> getConfig(){
        LinkedHashMap<String,Object> save = new LinkedHashMap<>();
        save.put("name",name);
        save.put("message",message);
        return save;
    }

    public boolean isCommandClass(){
        return false;
    }

    public String getCmd(){
        return null;
    }

    public Item getItem(){
        return null;
    }

    public boolean isItemClass(){
        return false;
    }

    public void givePlayer(Player player){
        if(isCommandClass()){
            Server.getInstance().dispatchCommand(new ConsoleCommandSender(),getCmd());
        }else if(isItemClass()){
            player.getInventory().addItem(getItem());
        }
    }

    @Override
    public String toString() {
        return name+" > "+message+"\n";
    }
}
