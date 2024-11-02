package fr.kirosnn.keconomy;

import com.google.common.reflect.TypeToken;
import fr.kirosnn.keconomy.command.BalanceCommand;
import fr.kirosnn.keconomy.command.BalanceTopCommand;
import fr.kirosnn.keconomy.command.MainCommand;
import fr.kirosnn.keconomy.command.PayCommand;
import fr.kirosnn.keconomy.config.MainConfig;
import fr.kirosnn.keconomy.config.MessageConfig;
import fr.kirosnn.keconomy.config.converter.StringObjectMapConverter;
import fr.kirosnn.keconomy.hook.HookProvider;
import fr.kirosnn.keconomy.listener.JoinListener;
import fr.kirosnn.keconomy.provider.EconomyHandlerProvider;
import fr.kirosnn.keconomy.top.TopRunnable;
import io.github.projectunified.minelib.plugin.base.BasePlugin;
import io.github.projectunified.minelib.plugin.command.CommandComponent;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.config.annotation.converter.manager.DefaultConverterManager;
import me.hsgamer.hscore.config.proxy.ConfigGenerator;
import org.bstats.bukkit.Metrics;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class kEconomy extends BasePlugin {
    static {
        DefaultConverterManager.registerConverter(new TypeToken<Map<String, Object>>() {
        }.getType(), new StringObjectMapConverter());
    }

    @Override
    protected List<Object> getComponents() {
        return Arrays.asList(
                ConfigGenerator.newInstance(MainConfig.class, new BukkitConfig(this)),
                ConfigGenerator.newInstance(MessageConfig.class, new BukkitConfig(this, "messages.yml")),
                new EconomyHandlerProvider(this),
                new TopRunnable(this),
                new Permissions(this),
                new CommandComponent(this,
                        new BalanceCommand(this),
                        new BalanceTopCommand(this),
                        new MainCommand(this),
                        new PayCommand(this)
                ),
                new JoinListener(this),
                new HookProvider(this)
        );
    }

    @Override
    public void load() {
        MessageUtils.setPrefix(get(MessageConfig.class)::getPrefix);
    }

    @Override
    public void enable() {
        new Metrics(this, 12981);
    }
}
