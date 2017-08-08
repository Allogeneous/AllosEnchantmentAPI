package me.Allogeneous.AllosEnchantsAPI.listeners.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.Allogeneous.AllosEnchantsAPI.core.AEnchantment;

public class AEnchantmentTabCompleter implements TabCompleter{
	
	private String[] arg1 = new String[]{"help", "list", "id", "anvil", "table", "makebook", "enchant", "add", "disenchant", "remove", "reload", "hooks", "fullreload"};
	private String[] arg2 = new String[]{"safe", "unsafe"};
	private String[] arg3 = new String[]{"<level>"};

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String commandLable, String[] args) {
		
		List<String> tabs;
		
		if(command.getName().equalsIgnoreCase("aenchants")){
			if(args.length == 0){
				return Arrays.asList(arg1);
			}
			
			if(args.length == 1){
				tabs = new ArrayList<>();
				for(String s : arg1){
					if(s.startsWith(args[0])){
						tabs.add(s);
					}
				}
				return tabs;
			}
			
			if(args.length == 2){
				if(args[0].equalsIgnoreCase("makebook") || args[0].equalsIgnoreCase("disenchant")){
					return AEnchantment.storageNames();
				}else if(args[0].equalsIgnoreCase("ids") || args[0].equalsIgnoreCase("list")){
						tabs = new ArrayList<>();
						tabs.add("<Page Number>");
						return tabs;
				}else{
					tabs = new ArrayList<>();
					for(String s : arg2){
						if(s.startsWith(args[1])){
							tabs.add(s);
						}
					}
					return tabs;
				}
			}
			
			if(args.length == 3){
				if(args[1].equalsIgnoreCase("safe") || args[1].equalsIgnoreCase("unsafe")){
					return AEnchantment.storageNames();
				}else{
					return Arrays.asList(arg3);
				}
			}
			
			if(args.length == 4){
				if(AEnchantment.viewRegisteredAEnchantments().contains(AEnchantment.fromSTORAGE_NAME(args[2]))){
					return Arrays.asList(arg3);
				}
			}
		}	
		return null;
	}

}
