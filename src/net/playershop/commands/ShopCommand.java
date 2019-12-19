package net.playershop.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.utils.TextFormat;
import net.playershop.PlayerShop;
import net.playershop.commands.shop.*;
import net.playershop.windows.CreateWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 若水
 */
public class ShopCommand extends PluginCommand<PlayerShop> {


    private final List<BaseSubCommand> commands = new ArrayList<>();
    private final ConcurrentHashMap<String, Integer> SubCommand = new ConcurrentHashMap<>();



    public ShopCommand(PlayerShop owner) {
        super("pointshop", owner);
        this.setAliases(new String[]{"点券商店","ps"});
        this.setDescription("点券商店主命令");
        this.loadSubCommand(new CreateSubCommand(getPlugin()));
        this.loadSubCommand(new DelSubCommand(getPlugin()));
        this.loadSubCommand(new AddSubCommand(getPlugin()));
        this.loadSubCommand(new RemoveSubCommand(getPlugin()));
        this.loadSubCommand(new UpSubCommand(getPlugin()));
    }


    private void loadSubCommand(BaseSubCommand cmd) {
        commands.add(cmd);
        int commandId = (commands.size()) - 1;
        SubCommand.put(cmd.getName().toLowerCase(), commandId);
        for (String alias : cmd.getAliases()) {
            SubCommand.put(alias.toLowerCase(), commandId);
        }
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!sender.hasPermission("point.shop")) {
            sender.sendMessage(TextFormat.RED+"抱歉，，您没有使用此指令权限");
            return true;
        }
        if(args.length == 0){
            if(sender instanceof Player){
                CreateWindow.sendMenu((Player) sender);
            }else{
                sender.sendMessage("控制台无法显示GUI");
            }
            return true;
        }
        String subCommand = args[0].toLowerCase();
        if (SubCommand.containsKey(subCommand)) {
            BaseSubCommand command = commands.get(SubCommand.get(subCommand));
            boolean canUse = command.canUse(sender);
            if (canUse) {
                return command.execute(sender, args);
            } else {
                return false;
            }
        } else {
            return this.sendHelp(sender, args);
        }
    }
    private boolean sendHelp(CommandSender sender, String[] args) {
        if ("help".equals(args[0])) {
            sender.sendMessage("§a§l >> §eHelp for PointsShop§a<<");
            if(sender.isPlayer()){
                sender.sendMessage(getHelp());
            }
            for(BaseSubCommand subCommand:commands){
                if(subCommand.canUse(sender)){
                    sender.sendMessage(subCommand.getHelp());
                }
            }
            sender.sendMessage("§a§l >> §eHelp for PointsShop §a<<");
        }
        return true;
    }

    private String getHelp() {
        return "§a/ps §7打开点券商店GUI";
    }
}
