package net.iamvishnu.EconRepair;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandHandler implements CommandExecutor {

	private double GetCost(Player player, ArrayList<ItemStack> items) {
		double damage = 0D;
		for (final ItemStack it : items) {
			final ItemMeta im = it.getItemMeta();
			final Damageable d = (Damageable) im;
			double itemDamage = d.getDamage();
			if (im.hasEnchants()) {
				final double mod = Settings.EnchantModifier();
				if (Settings.PerEnchant())
					itemDamage *= Math.pow(mod, im.getEnchants().size());
				else
					itemDamage *= mod;
			}

			damage += itemDamage;
		}

		double cost = Settings.Cost();
		if (cost < 0)
			return -1;

		cost *= damage;

		for (final String group : EconRepair.perms.getPlayerGroups(player))
			cost *= Settings.GroupModifier(group);

		return cost;
	}

	private String GetQuote(Player player, ArrayList<ItemStack> items) {
		final StringBuilder builder = new StringBuilder(ChatColor.BLUE + "EconRepair Quote:");
		for (final ItemStack it : items) {
			String name = it.getType().name();
			if (it.hasItemMeta() && it.getItemMeta().hasDisplayName())
				name = it.getItemMeta().getDisplayName();
			final ArrayList<ItemStack> single = new ArrayList<ItemStack>();
			single.add(it);
			builder.append("\n" + ChatColor.BLUE + name + ": " + ChatColor.YELLOW + GetCost(player, single) + "");
		}

		builder.append("\n" + ChatColor.BLUE + "Total: " + ChatColor.YELLOW + GetCost(player, items));

		return builder.toString();
	}

	private ArrayList<ItemStack> GetRepairableItems(Player player) {
		final ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		// Loop over all items in inventory to get all damageable items.
		for (int i = 0; i < player.getInventory().getSize(); i++) {
			final ItemStack item = player.getInventory().getItem(i);
			if (item == null || !(item.getItemMeta() instanceof Damageable))
				continue;
			items.add(item);
		}

		return items;
	}

	private void Help(CommandSender sender) {
		sender.sendMessage(Messaging.helpInfo());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (label.toLowerCase()) {
		case "repair":
			Repair(sender, args);
			break;
		case "er":
		case "ecr":
		case "econrepair":
			if (args.length > 0)
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
			else
				sender.sendMessage(Messaging.pluginInfo());
			break;
		}

		return true;
	}

	private void Quote(CommandSender sender) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Messaging.playersOnly());
			return;
		}

		final String perm = "econrepair.quote";
		if (!sender.hasPermission(perm)) {
			sender.sendMessage(Messaging.noPerms(perm));
			return;
		}

		final Player player = (Player) sender;

		final ArrayList<ItemStack> items = GetRepairableItems(player);
		if (items.size() == 0) {
			sender.sendMessage(Messaging.noItemsFound());
			return;
		}

		sender.sendMessage(GetQuote(player, items));
	}

	private void Reload(CommandSender sender) {
		final String perm = "econrepair.reload";
		if (!sender.hasPermission(perm)) {
			sender.sendMessage(Messaging.noPerms(perm));
			return;
		}

		EconRepair.plugin.reloadConfig();
		sender.sendMessage(ChatColor.BLUE + "EconRepair config reloaded.");
	}

	private void Repair(CommandSender sender, String[] args) {
		if (args.length > 0 && args[0].equalsIgnoreCase("quote")) {
			Quote(sender);
			return;
		}

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
			final String arg = args[0];
			if (arg.equalsIgnoreCase("all")) {
				perm = "econrepair.repair.all";
				if (sender.hasPermission(perm))
					all = true;
				else {
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
		final Player player = (Player) sender;

		if (all)
			items = GetRepairableItems(player);
		else {
			// Get item in player's hand
			final ItemStack item = player.getInventory().getItemInMainHand();
			if (!(item.getItemMeta() instanceof Damageable)) {
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
		final double cost = GetCost(player, items);

		final double bal = EconRepair.econ.getBalance(player);
		if (bal < cost) {
			sender.sendMessage(Messaging.cannotAffort(cost, bal));
			return;
		}

		EconRepair.econ.withdrawPlayer(player, cost);
		final String itemsRepaired = RepairItems(items);
		sender.sendMessage(Messaging.repairedReceipt(cost, itemsRepaired));
	}

	private String RepairItems(ArrayList<ItemStack> items) {
		final ArrayList<String> itemNames = new ArrayList<String>();
		for (final ItemStack is : items) {
			final ItemMeta im = is.getItemMeta();
			if (im.hasDisplayName())
				itemNames.add(im.getDisplayName());
			else
				itemNames.add(is.getType().name());
			((Damageable) im).setDamage(0);
			is.setItemMeta(im);
		}

		return String.join(",", itemNames);
	}
}
