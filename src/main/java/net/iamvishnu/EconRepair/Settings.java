package net.iamvishnu.EconRepair;

import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import net.md_5.bungee.api.ChatColor;

public class Settings {
	private final static FileConfiguration Config = EconRepair.plugin.getConfig();
	private final static Logger serverLog = EconRepair.serverLog;

	public static double Cost() {
		final double cost = Config.getDouble("cost", -1D);
		if (cost < 0)
			serverLog.warning("Default cost not specified. Please set a default cost in config.yml.");
		return cost;
	}

	public static double GroupModifier(String group) {
		final ConfigurationSection cs = Config.getConfigurationSection("group_modifiers");
		double multiplier = 1.0;
		if (cs != null && cs.contains(group))
			multiplier = cs.getDouble(group);
		return multiplier;
	}

	public static double EnchantModifier() {
		return Config.getDouble("enchant_modifier");
	}

	public static boolean PerEnchant() {
		return Config.getBoolean("per_enchant");
	}

	public static boolean DoDurabilityWarning() {
		return Config.getBoolean("durability_warning");
	}

	public static String WarningMessage() {
		String msg = Config.getString("durability_message");
		if (msg != null)
			msg = ChatColor.translateAlternateColorCodes('&', msg);
		return msg;
	}

	public static double DurabilityThreshold() {
		return Config.getDouble("durability_threshold");
	}
}
