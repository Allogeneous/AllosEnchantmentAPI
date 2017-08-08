package me.Allogeneous.AllosEnchantsAPI.listeners.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Allogeneous.AllosEnchantsAPI.GUIs.EnchantmentTableGUI;
import me.Allogeneous.AllosEnchantsAPI.core.AEnchantment;
import me.Allogeneous.AllosEnchantsAPI.core.AEnchantmentConfigData;
import me.Allogeneous.AllosEnchantsAPI.core.AllosEnchantsMain;
import me.Allogeneous.AllosEnchantsAPI.exceptions.AEnchantmentInvalidAEnchantmentException;
import me.Allogeneous.AllosEnchantsAPI.exceptions.AEnchantmentLevelException;
import me.Allogeneous.AllosEnchantsAPI.objects.AEnchanter;
import me.Allogeneous.AllosEnchantsAPI.utils.AEnchantmentTableUtils;
import me.Allogeneous.AllosEnchantsAPI.utils.AEnchantmentUtils;
import net.md_5.bungee.api.ChatColor;

public class EnchantmentTableListener implements Listener {
	
public AllosEnchantsMain instance;
	
	private HashMap<UUID, List<ItemStack>> itemStorage = new HashMap<>();

	public EnchantmentTableListener(AllosEnchantsMain plugin){
		instance = plugin;
	}
	
