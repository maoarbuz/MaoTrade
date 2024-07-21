package me.maotrade.handler;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MessageHandler {

    public void sendSellMessage(Player seller, ItemStack item, double price) {
        String itemName = item.getType().name();
        String sellerName = seller.getName();

        TextComponent message = new TextComponent(sellerName + " is selling " + itemName + " for " + price + " coins. Click here to buy.");
        message.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mtbuy " + sellerName + " " + price));

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(message);
        }
    }
}
