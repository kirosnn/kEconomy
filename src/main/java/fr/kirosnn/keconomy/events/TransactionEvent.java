package fr.kirosnn.keconomy.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final UUID senderID;
    private final UUID receiverID;
    private final double amount;
    private final TransactionType type;
    private final LocalDateTime timestamp;

    public TransactionEvent(UUID senderID, UUID receiverID, double amount, TransactionType type) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    public UUID getSenderID() {
        return senderID;
    }

    public UUID getReceiverID() {
        return receiverID;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public enum TransactionType {
        GIVE, PAY
    }
}
