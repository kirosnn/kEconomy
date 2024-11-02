package fr.kirosnn.keconomy.config;

import me.hsgamer.hscore.config.annotation.ConfigPath;

public interface MessageConfig {
    @ConfigPath("prefix")
    default String getPrefix() {
        return "&6&lECONOMIE &r";
    }

    @ConfigPath("player-not-found")
    default String getPlayerNotFound() {
        return "&cLe joueur n'a pas été trouvé";
    }

    @ConfigPath("player-only")
    default String getPlayerOnly() {
        return "&cCette commande est réservé aux joueurs";
    }

    @ConfigPath("empty-player-selector")
    default String getEmptyPlayerSelector() {
        return "&cAucun joueur n'est présent comme argument.";
    }

    @ConfigPath("balance-output")
    default String getBalanceOutput() {
        return "&eArgent: &f{balance}";
    }

    @ConfigPath("balance-top-output")
    default String getBalanceTopOutput() {
        return "&f#{place} &e{name}: &f{balance}";
    }

    @ConfigPath("empty-balance-top")
    default String getEmptyBalanceTop() {
        return "&eLe baltop est vide";
    }

    @ConfigPath("invalid-amount")
    default String getInvalidAmount() {
        return "&cMontant invalide.";
    }

    @ConfigPath("give-success")
    default String getGiveSuccess() {
        return "&aMontant de {balance} donné avec succès à {name}";
    }

    @ConfigPath("receive")
    default String getReceive() {
        return "&aVous avez reçu {balance} de {name}";
    }

    @ConfigPath("take-success")
    default String getTakeSuccess() {
        return "&aMontant de {balance} retiré avec succès de {name}";
    }

    @ConfigPath("set-success")
    default String getSetSuccess() {
        return "&aMontant de {balance} défini avec succès pour {name}";
    }

    @ConfigPath("give-fail")
    default String getGiveFail() {
        return "&cÉchec de la transaction : impossible de donner {balance} à {name}";
    }

    @ConfigPath("take-fail")
    default String getTakeFail() {
        return "&cÉchec de la transaction : impossible de retirer {balance} de {name}";
    }

    @ConfigPath("set-fail")
    default String getSetFail() {
        return "&cÉchec de la transaction : impossible de définir {balance} pour {name}";
    }

    @ConfigPath("success")
    default String getSuccess() {
        return "&aSuccès";
    }

    @ConfigPath("cannot-do")
    default String getCannotDo() {
        return "&cAction impossible";
    }

    void reloadConfig();
}
