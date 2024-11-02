package fr.kirosnn.keconomy.handler;

import fr.kirosnn.keconomy.config.MainConfig;
import fr.kirosnn.keconomy.kEconomy;
import me.hsgamer.hscore.database.Setting;
import me.hsgamer.hscore.database.driver.sqlite.SqliteFileDriver;

public class SqliteEconomyHandler extends SqlEconomyHandler {
    public SqliteEconomyHandler(kEconomy instance) {
        super(
                instance,
                Setting.create(new SqliteFileDriver(instance.getDataFolder())).setDatabaseName(instance.get(MainConfig.class).getSqliteDatabaseName())
        );
    }
}
