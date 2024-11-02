package fr.kirosnn.keconomy.command;

import fr.kirosnn.keconomy.Permissions;
import fr.kirosnn.keconomy.Utils;
import fr.kirosnn.keconomy.config.MainConfig;
import fr.kirosnn.keconomy.config.MessageConfig;
import fr.kirosnn.keconomy.kEconomy;
import fr.kirosnn.keconomy.provider.EconomyHandlerProvider;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.UUID;

public class BalanceCommand extends Command {
    private final kEconomy instance;

    public BalanceCommand(kEconomy instance) {
        super("balance", "Obtenir l'argent d'un joueur", "/balance [player]", Collections.singletonList("bal"));
        this.instance = instance;
        setPermission(Permissions.BALANCE.getName());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        if (!testPermission(sender)) {
            return false;
        }
        UUID uuid;
        if (args.length > 0 && sender.hasPermission(Permissions.BALANCE_OTHERS)) {
            uuid = Utils.getUniqueId(args[0]);
        } else if (sender instanceof Player) {
            uuid = ((Player) sender).getUniqueId();
        } else {
            MessageUtils.sendMessage(sender, instance.get(MessageConfig.class).getPlayerOnly());
            return false;
        }
        if (!instance.get(EconomyHandlerProvider.class).getEconomyHandler().hasAccount(uuid)) {
            MessageUtils.sendMessage(sender, instance.get(MessageConfig.class).getPlayerNotFound());
            return false;
        }
        MessageUtils.sendMessage(sender, instance.get(MessageConfig.class).getBalanceOutput().replace("{balance}", instance.get(MainConfig.class).format(instance.get(EconomyHandlerProvider.class).getEconomyHandler().get(uuid))));
        return true;
    }
}
