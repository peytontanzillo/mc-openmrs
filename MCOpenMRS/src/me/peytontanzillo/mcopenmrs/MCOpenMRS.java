package me.peytontanzillo.mcopenmrs;

import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MCOpenMRS extends JavaPlugin {
	@Override
	public void onEnable() {
		System.out.println("OpenMRS has entered the chat.");
	}

	@Override
	public void onDisable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("mvn")) {
			if (args.length == 1 && args[0].equalsIgnoreCase("openmrs-sdk:run")) {
				sender.sendMessage(ChatColor.GOLD + "INFO: Starting ProtocolHandler [\"http-bio-8080\"]");
			} else {
				sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "BUILD FAILURE");
			}
		} else if (cmd.getName().equalsIgnoreCase("java")) {
			if (args.length == 2 && args[0].equalsIgnoreCase("-jar") && args[1].equalsIgnoreCase(".\\openmrs-standalone.jar")) {
				sender.sendMessage(ChatColor.GOLD + "INFO: Starting ProtocolHandler [\"http-bio-8080\"]");
			} else {
				sender.sendMessage(ChatColor.RED + "Error: Could not find or load main class");
			}
		} else if (cmd.getName().equalsIgnoreCase("snap")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (hasFullGauntlet(player)) {
					Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
					int numberOfPlayers = Bukkit.getOnlinePlayers().size();
					int half = numberOfPlayers / 2;
					while (half > 0) {
						int randomIndex = (int)(Math.random() * (double)(numberOfPlayers));
						Player randomPlayer = Iterables.get(players, randomIndex);
						this.getServer().broadcastMessage(ChatColor.DARK_PURPLE + randomPlayer.getName() + ChatColor.WHITE +  "has been snapped.");
						randomPlayer.kickPlayer("You have been snapped.");
						players.remove(randomPlayer);
						numberOfPlayers--;
						half--;
					}
				}
			}
		}
		return true;
	}

	private boolean hasFullGauntlet(Player player) {
		Set<String> remaining = new HashSet<>();
		remaining.add("gauntlet");
		remaining.add("reality");
		remaining.add("soul");
		remaining.add("mind");
		remaining.add("time");
		remaining.add("space");
		remaining.add("power");
		Inventory i = player.getInventory();
		for (ItemStack items : i.getContents()) {
			if (items != null) {
				if (items.getType().equals(Material.TOTEM_OF_UNDYING)) {
					remaining.remove("gauntlet");
				} else if (items.getType().equals(Material.REDSTONE)) {
					remaining.remove("reality");
				} else if (items.getType().equals(Material.MAGMA_CREAM)) {
					remaining.remove("soul");
				} else if (items.getType().equals(Material.GOLDEN_APPLE)) {
					remaining.remove("mind");
				} else if (items.getType().equals(Material.SLIME_BALL)) {
					remaining.remove("time");
				} else if (items.getType().equals(Material.HEART_OF_THE_SEA)) {
					remaining.remove("space");
				} else if (items.getType().equals(Material.CHORUS_FRUIT)) {
					remaining.remove("power");
				}
			}
		}
		if (remaining.isEmpty()) {
			player.sendRawMessage(ChatColor.GOLD + "You have successfully snapped!");
			this.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "<Thanos> " + ChatColor.LIGHT_PURPLE + "You should've gone for the head...");
			return true;
		} else {
			player.sendRawMessage(getMessageFromRemaining(remaining));
			return false;
		}
	}

	private String getMessageFromRemaining (Set remaining) {
		String start = ChatColor.AQUA + "You still need a";
		if (remaining.contains("gauntlet")) {
			start += "n " + ChatColor.GOLD + "Infinity Gauntlet," + ChatColor.AQUA;
		}
		if (remaining.contains("reality")) {
			start += ChatColor.RED + " Reality Stone," + ChatColor.AQUA;
		}
		if (remaining.contains("soul")) {
			start += ChatColor.GOLD + " Soul Stone," + ChatColor.AQUA;
		}
		if (remaining.contains("mind")) {
			start += ChatColor.YELLOW + " Mind Stone," + ChatColor.AQUA;
		}
		if (remaining.contains("time")) {
			start += ChatColor.GREEN + " Time Stone," + ChatColor.AQUA;
		}
		if (remaining.contains("space")) {
			start += ChatColor.BLUE + " Space Stone," + ChatColor.AQUA;
		}
		if (remaining.contains("power")) {
			start += ChatColor.DARK_PURPLE + " Power Stone," + ChatColor.AQUA;
		}
		start += " to snap.";
		return start;
	}
}
