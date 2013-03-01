package net.alphaatom.vengeance;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Vengeance extends JavaPlugin {
	
	private HashMap<String, Integer> chargedPlayers = new HashMap<String, Integer>();
	private HashMap<String, Integer> extendTime = new HashMap<String, Integer>();
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new VengeanceListener(this), this);
		this.saveDefaultConfig();
	}
	
	public int getVengeanceItem() {
		int vengeanceItem = getConfig().getInt("vengeanceItem", 289);
		if (vengeanceItem == 0) {
			Bukkit.getLogger().log(Level.WARNING, "vengeanceItem is 0, plugin will now disable.");
			this.getPluginLoader().disablePlugin(this);
		}
		return vengeanceItem;
	}
	
	public int getVengeanceTime() {
		int vengeanceTime = getConfig().getInt("vengeanceDuration", 300);
		if (vengeanceTime == 0) {
			Bukkit.getLogger().log(Level.WARNING, "vengeanceTime is 0, plugin will now disable.");
			this.getPluginLoader().disablePlugin(this);
		}
		return vengeanceTime;
	}
	
	public float getExplosionSize() {
		float explosionSize = getConfig().getInt("explosionSize", 4);
		return explosionSize;
	}
	
	public boolean causeDamage() {
		boolean canDamage = getConfig().getBoolean("no-damage", false);
		return !canDamage;
	}
	
	public boolean getStackEffectEnabled() {
		boolean stackEffect = getConfig().getBoolean("consumptions-stack");
		return stackEffect;
	}
	
	public boolean allowSuicideExplosion() {
		boolean allowSuicide = getConfig().getBoolean("allow-explosion-on-suicide");
		return allowSuicide;
	}
	
	public HashMap<String, Integer> getChargedPlayers() {
		return chargedPlayers;
	}
	
	public HashMap<String, Integer> getTimeExtensions() {
		return extendTime;
	}

}
