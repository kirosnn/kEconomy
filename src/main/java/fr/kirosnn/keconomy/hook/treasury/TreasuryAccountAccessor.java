package fr.kirosnn.keconomy.hook.treasury;

import fr.kirosnn.keconomy.kEconomy;
import me.lokka30.treasury.api.economy.account.accessor.AccountAccessor;
import me.lokka30.treasury.api.economy.account.accessor.NonPlayerAccountAccessor;
import me.lokka30.treasury.api.economy.account.accessor.PlayerAccountAccessor;
import org.jetbrains.annotations.NotNull;

public class TreasuryAccountAccessor implements AccountAccessor {
    private final kEconomy instance;

    public TreasuryAccountAccessor(kEconomy instance) {
        this.instance = instance;
    }

    @Override
    public @NotNull PlayerAccountAccessor player() {
        return new TreasuryPlayerAccountAccessor(instance);
    }

    @Override
    public @NotNull NonPlayerAccountAccessor nonPlayer() {
        return new TreasuryNonPlayerAccountAccessor();
    }
}
