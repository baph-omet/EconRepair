package net.iamvishnu.EconRepair;

import java.util.logging.Logger;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public final class EconRepair extends JavaPlugin {
	static final Logger serverLog = Logger.getLogger("Minecraft");
	static Plugin plugin;

	static Economy econ = null;
	static Permission perms = null;

	@Override
	public void onEnable() {
		plugin = this;
		saveDefaultConfig();
		getConfig();

		if (!setupEconomy()) {
			serverLog.severe(
					String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		if (!setupPermissions()) {
			serverLog.warning(
					"Couldn't get permissions hook from Vault. Make sure you are using a Permissions plugin that supports Vault.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		final CommandHandler commander = new CommandHandler();
		getCommand("econrepair").setExecutor(commander);
		getCommand("repair").setExecutor(commander);

		getServer().getPluginManager().registerEvents(new EventListener(), plugin);

		serverLog.info("EconRepair started successfully.");
	}

	@Override
	public void onDisable() {
		plugin = null;
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null)
			return false;

		final RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		econ = rsp.getProvider();
		return econ != null;
	}

	private boolean setupPermissions() {
		if (getServer().getPluginManager().getPlugin("Vault") == null)
			return false;

		final RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager()
				.getRegistration(Permission.class);
		perms = rsp.getProvider();
		return perms != null;
	}

	public static String GetItemName(ItemStack item) {
		final ItemMeta meta = item.getItemMeta();
		if (meta.hasDisplayName())
			return meta.getDisplayName();
		return item.getType().name();
	}
}
