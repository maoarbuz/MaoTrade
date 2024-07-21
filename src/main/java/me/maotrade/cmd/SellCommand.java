package me.maotrade.cmd;

import me.maotrade.MaoTrade;
import me.maotrade.handler.SellOffer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class SellCommand implements CommandExecutor, Listener {

    private MaoTrade plugin;

    public SellCommand(MaoTrade plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player seller = (Player) sender;

        if (args.length != 1) {
            seller.sendMessage(ChatColor.RED + "Usage: /mtsell <price>");
            return false;
        }

        double price;
        try {
            price = Double.parseDouble(args[0]);
        } catch (NumberFormatException e) {
            seller.sendMessage(ChatColor.RED + "Invalid price. Please enter a valid number.");
            return false;
        }

        if (price <= 0) {
            seller.sendMessage(ChatColor.RED + "Price must be greater than zero.");
            return false;
        }

        ItemStack itemToSell = seller.getInventory().getItemInMainHand();
        if (itemToSell == null || itemToSell.getType().isAir()) {
            seller.sendMessage(ChatColor.RED + "You are not holding any item.");
            return false;
        }

        plugin.getSellOffers().put(seller.getUniqueId(), new SellOffer(itemToSell, price));

        TextComponent message = new TextComponent(seller.getName() + " is selling " + itemToSell.getAmount() + "x " +
                itemToSell.getType().name() + " for " + price + " coins. Click here to buy.");
        message.setColor(ChatColor.GOLD);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mtbuy " + seller.getName()));

        Bukkit.spigot().broadcast(message);

        return true;
    }
}
