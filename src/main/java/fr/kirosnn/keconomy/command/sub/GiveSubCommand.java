package fr.kirosnn.keconomy.command.sub;

import fr.kirosnn.keconomy.Utils;
import fr.kirosnn.keconomy.config.MainConfig;
import fr.kirosnn.keconomy.config.MessageConfig;
import fr.kirosnn.keconomy.kEconomy;
import fr.kirosnn.keconomy.provider.EconomyHandlerProvider;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class GiveSubCommand extends ChangeMoneySubCommand {
    public GiveSubCommand(kEconomy instance) {
        super(instance, "give", "Donner de l'argent à un joueur", "/<label> give <joueur> <quantitée>");
    }

    @Override
    protected boolean tryChange(CommandSender sender, OfflinePlayer offlinePlayer, double amount) {
        return instance.get(EconomyHandlerProvider.class).getEconomyHandler().deposit(Utils.getUniqueId(offlinePlayer), amount);
    }

    @Override
    protected void sendSuccessMessage(CommandSender sender, OfflinePlayer offlinePlayer, double amount) {
        MessageUtils.sendMessage(sender,
                instance.get(MessageConfig.class).getGiveSuccess()
                        .replace("{balance}", instance.get(MainConfig.class).format(amount))
                        .replace("{name}", Optional.ofNullable(offlinePlayer.getName()).orElse(Utils.getUniqueId(offlinePlayer).toString()))
        );
    }

    @Override
    protected void sendFailMessage(CommandSender sender, OfflinePlayer offlinePlayer, double amount) {
        MessageUtils.sendMessage(sender,
                instance.get(MessageConfig.class).getGiveFail()
                        .replace("{balance}", instance.get(MainConfig.class).format(amount))
                        .replace("{name}", Optional.ofNullable(offlinePlayer.getName()).orElse(Utils.getUniqueId(offlinePlayer).toString()))
        );
    }
}
