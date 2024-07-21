package me.maotrade.cmd;

import me.maotrade.MaoTrade;
import me.maotrade.handler.SellOffer;
import me.maotrade.handler.EconomyHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BuyCommand implements CommandExecutor {

    private MaoTrade plugin;

    public BuyCommand(MaoTrade plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player buyer = (Player) sender;

        if (args.length != 1) {
            buyer.sendMessage(ChatColor.RED + "Usage: /mtbuy <seller>");
            return false;
        }

        String sellerName = args[0];
        Player seller = Bukkit.getPlayer(sellerName);
        if (seller == null) {
            buyer.sendMessage(ChatColor.RED + "Seller not found.");
            return false;
        }

        SellOffer sellOffer = plugin.getSellOffers().get(seller.getUniqueId());
        if (sellOffer == null) {
            buyer.sendMessage(ChatColor.RED + "Seller has no active sell offer.");
            return false;
        }

        ItemStack itemToSell = sellOffer.getItem();
        double price = sellOffer.getPrice();

        EconomyHandler econHandler = plugin.getEconomyHandler();
        if (!econHandler.hasEnough(buyer, price)) {
            buyer.sendMessage(ChatColor.RED + "You do not have enough money.");
            return false;
        }

        if (!econHandler.withdraw(buyer, price) || !econHandler.deposit(seller, price)) {
            buyer.sendMessage(ChatColor.RED + "Transaction failed. Please try again later.");
            econHandler.deposit(buyer, price); // Refund the buyer
            return false;
        }

        seller.getInventory().removeItem(itemToSell);
        buyer.getInventory().addItem(itemToSell);
        plugin.getSellOffers().remove(seller.getUniqueId());

        seller.sendMessage(ChatColor.GREEN + "Your item was bought by " + buyer.getName() + " for " + price + " coins.");
        buyer.sendMessage(ChatColor.GREEN + "You bought " + itemToSell.getType().name() + " from " + seller.getName() + " for " + price + " coins.");

        return true;
    }
}
