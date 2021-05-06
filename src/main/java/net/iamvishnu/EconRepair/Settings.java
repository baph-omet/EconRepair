package net.iamvishnu.EconRepair;

import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class Settings {
	private final static Plugin plugin = EconRepair.plugin;
	private final static Logger serverLog = EconRepair.serverLog;
	
	public static double GetCost() {
		double cost = plugin.getConfig().getDouble("cost", -1D);
		if (cost < 0) serverLog.warning("Default cost not specified. Please set a default cost in config.yml.");
		return cost;
	}
	
	public static double GetModifier(String group) {
		ConfigurationSection cs = plugin.getConfig().getConfigurationSection("group_modifiers");
		double multiplier = 1.0;
		if (cs.contains(group)) multiplier = cs.getDouble(group);
		return multiplier;
	}
}
