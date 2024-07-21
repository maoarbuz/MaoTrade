package me.maotrade.handler;

import org.bukkit.inventory.ItemStack;

public class SellOffer {
    private final ItemStack item;
    private final double price;

    public SellOffer(ItemStack item, double price) {
        this.item = item;
        this.price = price;
    }

    public ItemStack getItem() {
        return item;
    }

    public double getPrice() {
        return price;
    }
}
