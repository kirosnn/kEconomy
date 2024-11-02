package fr.kirosnn.keconomy.api;

import fr.kirosnn.keconomy.config.MainConfig;
import fr.kirosnn.keconomy.kEconomy;

import java.util.UUID;

public abstract class EconomyHandler {
    protected final kEconomy instance;

    protected EconomyHandler(kEconomy instance) {
        this.instance = instance;
    }

    public abstract boolean hasAccount(UUID uuid);

    public abstract double get(UUID uuid);

    public boolean has(UUID uuid, double amount) {
        return get(uuid) >= amount;
    }

    public abstract boolean set(UUID uuid, double amount);

    public boolean withdraw(UUID uuid, double amount) {
        return set(uuid, get(uuid) - amount);
    }

    public boolean deposit(UUID uuid, double amount) {
        return set(uuid, get(uuid) + amount);
    }

    public abstract boolean createAccount(UUID uuid, double startAmount);

    public boolean createAccount(UUID uuid) {
        return createAccount(uuid, instance.get(MainConfig.class).getStartAmount());
    }

    public boolean deleteAccount(UUID uuid) {
        return false;
    }

    public void disable() {
        // VIDE
    }
}