	//Player opens the AEnchantments Enchantment Table
	@EventHandler
	public void onPlayerClickEnchantmentTable(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getPlayer().isSneaking() && AEnchantmentConfigData.isLoadedEnchantmentTableShiftClick() && e.getPlayer().hasPermission("aenchants.interact.enchantmentTable")){
			if(e.getClickedBlock().getType() == AEnchantmentConfigData.getEnchantmentTableBlock()){
				e.setCancelled(true);
				EnchantmentTableGUI.displayGUI(e.getPlayer());
			}
		}
		
	}
	
	//Player puts items into the enchantment table and clicks activate
	@EventHandler
	public void onPlayerInteractEnchantmentTable(InventoryClickEvent e){
		if(e.getClickedInventory() == null){
			return;
		}
		Player p = (Player) e.getWhoClicked();
		if(p.getOpenInventory().getTitle().equals("AEnchantments Enchantment Table")){
			if(e.isShiftClick()){
				e.setCancelled(true);
			}
		}
		if(e.getClickedInventory().getName().equals("AEnchantments Enchantment Table")){
			if(e.getSlot() != 10 & e.getSlot() != 11 & e.getSlot() != 13 & e.getSlot() != 16){
				e.setCancelled(true);
			}
			if(e.getSlot() == 13){
				if(e.getInventory().getContents()[10] != null && !e.getInventory().getContents()[10].getType().equals(Material.AIR) && e.getInventory().getContents()[11] != null && !e.getInventory().getContents()[11].getType().equals(Material.AIR)){
					if(e.getInventory().getContents()[11].getType() == Material.INK_SACK && e.getInventory().getContents()[11].getData().getData() == 4){
						List<AEnchantment> aenchantments = AEnchantmentTableUtils.getEnchantmentTableWeightedList(AEnchantmentTableUtils.getAllowedTableEnchantments(p, e.getInventory().getContents()[10]));
						if(e.getInventory().getContents()[10].getType() == Material.BOOK && e.getInventory().getContents()[10].getAmount() == 1){
							List<AEnchantment> all = new ArrayList<>(AEnchantment.viewRegisteredAEnchantments());
							aenchantments = AEnchantmentTableUtils.getEnchantmentTableWeightedList(all);
						}
						if(aenchantments != null && !aenchantments.isEmpty() && !AEnchantment.isAEnchanted(e.getInventory().getContents()[10])){
							ItemStack newItem;
							ItemStack displayItem;
							if(e.getInventory().getContents()[10].getType() == Material.BOOK){
								newItem = new ItemStack(Material.ENCHANTED_BOOK);
								displayItem = new ItemStack(Material.ENCHANTED_BOOK);
							}else{
								newItem = new ItemStack(e.getInventory().getContents()[10]);
								displayItem = new ItemStack(e.getInventory().getContents()[10]);
							}
							List<AEnchantment> aenchantsToApply = new ArrayList<>();
							int total = 0;
							if(aenchantments.size() < 3){
								int it = 0;
								for (AEnchantment entry : aenchantments) {
									if(it < aenchantments.size()){
										aenchantsToApply.add(entry);
									}else{
										break;
									}
									it++;
								}
							}else{
								int it = 0;
								for (AEnchantment entry : aenchantments) {
									if(it < 3){
										aenchantsToApply.add(entry);
									}else{
										break;
									}
									it++;
								}
							}
							
							Random amount = new Random();
							if((e.getInventory().getContents()[11].getAmount() == 1 && p.getLevel() >= 5 ) || (e.getInventory().getContents()[11].getAmount() >= 1 && p.getLevel() >= 5 && p.getLevel() < 15)){
								int enchs = amount.nextInt(2) + 1;
								total = 1;
								int it = 0;
								int lvl = 0;
								for (AEnchantment enchToApply : aenchantsToApply) {
									if(it >= enchs){
										break;
									}
									lvl = split3(enchToApply.getSafeLevel())[0];
									try {
										AEnchantment.addUnsafeAEnchantment(new AEnchanter(p), newItem, enchToApply, lvl);
										ItemMeta im = displayItem.getItemMeta();
										List<String> loreLines = new ArrayList<>();
										loreLines.add(enchToApply.getDisplayName() +  " " + AEnchantmentUtils.romanNumeralTranslator(lvl) + "...");
										im.setLore(loreLines);
										displayItem.setItemMeta(im);
									}catch (AEnchantmentLevelException | AEnchantmentInvalidAEnchantmentException e1) {}
									it++;
								}
							}else if((e.getInventory().getContents()[11].getAmount() == 2 && p.getLevel() >= 15) || (e.getInventory().getContents()[11].getAmount() >= 2 && p.getLevel() >= 15 && p.getLevel() < 30)){
								int enchs = amount.nextInt(3) + 1;
								total = 2;
								int it = 0;
								int lvl = 0;
								for (AEnchantment enchToApply : aenchantsToApply) {
									if(it >= enchs){
										break;
									}
									lvl = split3(enchToApply.getSafeLevel())[1];
									try {
										AEnchantment.addUnsafeAEnchantment(new AEnchanter(p), newItem, enchToApply, lvl);
										if(it == 0){
											ItemMeta im = displayItem.getItemMeta();
											List<String> loreLines = new ArrayList<>();
											loreLines.add(enchToApply.getDisplayName() +  " " + AEnchantmentUtils.romanNumeralTranslator(lvl) + "...");
											im.setLore(loreLines);
											displayItem.setItemMeta(im);
										}
									}catch (AEnchantmentLevelException | AEnchantmentInvalidAEnchantmentException e1) {}
									it++;
								}
								}else if(e.getInventory().getContents()[11].getAmount() >= 3 && p.getLevel() >= 30){
									int enchs = amount.nextInt(2) + 2;
									total = 3;
									int it = 0;
									int lvl = 0;
									for (AEnchantment enchToApply : aenchantsToApply) {
										if(it >= enchs){
											break;
										}
										lvl = split3(enchToApply.getSafeLevel())[2];
										try {
											AEnchantment.addUnsafeAEnchantment(new AEnchanter(p), newItem, enchToApply, lvl);
											if(it == 0){
												ItemMeta im = displayItem.getItemMeta();
												List<String> loreLines = new ArrayList<>();
												loreLines.add(enchToApply.getDisplayName() +  " " + AEnchantmentUtils.romanNumeralTranslator(lvl) + "...");
												im.setLore(loreLines);
												displayItem.setItemMeta(im);
											}
										}catch (AEnchantmentLevelException | AEnchantmentInvalidAEnchantmentException e1) {}
										it++;
									}
								}
							
							if(!newItem.getItemMeta().equals(e.getInventory().getContents()[10].getItemMeta())){
									updateInfo(p, e.getClickedInventory(), total);
									itemStorage.put(p.getUniqueId(), Arrays.asList(new ItemStack[]{e.getInventory().getContents()[10], e.getInventory().getContents()[11], newItem}));
									e.getInventory().setItem(16, displayItem);
									p.updateInventory();
							}
						}	
					}
				}
				
				e.setCancelled(true);
			}
			
			if(e.getSlot() == 16){
				if(e.getInventory().getContents()[16] != null && !e.getInventory().getContents()[16].getType().equals(Material.AIR)){	
				}else{
					e.setCancelled(true);
					return;
				}
				try{
				int cost = getCost(e.getClickedInventory());
				if(e.getInventory().getContents()[10] != null && !e.getInventory().getContents()[10].getType().equals(Material.AIR) && e.getInventory().getContents()[11] != null && !e.getInventory().getContents()[11].getType().equals(Material.AIR) && p.getLevel() >= cost){
					if(itemStorage.containsKey(p.getUniqueId()) && itemStorage.get(p.getUniqueId()).get(0).equals(e.getInventory().getContents()[10]) && itemStorage.get(p.getUniqueId()).get(1).equals(e.getInventory().getContents()[11])){
						if(p.getInventory().firstEmpty() != -1){
							p.setLevel(p.getLevel() - cost);
							p.getInventory().addItem(itemStorage.get(p.getUniqueId()).get(2));
						}else{
							p.setLevel(p.getLevel() - cost);
							p.getWorld().dropItemNaturally(p.getLocation(), itemStorage.get(p.getUniqueId()).get(2));
						}
						
						e.setCancelled(true);
						ItemStack drops = e.getInventory().getItem(11);
						drops.setAmount(drops.getAmount() - cost);
						if(drops.getAmount() > 0){
							if(p.getInventory().firstEmpty() != -1){
								p.getInventory().addItem(drops);
							}else{
								p.setLevel(p.getLevel() - cost);
								p.getWorld().dropItemNaturally(p.getLocation(), drops);
							}
						}
						e.getInventory().setItem(10, new ItemStack(Material.AIR));
						e.getInventory().setItem(11, new ItemStack(Material.AIR));
						p.closeInventory();
						itemStorage.remove(p.getUniqueId());
					}
				}
				e.setCancelled(true);
				resetInfo(e.getClickedInventory());
				e.getInventory().setItem(16, new ItemStack(Material.AIR));
				p.updateInventory();
				}catch(NullPointerException npe){
					e.setCancelled(true);
					return;
				}
			}
				
		}
			
		
	}
	
	//Returns items when player closes the inventory
	@EventHandler
	public void onPlayerCloseEnchantmentTable(InventoryCloseEvent e){
		if(e.getInventory().getName().equals("AEnchantments Enchantment Table")){
			Player p = (Player) e.getPlayer();
			if(e.getInventory().getContents()[10] != null && !e.getInventory().getContents()[10].getType().equals(Material.AIR)){
				if(p.getInventory().firstEmpty() != -1){
					p.getInventory().addItem(e.getInventory().getContents()[10]);
				}else{
					p.getWorld().dropItemNaturally(p.getLocation(), e.getInventory().getContents()[10]);
				}
			}
			
			if(e.getInventory().getContents()[11] != null && !e.getInventory().getContents()[11].getType().equals(Material.AIR)){
				if(p.getInventory().firstEmpty() != -1){
					p.getInventory().addItem(e.getInventory().getContents()[11]);
				}else{
					p.getWorld().dropItemNaturally(p.getLocation(), e.getInventory().getContents()[11]);
				}
			}
			itemStorage.remove(p.getUniqueId());
		}
	}
	
	//Updates the displayed cost info
	private void updateInfo(Player p, Inventory e, int total){
		ItemStack newInfo = e.getContents()[14];
		ItemMeta im = e.getContents()[14].getItemMeta();
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.BLUE + "" + total + " levels");
		if(p.getLevel() >= total){
			lore.add(ChatColor.GREEN + "You can afford this!");
		}else{
			lore.add(ChatColor.RED + "You cannot afford this!");
		}
		im.setLore(lore);
		newInfo.setItemMeta(im);
		e.setItem(14, newInfo);
	}
	
	//Gets enchantment cost
	private int getCost(Inventory e){
		ItemMeta im = e.getContents()[14].getItemMeta();
		String cost = im.getLore().get(0).split(" ")[0].substring(2);
		return AEnchantmentUtils.parseInt(cost);
	}
	
	//Resets enchantment info
	private void resetInfo(Inventory e){
		ItemStack newInfo = e.getContents()[14];
		ItemMeta im = e.getContents()[14].getItemMeta();
		List<String> lore = new ArrayList<>();
		im.setLore(lore);
		newInfo.setItemMeta(im);
		e.setItem(14, newInfo);
	}
	
	//Used for calculating enchantment amount
	private int[] split3(int i){
		if(i == 1){
			return new int[]{i,i,i};
		}
		if(i == 2){
			return new int[]{i-1, i, i};
		}
		if(i == 3){
			int[] chances = new int[3];
			int r = (i / 2);
			for(int n = 0; n < 3; n++){
				if(n == 0){
					chances[n] = ThreadLocalRandom.current().nextInt(1, r + 2);
				}
				if(n == 1){
					chances[n] = ThreadLocalRandom.current().nextInt(r, r + 2);
				}
				if(n == 2){
					chances[n] = ThreadLocalRandom.current().nextInt(r + 1, i + 1);
				}
			}
			return chances;
		}
		if(i > 3){
			int[] chances = new int[3];
			int r = (i / 3);
			for(int n = 0; n < 3; n++){
				if(n == 0){
					chances[n] = ThreadLocalRandom.current().nextInt(1, r + 2);
				}
				if(n == 1){
					chances[n] = ThreadLocalRandom.current().nextInt(r + 1, r + r + 2);
				}
				if(n == 2){
					chances[n] = ThreadLocalRandom.current().nextInt(r + r + 1, i + 1);
				}
			}
			return chances;
		}
		return null;
	}
}
