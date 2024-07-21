package me.maotrade.handler;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

public class EconomyHandler {

    private Economy econ;

    public EconomyHandler(Economy econ) {
        this.econ = econ;
    }

    public boolean withdraw(Player player, double amount) {
        EconomyResponse response = econ.withdrawPlayer(player, amount);
        return response.transactionSuccess();
    }

    public boolean deposit(Player player, double amount) {
        EconomyResponse response = econ.depositPlayer(player, amount);
        return response.transactionSuccess();
    }

    public boolean hasEnough(Player player, double amount) {
        return econ.has(player, amount);
    }
}
