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

		final short max = item.getType().getMaxDurability();
		final double currentDamagePct = d.getDamage() / max * 100d;
		if (currentDamagePct < threshold)
			return;

		final double nextDamagePct = (d.getDamage() - e.getDamage()) / max * 100d;
		if (nextDamagePct <= threshold && nextDamagePct > 0d)
			return;

		e.getPlayer().sendMessage(Settings.WarningMessage());
	}
}
