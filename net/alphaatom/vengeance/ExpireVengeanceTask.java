package net.alphaatom.vengeance;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class ExpireVengeanceTask implements Runnable {
	
	Player player;
	Vengeance instance;

	public ExpireVengeanceTask(Player _player, Vengeance _instance) {
		player = _player;
		instance = _instance;
	}
	
	public void run() {
		if (instance.getTimeExtensions().containsKey(player.getName()) && instance.getTimeExtensions().get(player.getName()) > 0) {
			instance.getChargedPlayers().remove(player.getName());
			BukkitTask removeCharge = Bukkit.getServer().getScheduler().runTaskLater(instance, new ExpireVengeanceTask(player, instance), instance.getVengeanceTime()*20);
			instance.getChargedPlayers().put(player.getName(), removeCharge.getTaskId());
			instance.getTimeExtensions().put(player.getName(), instance.getTimeExtensions().get(player.getName())-1);
			if (instance.getTimeExtensions().get(player.getName()) == 0) {
				instance.getTimeExtensions().remove(player.getName());
			}
			return;
		} else {
			if (player.isOnline()) {
				player.sendMessage("The explosive rage inside of you fades away...");
			}
			instance.getChargedPlayers().remove(player.getName());
		}
	}

}
