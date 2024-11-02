package fr.kirosnn.keconomy.hook.treasury;

import fr.kirosnn.keconomy.Utils;
import fr.kirosnn.keconomy.api.EconomyHandler;
import fr.kirosnn.keconomy.kEconomy;
import fr.kirosnn.keconomy.provider.EconomyHandlerProvider;
import me.lokka30.treasury.api.common.misc.FutureHelper;
import me.lokka30.treasury.api.economy.account.PlayerAccount;
import me.lokka30.treasury.api.economy.currency.Currency;
import me.lokka30.treasury.api.economy.transaction.EconomyTransaction;
import me.lokka30.treasury.api.economy.transaction.EconomyTransactionType;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class TreasuryAccount implements PlayerAccount {
    private final kEconomy instance;
    private final UUID uuid;

    public TreasuryAccount(kEconomy instance, UUID uuid) {
        this.instance = instance;
        this.uuid = uuid;
    }

    @Override
    public @NotNull UUID identifier() {
        return uuid;
    }

    @Override
    public @NotNull Optional<String> getName() {
        return Optional.ofNullable(Utils.getOfflinePlayer(uuid).getName());
    }

    @Override
    public @NotNull CompletableFuture<BigDecimal> retrieveBalance(@NotNull Currency currency) {
        if (!currency.getIdentifier().equals(TreasuryEconomyHook.CURRENCY_IDENTIFIER)) {
            return FutureHelper.failedFuture(FailureReasons.CURRENCY_NOT_FOUND.toException());
        } else {
            return CompletableFuture.supplyAsync(() -> BigDecimal.valueOf(instance.get(EconomyHandlerProvider.class).getEconomyHandler().get(uuid)));
        }
    }

    @Override
    public @NotNull CompletableFuture<BigDecimal> doTransaction(@NotNull EconomyTransaction economyTransaction) {
        return CompletableFuture.supplyAsync(() -> {
            if (!economyTransaction.getCurrencyId().equals(TreasuryEconomyHook.CURRENCY_IDENTIFIER)) {
                throw FailureReasons.CURRENCY_NOT_FOUND.toException();
            }
            EconomyTransactionType type = economyTransaction.getType();
            BigDecimal amount = economyTransaction.getAmount();
            double amountDouble = amount.doubleValue();
            if (amountDouble < 0) {
                throw FailureReasons.NEGATIVE_BALANCES_NOT_SUPPORTED.toException();
            }
            EconomyHandler economyHandler = instance.get(EconomyHandlerProvider.class).getEconomyHandler();
            boolean status = false;
            if (type == EconomyTransactionType.DEPOSIT) {
                status = economyHandler.deposit(uuid, amountDouble);
            } else if (type == EconomyTransactionType.WITHDRAWAL) {
                status = economyHandler.withdraw(uuid, amountDouble);
            }
            if (!status) {
                throw FailureReasons.NEGATIVE_BALANCES_NOT_SUPPORTED.toException();
            } else {
                return BigDecimal.valueOf(economyHandler.get(uuid));
            }
        });
    }

    @Override
    public @NotNull CompletableFuture<Boolean> deleteAccount() {
        return CompletableFuture.supplyAsync(() -> instance.get(EconomyHandlerProvider.class).getEconomyHandler().deleteAccount(uuid));
    }

    @Override
    public @NotNull CompletableFuture<Collection<String>> retrieveHeldCurrencies() {
        return CompletableFuture.completedFuture(Collections.singletonList(TreasuryEconomyHook.CURRENCY_IDENTIFIER));
    }

    @Override
    public @NotNull CompletableFuture<Collection<EconomyTransaction>> retrieveTransactionHistory(int transactionCount, @NotNull Temporal from, @NotNull Temporal to) {
        return FutureHelper.failedFuture(FailureReasons.FEATURE_NOT_SUPPORTED.toException());
    }
}
