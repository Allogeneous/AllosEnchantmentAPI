package me.Allogeneous.AllosEnchantsAPI.listeners.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

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

import me.Allogeneous.AllosEnchantsAPI.GUIs.AnvilGUI;
import me.Allogeneous.AllosEnchantsAPI.core.AEnchantment;
import me.Allogeneous.AllosEnchantsAPI.core.AEnchantmentConfigData;
import me.Allogeneous.AllosEnchantsAPI.core.AllosEnchantsMain;
import me.Allogeneous.AllosEnchantsAPI.exceptions.AEnchantmentInvalidAEnchantmentException;
import me.Allogeneous.AllosEnchantsAPI.exceptions.AEnchantmentLevelException;
import me.Allogeneous.AllosEnchantsAPI.objects.AEnchanter;
import me.Allogeneous.AllosEnchantsAPI.objects.AEnchantmentNullifier;
import me.Allogeneous.AllosEnchantsAPI.utils.AEnchantmentAnvilUtils;
import me.Allogeneous.AllosEnchantsAPI.utils.AEnchantmentUtils;
import net.md_5.bungee.api.ChatColor;

public class AnvilListener implements Listener {
	
public AllosEnchantsMain instance;

    private HashMap<UUID, List<ItemStack>> itemStorage = new HashMap<>();
	
