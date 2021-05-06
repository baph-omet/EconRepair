package net.iamvishnu.EconRepair;

import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public final class EconRepair extends JavaPlugin {
	static final Logger serverLog = Logger.getLogger("Minecraft");
	static Plugin plugin;
	
	static Economy econ = null;
	
	@Override
	public void onEnable() {
		plugin = this;
		saveDefaultConfig();
		getConfig();
		if (!setupEconomy()) {
			serverLog.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
		}
		
		//TODO: Register commands
	}
	
	@Override
	public void onDisable() {
		plugin = null;
	}
	
	private boolean setupEconomy() {
	        if (getServer().getPluginManager().getPlugin("Vault") == null) {
	            return false;
	        }
	        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
	        if (rsp == null) {
	            return false;
	        }
	        econ = rsp.getProvider();
	        return econ != null;
	}
}
