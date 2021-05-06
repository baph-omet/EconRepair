package net.iamvishnu.EconRepair;

import net.md_5.bungee.api.ChatColor;

public class Messaging {
	private static String helpCommand = "\"/econrepair help\"";
	private static String helpCommandMessage = String.format("See %s for more info.", helpCommand);
	
	public static String noSuchSubcommand(String argument) {
		return ChatColor.RED + "No such subcommand " + argument + ". " + helpCommandMessage;
	}
	
	public static String noPerms(String node) {
		return ChatColor.RED + "You don't have permission to do that. Required permission: " + node;
	}
	
	public static String playersOnly() {
		return ChatColor.RED + "Only Players can run this command. " + helpCommandMessage;
	}
	
	public static String pluginInfo() {
		return ChatColor.BLUE + "=====" + ChatColor.YELLOW + "EconRepair" + ChatColor.BLUE + "=====\nCreated by IAMVISHNU Media ( " 
				+ EconRepair.plugin.getDescription().getWebsite() + " )\n" + "GitHub: https://github.com/iamvishnu-media/EconRepair\n"
				+ "Version: " + EconRepair.plugin.getDescription().getVersion() + "See " + helpCommand + " for commands.";
	}
	
	public static String invalidItem() {
		return ChatColor.RED + "You cannot repair that item.";
	}
	
	public static String noItemsFound() {
		return ChatColor.RED + "Nothing found to repair.";
	}
	
	public static String cannotAffort(double cost, double balance) {
		return ChatColor.RED + "You cannot afford this repair. Cost: " + cost + " Your balance: " + balance;
	}
	
	public static String repairedReceipt(double cost, String itemNames) {
		return ChatColor.BLUE + "Paid " + ChatColor.YELLOW + cost + ChatColor.BLUE + " to repair: " + ChatColor.YELLOW + itemNames;
	}
}
