package com.mcdawn.shinratensei;

import java.util.HashMap;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShinraTensei extends JavaPlugin {
	public static ShinraTensei plugin;
	public HashMap<Player, String[]> shinraTenseiSettings;
	
	@Override
	public void onEnable() {
		plugin = this;
		shinraTenseiSettings = new HashMap<Player, String[]>();
		getServer().getPluginManager().registerEvents(new EventListeners(), this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("shinratensei")) {
			if (sender instanceof ConsoleCommandSender) return false;
			Player p = (Player)sender;
			if (!shinraTenseiSettings.containsKey(p)) {
				int defaultRadius = 10, radius = defaultRadius;
				try { radius = args.length > 0 ? Integer.parseInt(args[0]) : defaultRadius; }
				catch (Exception ex) { return false; }
				if (radius <= 0) return false;
				boolean defaultDestroy = false, destroy = defaultDestroy;
				try { destroy = args.length > 1 ? Boolean.parseBoolean(args[1]) : defaultDestroy; }
				catch (Exception ex) { return false; }
				shinraTenseiSettings.put(p, new String[] { Integer.toString(radius), Boolean.toString(destroy) });
				sender.sendMessage("Shinra Tensei mode activated. Press shift and click to use, use command again to deactivate.");
			}
			else {
				shinraTenseiSettings.remove(p);
				sender.sendMessage("Shinra Tensei mode deactivated.");
			}
			return true;
		}
		return false;
	}
}
