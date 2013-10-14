package com.mcdawn.shinratensei;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

public class EventListeners implements Listener {
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (ShinraTensei.plugin.shinraTenseiSettings.containsKey(p) && p.isSneaking()) {
			Location loc = p.getLocation();
			int radius = Integer.parseInt(ShinraTensei.plugin.shinraTenseiSettings.get(p)[0]),
					xi = (int)Math.round(loc.getX()) - radius,
					yi = (int)Math.round(loc.getY()) - radius,
					zi = (int)Math.round(loc.getZ()) - radius,
					xf = (int)Math.round(loc.getX()) + radius,
					yf = (int)Math.round(loc.getY()) + radius,
					zf = (int)Math.round(loc.getZ()) + radius;
			if (Boolean.parseBoolean(ShinraTensei.plugin.shinraTenseiSettings.get(p)[1]))
				for (int x = xi; x <= xf; x++)
					for (int y = yi; y <= yf; y++)
						for (int z = zi; z <= zf; z++) {
							Location temp = new Location(p.getWorld(), x, y, z);
							if (Math.round(getDistance(temp, loc)) <= radius)
								new Location(p.getWorld(), x, y, z).getBlock().setType(Material.AIR);
						}
			double velocity = Math.sqrt(radius) * 1.5, // not accurate, just something to make the velocity scale with the radius
					verticalModifier = 1d / 1.33; // just an approx value because setVelocity on y coord seems to be much more "stronger"
			for (Entity e : p.getWorld().getEntities()) {
				Location temp = e.getLocation();
				if (getDistance(loc, temp) <= radius)
					if (!e.equals((Entity)p)) {
						double xa = Math.atan2(temp.getZ() - loc.getZ(), temp.getX() - loc.getX()),
								ya = Math.atan2(temp.getY() - loc.getY(), Math.sqrt(Math.pow(temp.getX() - loc.getX(), 2) + Math.pow(temp.getY() - loc.getY(), 2)));
						e.setVelocity(new Vector(Math.cos(xa) * velocity, Math.sin(ya) * velocity * verticalModifier, Math.sin(xa) * velocity));
					}
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) { remove(event.getPlayer()); }
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) { remove(event.getPlayer()); }
	
	public void remove(Player p) { if (ShinraTensei.plugin.shinraTenseiSettings.containsKey(p)) ShinraTensei.plugin.shinraTenseiSettings.remove(p); }
	
	public double getDistance(Location a, Location b) { return a.getWorld() == b.getWorld() ? Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2) + Math.pow(b.getZ() - a.getZ(), 2)) : Double.MAX_VALUE; }
}
