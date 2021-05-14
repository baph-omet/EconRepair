package net.iamvishnu.EconRepair;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class EventListener implements Listener {
	@EventHandler(priority = EventPriority.MONITOR)
	public void onDamage(PlayerItemDamageEvent e) {
		if (!Settings.DoDurabilityWarning())
			return;

		double threshold = Settings.DurabilityThreshold();
		if (threshold < 0d || threshold > 100d)
			threshold = 10d;

		final ItemStack item = e.getItem();
		final Damageable d = (Damageable) item.getItemMeta();

		final double max = item.getType().getMaxDurability();
		final double currentDamagePct = 100 - (100d * d.getDamage() / max);
		if (currentDamagePct < threshold)
			return;

		final double nextDamagePct = 100 - (100d * (d.getDamage() + e.getDamage()) / max);
		if (nextDamagePct >= threshold && nextDamagePct > 0d)
			return;

		final String itemName = EconRepair.GetItemName(item);
		if (Settings.Debug())
			EconRepair.serverLog.info(String.format("Player: %s Item: %s current: %s next: %s", e.getPlayer().getName(),
					itemName, currentDamagePct, nextDamagePct));
		e.getPlayer().sendMessage(Settings.WarningMessage(itemName));
	}
}
