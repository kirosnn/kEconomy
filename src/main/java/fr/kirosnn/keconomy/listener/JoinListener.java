package fr.kirosnn.keconomy.listener;

import fr.kirosnn.keconomy.api.EconomyHandler;
import fr.kirosnn.keconomy.kEconomy;
import fr.kirosnn.keconomy.provider.EconomyHandlerProvider;
import io.github.projectunified.minelib.plugin.listener.ListenerComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class JoinListener implements ListenerComponent {
    private final kEconomy instance;

    public JoinListener(kEconomy instance) {
        this.instance = instance;
    }

    @Override
    public JavaPlugin getPlugin() {
        return instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        EconomyHandler economyHandler = instance.get(EconomyHandlerProvider.class).getEconomyHandler();
        if (!economyHandler.hasAccount(uuid)) {
            economyHandler.createAccount(uuid);
        }
    }
}
