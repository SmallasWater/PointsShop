package net.playershop.shop;


import java.util.LinkedHashMap;

/**
 * @author 若水
 */
public class CmdSubClass extends BaseSubClass{
    private String cmd;


    public CmdSubClass(String name, String message, String cmd) {
        super(name,message);
        this.cmd = cmd;
    }

    @Override
    public boolean isCommandClass() {
        return true;
    }

    @Override
    public LinkedHashMap<String, Object> getConfig() {
        LinkedHashMap<String,Object> save =  super.getConfig();
        save.put("items",cmd);
        return save;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CmdSubClass){
            return getName().equals(((CmdSubClass) obj).getName())
                    && getMessage().equals(((CmdSubClass) obj).getMessage());
        }
        return false;
    }


    @Override
    public String getCmd() {
        return cmd;
    }
}
