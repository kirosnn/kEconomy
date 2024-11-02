package fr.kirosnn.keconomy.hook.treasury;

import fr.kirosnn.keconomy.config.MainConfig;
import fr.kirosnn.keconomy.kEconomy;
import me.lokka30.treasury.api.common.misc.FutureHelper;
import me.lokka30.treasury.api.economy.account.Account;
import me.lokka30.treasury.api.economy.currency.Currency;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TreasuryCurrency implements Currency {
    private final kEconomy instance;

    public TreasuryCurrency(kEconomy instance) {
        this.instance = instance;
    }

    @Override
    public @NotNull String getIdentifier() {
        return TreasuryEconomyHook.CURRENCY_IDENTIFIER;
    }

    @Override
    public @NotNull String getSymbol() {
        return instance.get(MainConfig.class).getCurrencySymbol();
    }

    @Override
    public char getDecimal(@Nullable Locale locale) {
        return instance.get(MainConfig.class).getActualDecimalPoint();
    }

    @Override
    public @NotNull Map<Locale, Character> getLocaleDecimalMap() {
        return Collections.singletonMap(Locale.getDefault(), instance.get(MainConfig.class).getActualDecimalPoint());
    }

    @Override
    public @NotNull String getDisplayName(@NotNull BigDecimal value, @Nullable Locale locale) {
        if (value.compareTo(BigDecimal.ONE) <= 0) {
            return instance.get(MainConfig.class).getCurrencySingular();
        } else {
            return instance.get(MainConfig.class).getCurrencyPlural();
        }
    }

    @Override
    public int getPrecision() {
        return instance.get(MainConfig.class).getFractionalDigits();
    }

    @Override
    public boolean isPrimary() {
        return true;
    }

    @Override
    public @NotNull BigDecimal getStartingBalance(@NotNull Account account) {
        return BigDecimal.valueOf(instance.get(MainConfig.class).getStartAmount());
    }

    @Override
    public @NotNull BigDecimal getConversionRate() {
        return BigDecimal.ZERO;
    }

    @Override
    public @NotNull CompletableFuture<BigDecimal> parse(@NotNull String formattedAmount, @Nullable Locale locale) {
        StringBuilder valueBuilder = new StringBuilder();
        StringBuilder currencyBuilder = new StringBuilder();

        boolean hadDot = false;
        for (char c : formattedAmount.toCharArray()) {
            if (Character.isWhitespace(c)) {
                continue;
            }

            if (!Character.isDigit(c) && !isSeparator(c, locale)) {
                currencyBuilder.append(c);
            } else if (Character.isDigit(c)) {
                valueBuilder.append(c);
            } else if (isSeparator(c, locale)) {
                if (c == getDecimal(locale)) {
                    boolean nowChanged = false;
                    if (!hadDot) {
                        hadDot = true;
                        nowChanged = true;
                    }

                    if (!nowChanged) {
                        valueBuilder = new StringBuilder();
                        break;
                    }
                }
                valueBuilder.append('.');
            }
        }

        if (currencyBuilder.length() == 0) {
            return FutureHelper.failedFuture(FailureReasons.INVALID_CURRENCY.toException());
        }

        String currency = currencyBuilder.toString();
        if (!matches(currency, locale)) {
            return FutureHelper.failedFuture(FailureReasons.INVALID_CURRENCY.toException());
        }

        if (valueBuilder.length() == 0) {
            return FutureHelper.failedFuture(FailureReasons.INVALID_VALUE.toException());
        }

        try {
            double value = Double.parseDouble(valueBuilder.toString());
            if (value < 0) {
                return FutureHelper.failedFuture(FailureReasons.NEGATIVE_BALANCES_NOT_SUPPORTED.toException());
            }

            return CompletableFuture.completedFuture(BigDecimal.valueOf(value));
        } catch (NumberFormatException e) {
            return FutureHelper.failedFuture(FailureReasons.INVALID_VALUE.toException(e));
        }
    }

    private boolean matches(String currency, @Nullable Locale locale) {
        if (currency.length() == 1) {
            return currency.charAt(0) == getDecimal(locale);
        } else {
            return currency.equalsIgnoreCase(getSymbol())
                    || currency.equalsIgnoreCase(instance.get(MainConfig.class).getCurrencySingular())
                    || currency.equalsIgnoreCase(instance.get(MainConfig.class).getCurrencyPlural());
        }
    }

    private boolean isSeparator(char c, @Nullable Locale locale) {
        return c == getDecimal(locale) || c == instance.get(MainConfig.class).getActualThousandsSeparator();
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal amount, @Nullable Locale locale) {
        return instance.get(MainConfig.class).format(amount);
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal amount, @Nullable Locale locale, int precision) {
        return instance.get(MainConfig.class).format(amount, precision);
    }
}
