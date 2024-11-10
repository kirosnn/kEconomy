package fr.kirosnn.keconomy.command;

import fr.kirosnn.keconomy.Permissions;
import fr.kirosnn.keconomy.Utils;
import fr.kirosnn.keconomy.api.EconomyHandler;
import fr.kirosnn.keconomy.config.MainConfig;
import fr.kirosnn.keconomy.config.MessageConfig;
import fr.kirosnn.keconomy.events.TransactionEvent;
import fr.kirosnn.keconomy.kEconomy;
import fr.kirosnn.keconomy.provider.EconomyHandlerProvider;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class PayCommand extends Command {
    private final kEconomy instance;

    public PayCommand(kEconomy instance) {
        super("pay", "Transfer money to the player", "/pay <player> <amount>", Collections.emptyList());
        this.instance = instance;
        setPermission(Permissions.PAY.getName());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!testPermission(sender)) {
            return false;
        }
        if (args.length < 2) {
            MessageUtils.sendMessage(sender, getUsage());
            return false;
        }
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, instance.get(MessageConfig.class).getPlayerOnly());
            return false;
        }

        EconomyHandler economyHandler = instance.get(EconomyHandlerProvider.class).getEconomyHandler();

        Player player = (Player) sender;
        OfflinePlayer receiver = Utils.getOfflinePlayer(args[0]);
        if (receiver == player) {
            MessageUtils.sendMessage(sender, instance.get(MessageConfig.class).getCannotDo());
            return false;
        }
        UUID playerUUID = Utils.getUniqueId(player);
        UUID receiverUUID = Utils.getUniqueId(receiver);
        if (!economyHandler.hasAccount(receiverUUID)) {
            MessageUtils.sendMessage(sender, instance.get(MessageConfig.class).getPlayerNotFound());
            return false;
        }

        Optional<Double> optionalAmount = Validate.getNumber(args[1])
                .map(BigDecimal::doubleValue)
                .filter(value -> value > 0)
                .filter(value -> economyHandler.has(playerUUID, value));
        if (!optionalAmount.isPresent()) {
            MessageUtils.sendMessage(sender, instance.get(MessageConfig.class).getInvalidAmount());
            return false;
        }
        double amount = optionalAmount.get();

        economyHandler.withdraw(playerUUID, amount);
        economyHandler.deposit(receiverUUID, amount);

        TransactionEvent event = new TransactionEvent(playerUUID, receiverUUID, amount, TransactionEvent.TransactionType.PAY);
        instance.getServer().getPluginManager().callEvent(event);

        MessageUtils.sendMessage(sender,
                instance.get(MessageConfig.class).getGiveSuccess()
                        .replace("{balance}", instance.get(MainConfig.class).format(amount))
                        .replace("{name}", Optional.ofNullable(receiver.getName()).orElse(receiverUUID.toString()))
        );

        MessageUtils.sendMessage(receiverUUID,
                instance.get(MessageConfig.class).getReceive()
                        .replace("{balance}", instance.get(MainConfig.class).format(amount))
                        .replace("{name}", player.getName())
        );
        return true;
    }
}
