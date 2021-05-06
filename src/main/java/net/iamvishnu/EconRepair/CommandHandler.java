package net.iamvishnu.EconRepair;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("repair")) {
			return Repair(sender, args);
		}
		
		
		return true;
	}
	
	private boolean Repair(CommandSender sender, String[] args) {
		
		boolean all = false;
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("all")) {
				
			} else {
				
			}
		}
	}
	
	//private double GetCost()
}
