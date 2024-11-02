package fr.kirosnn.keconomy.command;

import fr.kirosnn.keconomy.Permissions;
import fr.kirosnn.keconomy.command.sub.GiveSubCommand;
import fr.kirosnn.keconomy.command.sub.ReloadSubCommand;
import fr.kirosnn.keconomy.command.sub.SetSubCommand;
import fr.kirosnn.keconomy.command.sub.TakeSubCommand;
import fr.kirosnn.keconomy.kEconomy;
import io.github.projectunified.minelib.util.subcommand.SubCommandManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainCommand extends Command {
    private final SubCommandManager subCommandManager;

    public MainCommand(kEconomy instance) {
        super(instance.getName().toLowerCase(Locale.ROOT), "Commande principale du plugin", "/" + instance.getName().toLowerCase(Locale.ROOT), Collections.singletonList("eco"));
        this.subCommandManager = new SubCommandManager() {
            @Override
            public void sendHelpMessage(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
                if (sender.hasPermission(Permissions.ADMIN)) {
                    super.sendHelpMessage(sender, label, args);
                }
                sendCommand(sender, "/balancetop", "Voir le classement par richesse des joueurs du serveur");
                sendCommand(sender, "/balance", "Obtenir 'largent du joueur");
            }

            private void sendCommand(CommandSender sender, String usage, String description) {
                sender.sendMessage(ChatColor.YELLOW + usage);
                sender.sendMessage(ChatColor.WHITE + "  " + description);
            }
        };
        this.subCommandManager.registerSubcommand(new GiveSubCommand(instance));
        this.subCommandManager.registerSubcommand(new TakeSubCommand(instance));
        this.subCommandManager.registerSubcommand(new SetSubCommand(instance));
        this.subCommandManager.registerSubcommand(new ReloadSubCommand(instance));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        return subCommandManager.onCommand(sender, commandLabel, args);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return subCommandManager.onTabComplete(sender, alias, args);
    }
}
