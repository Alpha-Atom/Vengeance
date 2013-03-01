package net.alphaatom.vengeance;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class VengeanceListener implements Listener {
	
	Vengeance pluginInstance;
	
	public VengeanceListener(Vengeance instance) {
		pluginInstance = instance;
	}

	@EventHandler
	public void consumeExplosionItem(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			int itemIDUsed = event.getItem().getTypeId();
			Player player = event.getPlayer();
			if (itemIDUsed == pluginInstance.getVengeanceItem() && player.hasPermission("vengeance.consume")) {
				ItemStack currentStack = player.getItemInHand();
				if (playerCharged(player) && pluginInstance.getStackEffectEnabled()) {
					if (currentStack.getAmount() > 1) {
						currentStack.setAmount(currentStack.getAmount() - 1);
					} else {
						player.setItemInHand(null);
					}
					if (pluginInstance.getTimeExtensions().containsKey(player.getName())) {
						player.sendMessage("The rage builds inside you...");
						pluginInstance.getTimeExtensions().put(player.getName(), pluginInstance.getTimeExtensions().get(player.getName())+1);
					} else {
						player.sendMessage("The rage builds inside you...");
						pluginInstance.getTimeExtensions().put(player.getName(), 1);
					}
				} else {
					if (currentStack.getAmount() > 1) {
						currentStack.setAmount(currentStack.getAmount() - 1);
					} else {
						player.setItemInHand(null);
					}
					player.sendMessage("You suddenly feel overcome by a feeling of intense rage...");
					setEffect(player, true);
				}
			}
		}
	}
	
	@EventHandler
	public void playerDeathEvent(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (playerCharged(player)) {
			Location location = player.getLocation();
			String deathMessage = event.getDeathMessage() + " and left a huge explosion!";
			DamageCause deathCause = player.getLastDamageCause().getCause();
			if (pluginInstance.allowSuicideExplosion() || (!deathCause.equals(DamageCause.SUICIDE) || !deathCause.equals(DamageCause.FALL))) {
				event.setDeathMessage(deathMessage);
				float explosionSize = pluginInstance.getExplosionSize();
				if (!pluginInstance.causeDamage()) {
					explosionSize = 0F;
				}
				player.getWorld().createExplosion(location, explosionSize);
				setEffect(player, false);
			}
		}
	}
	
	private boolean playerCharged(Player player) {
		if (pluginInstance.getChargedPlayers().containsKey(player.getName())) {
			return true;
		} else {
			return false;
		}
	}
	
	private void setEffect(Player player, boolean apply) {
		if (apply) {
			if (!pluginInstance.getChargedPlayers().containsKey(player.getName())) {
				BukkitTask removeCharge = Bukkit.getServer().getScheduler().runTaskLater(pluginInstance, new ExpireVengeanceTask(player, pluginInstance), pluginInstance.getVengeanceTime()*20);
				pluginInstance.getChargedPlayers().put(player.getName(), removeCharge.getTaskId());
			}
		} else {
			if (pluginInstance.getChargedPlayers().containsKey(player.getName())) {
				Bukkit.getServer().getScheduler().cancelTask(pluginInstance.getChargedPlayers().get(player.getName()));
				pluginInstance.getChargedPlayers().remove(player.getName());
			}
		}
	}
	
}
