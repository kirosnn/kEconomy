package fr.kirosnn.keconomy.top;

import lombok.Data;

import java.util.UUID;

@Data
public class PlayerBalanceSnapshot {
    private final UUID uuid;
    private double balance;

    public PlayerBalanceSnapshot(UUID uuid, double balance) {
        this.uuid = uuid;
        this.balance = balance;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public double getBalance() {
        return this.balance;
    }
}
