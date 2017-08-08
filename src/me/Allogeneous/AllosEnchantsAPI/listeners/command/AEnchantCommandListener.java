package me.Allogeneous.AllosEnchantsAPI.listeners.command;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Allogeneous.AllosEnchantsAPI.GUIs.AnvilGUI;
import me.Allogeneous.AllosEnchantsAPI.GUIs.EnchantmentTableGUI;
import me.Allogeneous.AllosEnchantsAPI.core.AEnchantment;
import me.Allogeneous.AllosEnchantsAPI.core.AEnchantmentConfigData;
import me.Allogeneous.AllosEnchantsAPI.core.AllosEnchantsMain;
import me.Allogeneous.AllosEnchantsAPI.exceptions.AEnchantmentInvalidAEnchantmentException;
import me.Allogeneous.AllosEnchantsAPI.exceptions.AEnchantmentLevelException;
import me.Allogeneous.AllosEnchantsAPI.objects.AEnchanter;
import me.Allogeneous.AllosEnchantsAPI.objects.AEnchantmentBook;
import me.Allogeneous.AllosEnchantsAPI.utils.AEnchantmentUtils;
import net.md_5.bungee.api.ChatColor;

public class AEnchantCommandListener implements CommandExecutor{
	
	String tag = ChatColor.translateAlternateColorCodes('&', AllosEnchantsMain.commandTag);

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(commandLabel.equalsIgnoreCase("aenchants") && sender instanceof Player){
			Player p = (Player) sender;
			if(args.length == 0){
				sender.sendMessage(tag + ChatColor.BLUE + "Try /aenchants help");
				return true;
			}
			
			if(args.length >= 1){
				if(args[0].equalsIgnoreCase("help") && sender.hasPermission("aenchants.help")){
					p.sendMessage(ChatColor.GREEN + "=+=+=+=+=+=+=+AEnchantsHelp=+=+=+=+=+=+=+=+");
					p.sendMessage(tag + ChatColor.BLUE + "/aenchants help");
					p.sendMessage(tag + ChatColor.BLUE + "/aenchants list <Page Number>");
					p.sendMessage(tag + ChatColor.BLUE + "/aenchants ids <Page Number>");
					p.sendMessage(tag + ChatColor.BLUE + "/aenchants table");
					p.sendMessage(tag + ChatColor.BLUE + "/aenchants anvil");
					p.sendMessage(tag + ChatColor.BLUE + "/aenchants enchant safe <AEnchantment> <Level>");
					p.sendMessage(tag + ChatColor.BLUE + "/aenchants enchant unsafe <AEnchantment> <Level>");
					p.sendMessage(tag + ChatColor.BLUE + "/aenchants disenchant <AEnchantment>");
					p.sendMessage(tag + ChatColor.BLUE + "/aenchants makebook <AEnchantment> <Level>");
					p.sendMessage(tag + ChatColor.BLUE + "/aenchants hooks");
					p.sendMessage(tag + ChatColor.BLUE + "/aenchants reload");
					p.sendMessage(tag + ChatColor.BLUE + "/aenchants fullreload");
					p.sendMessage(tag + ChatColor.RED + "Warning: /aenchants fullreload will reload the whole server!");
					p.sendMessage(ChatColor.GREEN + "=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=");
					return true;
				}
				
				if(args.length >= 1 && args[0].equalsIgnoreCase("list") && sender.hasPermission("aenchants.list")){
					if(AEnchantment.viewRegisteredAEnchantments().isEmpty()){
						p.sendMessage(tag + ChatColor.BLUE + "No AEnchantments found!");
						return true;
					}
					
					int page = 1;
					if(args.length == 2){
						if(!AEnchantmentUtils.isInt(args[1])){
							p.sendMessage(tag + ChatColor.BLUE + "Please enter a valid page number!");
							return true;
						}else{
							page = Integer.parseInt(args[1]);
						}
					}
					
					int maxPages = (AEnchantment.storageNames().size() / 10) + 1;
					
					if(page <= 0){
						page = 1;
					}
					if(page > maxPages){
						page = maxPages;
					}
					int end = page * 10 - 1;
					if(end >= AEnchantment.storageNames().size()){
						end = AEnchantment.storageNames().size();
					}
					int start = end - 9;
					p.sendMessage(tag + ChatColor.BLUE + "AEnchantments ID Shorcuts Page " + page + " of " + maxPages + ": " + ChatColor.LIGHT_PURPLE + Arrays.toString(AEnchantment.storageNames().subList(start, end).toArray()));
					return true;
				}
				
				if(args[0].equalsIgnoreCase("fullreload") && sender.hasPermission("aenchants.fullreload")){
					Bukkit.getServer().reload();
					p.sendMessage(tag + ChatColor.BLUE + "Server reloaded!");
					return true;
				}
				
				if(args[0].equalsIgnoreCase("anvil") && sender.hasPermission("aenchants.anvil")){
					AnvilGUI.displayGUI(p);
					return true;
				}
					
				if(args[0].equalsIgnoreCase("table") && sender.hasPermission("aenchants.table")){
					EnchantmentTableGUI.displayGUI(p);
					return true;
				}
				
				if(args[0].equalsIgnoreCase("reload") && sender.hasPermission("aenchants.reload")){
					AllosEnchantsMain.instance.reload();
					sender.sendMessage(tag + ChatColor.BLUE + "reloaded!");
					return true;
				}
				
				if(args.length >= 1 && args[0].equalsIgnoreCase("ids") && sender.hasPermission("aenchants.ids")){
					if(AEnchantment.viewRegisteredAEnchantments().isEmpty()){
						p.sendMessage(tag + ChatColor.BLUE + "No AEnchantments found!");
						return true;
					}
					
					int page = 1;
					if(args.length == 2){
						if(!AEnchantmentUtils.isInt(args[1])){
							p.sendMessage(tag + ChatColor.BLUE + "Please enter a valid page number!");
							return true;
						}else{
							page = Integer.parseInt(args[1]);
						}
					}
					
					int maxPages = (AEnchantment.globalIDs().size() / 10) + 1;
					
					if(page <= 0){
						page = 1;
					}
					if(page > maxPages){
						page = maxPages;
					}
					int end = page * 10 - 1;
					if(end >= AEnchantment.globalIDs().size()){
						end = AEnchantment.globalIDs().size();
					}
					int start = end - 9;
					sender.sendMessage(tag + ChatColor.BLUE +  "AEnchantments Global ID Page " + page + " of " + maxPages + ": "  + ChatColor.LIGHT_PURPLE + Arrays.toString(AEnchantment.globalIDs().subList(start, end).toArray()));
					return true;
				}
				
				if(args[0].equalsIgnoreCase("hooks") && sender.hasPermission("aenchants.hooks.list")){
					sender.sendMessage(tag + ChatColor.BLUE + Arrays.toString(AEnchantmentConfigData.viewHooks().toArray()));
					return true;
				}
				
				if(args[0].equalsIgnoreCase("makebook") && sender.hasPermission("aenchants.makebook")){
					if(args.length == 3){
						AEnchantment enchantment;
						int level;
						if(AEnchantment.viewRegisteredAEnchantments().contains(AEnchantment.fromSTORAGE_NAME(args[1]))){
							enchantment = AEnchantment.fromSTORAGE_NAME(args[1]);
						}else if(AEnchantment.viewRegisteredAEnchantments().contains(AEnchantment.fromGlobalID(args[1]))){
							enchantment = AEnchantment.fromGlobalID(args[1]);
						}else{
							p.sendMessage(tag + ChatColor.BLUE + "Invalid enchantment!");
							return true;
						}
						
						try{
							level = AEnchantmentUtils.parseInt(args[2]);
						}catch(Exception e){
							p.sendMessage(tag + ChatColor.BLUE + "Invalid enchantment level!");
							return true;
						}
						AEnchantmentBook book;
						try {
							book = new AEnchantmentBook(new AEnchanter(p), enchantment, level);
						} catch (AEnchantmentInvalidAEnchantmentException | AEnchantmentLevelException e) {
							p.sendMessage(tag + ChatColor.BLUE + "An error has occurred!");
							return true;
						}
						
						if(p.getInventory().firstEmpty() == -1){
							p.sendMessage(tag + ChatColor.BLUE + "Please make room in your inventory!");
							return true;
						}else{
							p.getInventory().addItem(book.getBook());
							return true;
						}
					}
					
				}
				if(sender.hasPermission("aenchants.disenchant")){
					if(args[0].equalsIgnoreCase("disenchant") || args[0].equalsIgnoreCase("remove")){
						if(args.length == 2){
							AEnchantment enchantment;
							if(AEnchantment.viewRegisteredAEnchantments().contains(AEnchantment.fromSTORAGE_NAME(args[1]))){
								enchantment = AEnchantment.fromSTORAGE_NAME(args[1]);
							}else if(AEnchantment.viewRegisteredAEnchantments().contains(AEnchantment.fromGlobalID(args[1]))){
								enchantment = AEnchantment.fromGlobalID(args[1]);
							}else{
								p.sendMessage(tag + ChatColor.BLUE + "Invalid enchantment!");
								return true;
							}
							try {
								AEnchantment.removeAEnchantment(new AEnchanter(p), p.getItemInHand(), enchantment);
								p.sendMessage(tag + ChatColor.BLUE + "Successfully removed " + args[1] + "!");
							} catch (AEnchantmentInvalidAEnchantmentException e) {
								p.sendMessage(tag + ChatColor.BLUE + "An error has occurred!");
								e.printStackTrace();
							}
							return true;
						}
					}
				}
				
				if(sender.hasPermission("aenchants.enchant.general")){
					if(args[0].equalsIgnoreCase("enchant") || args[0].equalsIgnoreCase("add")){
						if(args.length == 4){
							if(args[1].equalsIgnoreCase("safe") && sender.hasPermission("aenchants.enchant.safe")){
								if(p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)){
									return true;
								}
								AEnchantment enchantment;
								int level;
								if(AEnchantment.viewRegisteredAEnchantments().contains(AEnchantment.fromSTORAGE_NAME(args[2]))){
									enchantment = AEnchantment.fromSTORAGE_NAME(args[2]);
								}else if(AEnchantment.viewRegisteredAEnchantments().contains(AEnchantment.fromGlobalID(args[2]))){
									enchantment = AEnchantment.fromGlobalID(args[2]);
								}else{
									p.sendMessage(tag + ChatColor.BLUE + "Invalid enchantment!");
									return true;
								}
							
								try{
									level = AEnchantmentUtils.parseInt(args[3]);
								}catch(Exception e){
									p.sendMessage(tag + ChatColor.BLUE + "Invalid enchantment level!");
									return true;
								}
							
								try {
									AEnchantment.addAEnchantment(new AEnchanter(p), p.getItemInHand(), enchantment, level);
									if(enchantment.isSafeAEnchantment(p.getItemInHand(), level)){
										p.sendMessage(tag + ChatColor.BLUE + "Successfully added " + args[2] + " " + args[3] + "!");
									}else{
										p.sendMessage(tag + ChatColor.BLUE + "Invalid level, must be between 1 and " + enchantment.getSafeLevel());
									}
									return true;
								}catch (AEnchantmentLevelException | AEnchantmentInvalidAEnchantmentException e) {
									p.sendMessage(tag + ChatColor.BLUE + "An error has occurred!");
									return true;
								}
							}
						
						
						if(args[1].equals("unsafe") && sender.hasPermission("aenchants.enchant.unsafe")){
								AEnchantment enchantment;
								int level;
								if(p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)){
									return true;
								}
								if(AEnchantment.viewRegisteredAEnchantments().contains(AEnchantment.fromSTORAGE_NAME(args[2]))){
									enchantment = AEnchantment.fromSTORAGE_NAME(args[2]);
								}else if(AEnchantment.viewRegisteredAEnchantments().contains(AEnchantment.fromGlobalID(args[2]))){
									enchantment = AEnchantment.fromGlobalID(args[2]);
								}else{
									p.sendMessage(tag + ChatColor.BLUE + "Invalid enchantment!");
									return true;
								}
								
								try{
									level = AEnchantmentUtils.parseInt(args[3]);
								}catch(Exception e){
									p.sendMessage(tag + ChatColor.BLUE + "Invalid enchantment level!");
									return true;
								}
								
								try {
									AEnchantment.addUnsafeAEnchantment(new AEnchanter(p), p.getItemInHand(), enchantment, level);
									p.sendMessage(tag + ChatColor.BLUE + "Successfully added " + args[2] + " " + args[3] + "!");
									return true;
								} catch (AEnchantmentLevelException | AEnchantmentInvalidAEnchantmentException e) {
									p.sendMessage(tag + ChatColor.BLUE + "An error has occurred!");

									return true;
								}
						}
					}
				}
			}			
				sender.sendMessage(tag + ChatColor.RED + "You don't have permission to do this or typed an invalid argument!");
			}
		}
		return true;
	}
	

}