	public AnvilListener(AllosEnchantsMain plugin){
		instance = plugin;
	}
	//Player opens the AEnchantment Anvil Inventory.
	@EventHandler
	public void onPlayerClickAnvil(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getPlayer().isSneaking() && AEnchantmentConfigData.isLoadedEnableAnvilShiftClick() && e.getPlayer().hasPermission("aenchants.interact.anvil")){
			if(e.getClickedBlock().getType() == AEnchantmentConfigData.getAnvilBlock()){
				e.setCancelled(true);
				AnvilGUI.displayGUI(e.getPlayer());
			}
		}
		
	}
	
	//Player puts items into the anvil and clicks to activate.
	@EventHandler
	public void onPlayerInteractAnvil(InventoryClickEvent e){
		if(e.getClickedInventory() == null){
			return;
		}
		Player p = (Player) e.getWhoClicked();
		if(p.getOpenInventory().getTitle().equals("AEnchantments Anvil")){
			if(e.isShiftClick()){
				e.setCancelled(true);
			}
		}
		if(e.getClickedInventory().getName().equals("AEnchantments Anvil")){
			if(e.getSlot() != 10 & e.getSlot() != 11 & e.getSlot() != 13 & e.getSlot() != 16){
				e.setCancelled(true);
			}
			if(e.getSlot() == 13){
				if(e.getInventory().getContents()[10] != null && !e.getInventory().getContents()[10].getType().equals(Material.AIR) && e.getInventory().getContents()[11] != null && !e.getInventory().getContents()[11].getType().equals(Material.AIR)){
						HashMap<AEnchantment, Integer> aenchantments = AEnchantmentAnvilUtils.getApplicableAnvilAEnchantments(p, e.getInventory().getContents()[10], e.getInventory().getContents()[11]);
						if(aenchantments != null){
							ItemStack newItem = new ItemStack(e.getInventory().getContents()[10]);
							int total = 0;
							for (Entry<AEnchantment, Integer> entry : aenchantments.entrySet()) {
								if(entry.getKey() instanceof AEnchantmentNullifier){
									int subtract = 0;
									AEnchantmentNullifier aend = (AEnchantmentNullifier) entry.getKey();
									int left = entry.getValue();
									innerLoop:
									for(AEnchantment ad : aend.getNullifiedEnchantments().nullifiedAEnchantments()){
										if(AEnchantment.hasAEnchantment(e.getInventory().getContents()[10], ad)){
											int level = AEnchantment.getAEnchantmentLevel(e.getInventory().getContents()[10], ad);
											if((level - left) > 0){
												try {
													subtract = level - left;
													total = total + subtract;
													left = 0;
													AEnchantment.addUnsafeAEnchantment(new AEnchanter(p), newItem, ad, (level - entry.getValue()));
												} catch (AEnchantmentLevelException | AEnchantmentInvalidAEnchantmentException e1) {
													return;
												}
											}else{
												try {
													AEnchantment.removeAEnchantment(new AEnchanter(p), newItem, ad);
													left = left - level;
													subtract = level;
													total = total + subtract;
												} catch (AEnchantmentInvalidAEnchantmentException e1) {
													return;
												}
											}
											if(left <= 0){
												break innerLoop;
											}
										}
									}
								}else{
								try {
									int add = 0;
										if(AEnchantment.hasAEnchantment(e.getInventory().getContents()[10], entry.getKey())){
											add = AEnchantment.getAEnchantmentLevel(e.getInventory().getContents()[10], entry.getKey());
										}
								    	AEnchantment.addAEnchantment(new AEnchanter(p), newItem, entry.getKey(), entry.getValue() + add);
								    	total = total + (entry.getValue() + add);
							   		}catch (AEnchantmentLevelException | AEnchantmentInvalidAEnchantmentException e1) {
							   			return;
							   		}
								}
							}
							
							if(!newItem.getItemMeta().equals(e.getInventory().getContents()[10].getItemMeta())){
								updateInfo(p, e.getClickedInventory(), total);
								itemStorage.put(p.getUniqueId(), Arrays.asList(new ItemStack[]{e.getInventory().getContents()[10], e.getInventory().getContents()[11]}));
								e.getInventory().setItem(16, newItem);
								p.updateInventory();
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
				if(e.getInventory().getContents()[10] != null && !e.getInventory().getContents()[10].getType().equals(Material.AIR) && e.getInventory().getContents()[11] != null && !e.getInventory().getContents()[11].getType().equals(Material.AIR) && (AEnchantmentUtils.getAEnchantmentsAmount(itemStorage.get(p.getUniqueId()).get(0)) <= AEnchantmentConfigData.getLoadedtMaxSafeEnchantmentsOnItem()  || AEnchantmentUtils.getAEnchantmentsAmount(e.getInventory().getContents()[16]) <= AEnchantmentUtils.getAEnchantmentsAmount(itemStorage.get(p.getUniqueId()).get(0))) && itemStorage.containsKey(p.getUniqueId()) && p.getLevel() >= cost){
					if(itemStorage.get(p.getUniqueId()).get(0).equals(e.getInventory().getContents()[10]) && itemStorage.get(p.getUniqueId()).get(1).equals(e.getInventory().getContents()[11])){
						if(p.getInventory().firstEmpty() != -1){
							p.setLevel(p.getLevel() - cost);
							p.getInventory().addItem(e.getInventory().getContents()[16]);
						}else{
							p.setLevel(p.getLevel() - cost);
							p.getWorld().dropItemNaturally(p.getLocation(), e.getInventory().getContents()[16]);
						}
						
						e.setCancelled(true);
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
	
	//Returns items inside the anvil when closed.	
	@EventHandler
	public void onPlayerCloseAnvil(InventoryCloseEvent e){
		Player p = (Player) e.getPlayer();
		if(e.getInventory().getName().equals("AEnchantments Anvil")){
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
	
	//Get application cost
	private int getCost(Inventory e){
		ItemMeta im = e.getContents()[14].getItemMeta();
		String cost = im.getLore().get(0).split(" ")[0].substring(2);
		return AEnchantmentUtils.parseInt(cost);
	}
	
	//Update display info
	private void updateInfo(Player p, Inventory e, int total){
		ItemStack newInfo = e.getContents()[14];
		ItemMeta im = e.getContents()[14].getItemMeta();
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.BLUE + "" + (total * 5) + " levels");
		if(p.getLevel() >= (total * 5)){
			lore.add(ChatColor.GREEN + "You can afford this!");
		}else{
			lore.add(ChatColor.RED + "You cannot afford this!");
		}
		im.setLore(lore);
		newInfo.setItemMeta(im);
		e.setItem(14, newInfo);
	}
	
	//Reset display info
	private void resetInfo(Inventory e){
		ItemStack newInfo = e.getContents()[14];
		ItemMeta im = e.getContents()[14].getItemMeta();
		List<String> lore = new ArrayList<>();
		im.setLore(lore);
		newInfo.setItemMeta(im);
		e.setItem(14, newInfo);
	}
}
