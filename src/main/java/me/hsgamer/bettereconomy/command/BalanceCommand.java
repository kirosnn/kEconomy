package me.hsgamer.bettereconomy.command;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.hsgamer.bettereconomy.Permissions;
import me.hsgamer.bettereconomy.Utils;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class BalanceCommand extends Command {
    private final BetterEconomy instance;

    public BalanceCommand(BetterEconomy instance) {
        super("balance", "Get the balance of a player", "/balance [player]", Collections.singletonList("bal"));
        setPermission(Permissions.BALANCE.getName());
        this.instance = instance;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        OfflinePlayer who;
        if (args.length > 0 && sender.hasPermission(Permissions.BALANCE_OTHERS)) {
            who = Utils.getOfflinePlayer(args[0]);
        } else if (sender instanceof Player) {
            who = (OfflinePlayer) sender;
        } else {
            MessageUtils.sendMessage(sender, instance.getMessageConfig().getPlayerOnly());
            return false;
        }
        if (!instance.getEconomyHandler().hasAccount(who)) {
            MessageUtils.sendMessage(sender, instance.getMessageConfig().getPlayerNotFound());
            return false;
        }
        MessageUtils.sendMessage(sender, instance.getMessageConfig().getBalanceOutput().replace("{balance}", Double.toString(instance.getEconomyHandler().get(who))));
        return true;
    }
}