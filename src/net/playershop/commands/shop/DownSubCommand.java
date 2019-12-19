package net.playershop.commands.shop;

import cn.nukkit.command.CommandSender;
import net.playershop.PlayerShop;
import net.playershop.commands.BaseSubCommand;

public class DownSubCommand extends BaseSubCommand {
    public DownSubCommand(PlayerShop plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        return false;
    }

    @Override
    public String getHelp() {
        return null;
    }
}
