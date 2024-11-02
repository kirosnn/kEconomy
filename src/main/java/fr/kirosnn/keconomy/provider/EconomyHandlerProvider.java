package fr.kirosnn.keconomy.provider;

import fr.kirosnn.keconomy.api.EconomyHandler;
import fr.kirosnn.keconomy.config.MainConfig;
import fr.kirosnn.keconomy.handler.FlatFileEconomyHandler;
import fr.kirosnn.keconomy.handler.JsonEconomyHandler;
import fr.kirosnn.keconomy.handler.MySqlEconomyHandler;
import fr.kirosnn.keconomy.handler.SqliteEconomyHandler;
import fr.kirosnn.keconomy.kEconomy;
import io.github.projectunified.minelib.plugin.base.Loadable;
import lombok.Getter;
import me.hsgamer.hscore.builder.Builder;

@Getter
public class EconomyHandlerProvider implements Loadable {
    private final kEconomy instance;
    private final Builder<kEconomy, EconomyHandler> builder = new Builder<>();
    private EconomyHandler economyHandler;

    public EconomyHandlerProvider(kEconomy instance) {
        this.instance = instance;
    }

    @Override
    public void load() {
        builder.register(FlatFileEconomyHandler::new, "flat-file", "flatfile", "file");
        builder.register(MySqlEconomyHandler::new, "mysql");
        builder.register(SqliteEconomyHandler::new, "sqlite");
        builder.register(JsonEconomyHandler::new, "json");
    }

    @Override
    public void enable() {
        economyHandler = builder.build(instance.get(MainConfig.class).getHandlerType(), instance).orElseGet(() -> {
            instance.getLogger().warning("Cannot find an economy handler from the config. FlatFile will be used");
            return new FlatFileEconomyHandler(instance);
        });
    }

    @Override
    public void disable() {
        if (economyHandler != null) {
            economyHandler.disable();
        }
    }

    public EconomyHandler getEconomyHandler() {
        return economyHandler;
    }
}
