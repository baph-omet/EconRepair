package net.iamvishnu.EconRepair;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class CommandHandler implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (label.toLowerCase()) {
			case "repair":
				Repair(sender,args);
				break;
			case "econrepair":
				if (args.length > 0) {
					switch (args[0].toLowerCase()) {
						case "reload":
							Reload(sender);
							break;
						case "help":
							Help(sender);
							break;
						default:
							sender.sendMessage(Messaging.noSuchSubcommand(args[0]));
							break;
					}
				} else {
					sender.sendMessage(Messaging.pluginInfo());
				}
				break;
		}
		
		return true;
	}
	
	private void Repair(CommandSender sender, String[] args) {
		// Disallow non-players to use this command.
		if (!(sender instanceof Player)) {
			sender.sendMessage(Messaging.playersOnly());
			return;
		}
		
		// Check to make sure sender has permission to repair.
		String perm = "econrepair.repair";
		if (!sender.hasPermission(perm)) {
			sender.sendMessage(Messaging.noPerms(perm));
			return;
		}
		
		// Check if player has specified to repair one or all items.
		boolean all = false;
		if (args.length > 0) {
			String arg = args[0];
			if (arg.equalsIgnoreCase("all")) {
				perm = "econrepair.repair.all";
				if (sender.hasPermission(perm)) {
					all = true;
				} else {
					sender.sendMessage(Messaging.noPerms(perm));
					return;
				}
			} else {
				sender.sendMessage(Messaging.noSuchSubcommand(arg));
				return;
			}
		}
		
		// Make a list of items to be repaired.
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		Player player = (Player)sender;
		
		if (all) {
			// Loop over all items in inventory to get all damageable items.
			for (int i = 0; i < player.getInventory().getSize(); i++) {
				ItemStack item = player.getInventory().getItem(i);
				if (item == null || !(item instanceof Damageable)) continue;
				items.add(item);
			}
		} else {
			// Get item in player's hand
			ItemStack item = player.getInventory().getItemInMainHand();
			if (!(item instanceof Damageable)) {
				sender.sendMessage(Messaging.invalidItem());
				return;
			}
			
			items.add(item);
		}
		
		// Make sure at least one item is selected.
		if (items.size() == 0) {
			sender.sendMessage(Messaging.noItemsFound());
			return;
		}
		
		// Get cost
		double cost = GetCost(player, items);
		
		double bal = EconRepair.econ.getBalance(player);
		if (bal < cost) {
			sender.sendMessage(Messaging.cannotAffort(cost, bal));
			return;
		}
		
		EconRepair.econ.withdrawPlayer(player, cost);
		String itemsRepaired = RepairItems(items);
		sender.sendMessage(Messaging.repairedReceipt(cost, itemsRepaired));
	}
	
	private void Reload(CommandSender sender) {
		String perm = "econrepair.reload";
		if (!sender.hasPermission(perm)) {
			sender.sendMessage(Messaging.noPerms(perm));
			return;
		}
		
		EconRepair.plugin.reloadConfig();
		sender.sendMessage(ChatColor.BLUE + "EconRepair config reloaded.");
	}
	
	private void Help(CommandSender sender) {
		sender.sendMessage(Messaging.helpInfo());
	}

	private double GetCost(Player player, ArrayList<ItemStack> items) {
		double damage = 0D;
		for (ItemStack it : items) {
			Damageable d = (Damageable)it;
			double itemDamage = d.getDamage();
			if (it.getItemMeta().hasEnchants()) {
				double mod = Settings.EnchantModifier();
				if (Settings.PerEnchant()) itemDamage *= Math.pow(mod, it.getItemMeta().getEnchants().size());
				else itemDamage *= mod;
			}
			
			damage += itemDamage;
		}
		
		double cost = Settings.Cost();
		if (cost < 0) return -1;
		
		cost *= damage;
		
		for (String group : EconRepair.perms.getPlayerGroups(player)) cost *= Settings.GroupModifier(group);
		
		return cost;
	}
	
	private String RepairItems(ArrayList<ItemStack> items) {
		ArrayList<String> itemNames = new ArrayList<String>();
		for (ItemStack is : items) {
			itemNames.add(is.getItemMeta().getDisplayName());
			((Damageable)is).setDamage(0);
		}
		
		return String.join(",", itemNames);
	}
}
