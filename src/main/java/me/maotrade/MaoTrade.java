package me.maotrade;

import me.maotrade.cmd.BuyCommand;
import me.maotrade.cmd.SellCommand;
import me.maotrade.handler.EconomyHandler;
import me.maotrade.handler.MessageHandler;
import me.maotrade.handler.SellOffer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MaoTrade extends JavaPlugin {

    private Economy econ;
    private EconomyHandler economyHandler;
    private MessageHandler messageHandler;
    private Map<UUID, SellOffer> sellOffers;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.economyHandler = new EconomyHandler(econ);
        this.messageHandler = new MessageHandler();
        this.sellOffers = new HashMap<>();

        getLogger().info("Registering commands...");
        this.getCommand("mtsell").setExecutor(new SellCommand(this));
        this.getCommand("mtbuy").setExecutor(new BuyCommand(this));
        Bukkit.getPluginManager().registerEvents(new SellCommand(this), this);

        getLogger().info("MaoTrade plugin has been enabled!");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Vault plugin not found!");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().severe("Could not get Vault's Economy provider!");
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public EconomyHandler getEconomyHandler() {
        return economyHandler;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public Map<UUID, SellOffer> getSellOffers() {
        return sellOffers;
    }
}
