package fr.kirosnn.keconomy.hook;

import fr.kirosnn.keconomy.hook.placeholderapi.EconomyPlaceholder;
import fr.kirosnn.keconomy.hook.treasury.TreasuryEconomyHook;
import fr.kirosnn.keconomy.hook.vault.VaultEconomyHook;
import fr.kirosnn.keconomy.kEconomy;
import io.github.projectunified.minelib.plugin.base.Loadable;
import me.lokka30.treasury.api.common.service.ServiceRegistry;
import me.lokka30.treasury.api.economy.EconomyProvider;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import java.util.ArrayList;
import java.util.List;

public class HookProvider implements Loadable {
    private final kEconomy instance;
    private final List<Runnable> disableTasks = new ArrayList<>();

    public HookProvider(kEconomy instance) {
        this.instance = instance;
    }

    @Override
    public void load() {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            Bukkit.getServicesManager().register(
                    Economy.class,
                    new VaultEconomyHook(instance),
                    instance,
                    ServicePriority.High
            );
        }
        if (Bukkit.getPluginManager().getPlugin("Treasury") != null) {
            ServiceRegistry.INSTANCE.registerService(
                    EconomyProvider.class,
                    new TreasuryEconomyHook(instance),
                    instance.getName(),
                    me.lokka30.treasury.api.common.service.ServicePriority.NORMAL
            );
        }
    }

    @Override
    public void enable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            EconomyPlaceholder economyPlaceholder = new EconomyPlaceholder(instance);
            economyPlaceholder.register();
            disableTasks.add(economyPlaceholder::unregister);
        }
    }

    @Override
    public void disable() {
        disableTasks.forEach(Runnable::run);
    }
}
