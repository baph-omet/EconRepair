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
}
