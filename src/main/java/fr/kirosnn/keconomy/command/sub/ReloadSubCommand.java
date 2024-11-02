package fr.kirosnn.keconomy.command.sub;

import fr.kirosnn.keconomy.Permissions;
import fr.kirosnn.keconomy.config.MainConfig;
import fr.kirosnn.keconomy.config.MessageConfig;
import fr.kirosnn.keconomy.kEconomy;
import io.github.projectunified.minelib.util.subcommand.SubCommand;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ReloadSubCommand extends SubCommand {
    private final kEconomy instance;

    public ReloadSubCommand(kEconomy instance) {
        super("reload", "Recharge le plugin", "/<label> reload", Permissions.RELOAD.getName(), true);
        this.instance = instance;
    }

    @Override
    public void onSubCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        instance.get(MainConfig.class).reloadConfig();
        instance.get(MessageConfig.class).reloadConfig();
        MessageUtils.sendMessage(sender, instance.get(MessageConfig.class).getSuccess());
    }

    @Override
    public boolean isProperUsage(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        return Collections.emptyList();
    }
}
