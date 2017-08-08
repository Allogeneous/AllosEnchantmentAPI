package me.Allogeneous.AllosEnchantsAPI.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Allogeneous.AllosEnchantsAPI.events.AEnchantmentDisenchantEvent;
import me.Allogeneous.AllosEnchantsAPI.events.AEnchantmentEnchantEvent;
import me.Allogeneous.AllosEnchantsAPI.exceptions.AEnchantmentInvalidAEnchantmentException;
import me.Allogeneous.AllosEnchantsAPI.exceptions.AEnchantmentInvalidGlobalIDException;
import me.Allogeneous.AllosEnchantsAPI.exceptions.AEnchantmentLevelException;
import me.Allogeneous.AllosEnchantsAPI.exceptions.AEnchantmentNameException;
import me.Allogeneous.AllosEnchantsAPI.objects.AEnchanter;
import me.Allogeneous.AllosEnchantsAPI.objects.AEnchantmentConflictPool;
import me.Allogeneous.AllosEnchantsAPI.utils.AEnchantmentUtils;

public class AEnchantment implements Comparable<AEnchantment>{
	
	protected static List<AEnchantment> registeredAEnchantments = new ArrayList<>();
	
	private String globalID;
	private final String localID;
	private String STORAGE_NAME;
	private String displayName;
	private Material[] safeMaterials;
	private int safeLevel;
	private AEnchantmentConflictPool conflicts;
	private String permission;
	private float baseEffectOdds;
	private float enchantmentTableWeight;
	private final AEnchantmentPlugin host;
	
	/**
	 * Base AEnchantment object.
	 * 
	 * @param globalID
	 * @param STORAGE_NAME
	 * @param displayName
	 * @param safeMaterials
	 * @param safeLevel
	 * @param conflicts
	 * @param permission
	 * @param baseEffectOdds
	 * @param enchantmentTableWeight
	 * @param host
	 */
	public AEnchantment(String globalID, String STORAGE_NAME, String displayName, Material[] safeMaterials, int safeLevel, AEnchantmentConflictPool conflicts, String permission, float baseEffectOdds, float enchantmentTableWeight, AEnchantmentPlugin host){
		
		localID = globalID;
		
		if(!host.isGenerateGlobalIDs()){
			this.globalID = host.getName() + "." + globalID;
		}
		
		this.STORAGE_NAME = STORAGE_NAME;
		if(displayName == null || displayName == ""){
			this.displayName = localID;
		}else{
			this.displayName = displayName;
		}
			
		this.safeMaterials = safeMaterials;
		this.safeLevel = safeLevel;
		this.conflicts = conflicts;
		this.permission = permission;
		
		if(baseEffectOdds < 0){
			baseEffectOdds = 0;
		}
		this.baseEffectOdds = baseEffectOdds;
		if(enchantmentTableWeight <= 0){
			if(AEnchantmentConfigData.isNo0Weights()){
				enchantmentTableWeight = 0.00000001f;
			}else{
				enchantmentTableWeight = 0.0f;
			}
			
		}
		if(enchantmentTableWeight > 1){
			enchantmentTableWeight = 1;
		}
		this.enchantmentTableWeight = enchantmentTableWeight;
		this.host = host;
	}
	
	//Getters and setters.
	
	public String getSTORAGE_NAME() {
		return STORAGE_NAME;
	}

	public void setSTORAGE_NAME(String sTORAGE_NAME) {
		STORAGE_NAME = sTORAGE_NAME;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Material[] getSafeMaterials() {
		return safeMaterials;
	}

	public void setSafeMaterials(Material[] safeMaterials) {
		this.safeMaterials = safeMaterials;
	}

	public int getSafeLevel() {
		return safeLevel;
	}

	public void setSafeLevel(int safeLevel) {
		this.safeLevel = safeLevel;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public float getBaseEffectOdds() {
		return baseEffectOdds;
	}

	public void setBaseEffectOdds(float baseEffectOdds) {
		this.baseEffectOdds = baseEffectOdds;
	}

	public float getEnchantmentTableWeight() {
		return enchantmentTableWeight;
	}

	public void setEnchantmentTableWeight(float enchantmentTableWeight) {
		this.enchantmentTableWeight = enchantmentTableWeight;
	}

	public AEnchantmentConflictPool getConflicts() {
		return conflicts;
	}

	public void setConflicts(AEnchantmentConflictPool conflicts) {
		this.conflicts = conflicts;
	}
	
	public String getGlobalID() {
		return globalID;
	}
	
	private void setGlobalID(String globalID){
		this.globalID = globalID;
	}
	
	public String getLocalID() {
		return localID;
	}
	
	public AEnchantmentPlugin getHost() {
		return host;
	}
	
	/**
	 * Returns true if the given the AEnchantment is no in any existing AEnchantmentConflictPools of the AEnchantments currently on the 
	 * item, if the ItemStack is a safe Material as specified in the constructor, and the level is less than or equal to the 
	 * specified safe level in the AEnchantment's constructor.
	 * 
	 * @param item
	 * @param level
	 * @return
	 */
	public boolean isSafeAEnchantment(ItemStack item, int level){
		for(AEnchantment c : AEnchantmentUtils.getAppliedAEnchantments(item)){
			if(getConflicts().contains(c)){
				return false;
			}
		}
		
		return level <= getSafeLevel() && containsSafeMaterial(getSafeMaterials(), item.getType());
	}

	/**
	 * Returns true if the specified player has permission to use the AEnchantment or the no permission permission is being used or
	 * if the config file specifies otherwise.
	 * 
	 * @param player
	 * @return
	 */
	public boolean hasPermission(Player player){
			if(player.hasPermission(getPermission()) || getPermission().equals(AEnchantmentConfigData.NO_PERMISSION) || AEnchantmentConfigData.isLoadedCanEnchantItemsWithNoEnchantmentUsePermission()){
				return true;
			}
		return false;
	}

	/**
	 * This will fix any duplicate storage names for other AEnchantmentPlugins when the AEnchantment is loaded.
	 */
	public void resolveDuplicateStorageNames(){
		for(AEnchantment ench : registeredAEnchantments){
			if(ench.getSTORAGE_NAME().split(":")[0].equals(getSTORAGE_NAME())){
				if(ench.getSTORAGE_NAME().contains(":")){
					setSTORAGE_NAME(getSTORAGE_NAME() + ":" + getGlobalID());
				}else{
					ench.setSTORAGE_NAME(ench.getSTORAGE_NAME() + ":" + ench.getGlobalID());
					setSTORAGE_NAME(getSTORAGE_NAME() + ":" + getGlobalID());
				}
			}
		}
	}
	
	//Here are a few basic canUse methods, it is very easy to add your own variant of these.
	
	/**
	 * Returns true if the item has the AEnchantment and the item type is not an ENCHANTED_BOOK.
	 * 
	 * @param item
	 * @return
	 */
	public boolean canUse(ItemStack item){
		return hasAEnchantment(item, this) && item.getType() != Material.ENCHANTED_BOOK;
	}
	
	/**
	 * Returns true if the item has the AEnchantment, the item type is not an ENCHANTED_BOOK, and the Player has permission
	 * to use the AEnchantment.
	 * 
	 * @param player
	 * @param item
	 * @return
	 */
	public boolean canUsePermission(Player player, ItemStack item){
		return hasAEnchantment(item, this) && hasPermission(player) && item.getType() != Material.ENCHANTED_BOOK;
	}
	
	/**
	 * Returns true if the item has the AEnchantment, the item type is not an ENCHANTED_BOOK, the Player has permission
	 * to use the AEnchantment, and the AEnchantment is on a safe item.
	 * 
	 * @param player
	 * @param item
	 * @return
	 */
	public boolean canUseAllowedItem(Player player, ItemStack item){
		return hasAEnchantment(item, this) && hasPermission(player) &&  AEnchantment.containsSafeMaterial(getSafeMaterials(), item.getType()) && item.getType() != Material.ENCHANTED_BOOK;
	}
	
	/**
	 * Returns true if the item has the AEnchantment, the item type is not an ENCHANTED_BOOK, the Player has permission
	 * to use the AEnchantment, the AEnchantment is on a safe item, and the AEnchantment level is a safe level.
	 * 
	 * @param player
	 * @param item
	 * @return
	 */
	public boolean canUseSafe(Player player, ItemStack item){
		return hasAEnchantment(item, this) && hasPermission(player) &&  this.isSafeAEnchantment(item, AEnchantment.getAEnchantmentLevel(item, this)) && item.getType() != Material.ENCHANTED_BOOK;
	}
	
	@Override
	public String toString() {
		return "AEnchantment [globalID=" + globalID + ", localID=" + localID + ", STORAGE_NAME=" + STORAGE_NAME
				+ ", displayName=" + displayName + ", safeMaterials=" + Arrays.toString(safeMaterials) + ", safeLevel="
				+ safeLevel + ", conflicts=" + conflicts + ", permission=" + permission + ", baseEffectOdds="
				+ baseEffectOdds + ", enchantmentTableWeight=" + enchantmentTableWeight + ", host=" + host.getName() + "]";
	}
	
	/**
	 * Checks to see if the given Material is on the Material[] list. Usually used inside the API to check and see if a material
	 * is on an AEnchantment's safe materials list.
	 * 
	 * @param list
	 * @param given
	 * @return
	 */
	public static boolean containsSafeMaterial(Material[] list, Material given){
		for(Material safe : list){
			if(safe.equals(given)){
				return true;
			}
		}
		return false;
	}
	
	//Used for sorting the AEnchantments in the registered list based on size of enchantment table weight.
	@Override
	public int compareTo(AEnchantment enchantment) {
		if(enchantmentTableWeight == enchantment.getEnchantmentTableWeight()){
			 return 0;
		}
       return enchantmentTableWeight < enchantment.getEnchantmentTableWeight() ? -1 : 1;
	}

	//Static methods start here.

	/**
	 * Returns a list of all registered AEnchantment storage names.
	 * 
	 * @return
	 */
	public static List<String> storageNames(){
		List<String> names = new ArrayList<>();
		for(AEnchantment ad : registeredAEnchantments){
			names.add(ad.getSTORAGE_NAME());
		}
		return names;
	}

	
	/**
	 * Returns true if the given String is equal to the display name of one of the registered AEnchantments.
	 * 
	 * @param displayName
	 * @return
	 */
	public static boolean containsDisplayName(String displayName) {
	  for(AEnchantment ad : registeredAEnchantments){
		  if(ad.getDisplayName().equals(displayName)){
			  return true;
			}
		  }
	  return false;
	}
	
	/**
	 * Returns an AEnchantment object based on the AEnchantment's global ID String;
	 * 
	 * @param globalID
	 * @return
	 */
	public static AEnchantment fromGlobalID(String globalID){
		  for(AEnchantment ad : registeredAEnchantments){
			  if(ad.getGlobalID().equals(globalID)){
				  return ad;
				}
			  }
		  return null;
		 }

	/**
	 * Returns an AEnchantment object based on the AEnchantment's STORAGE_NAME String;
	 * 
	 * @param STORAGE_NAME
	 * @return
	 */
	public static AEnchantment fromSTORAGE_NAME(String STORAGE_NAME){
	  for(AEnchantment ad : registeredAEnchantments){
		  if(ad.getSTORAGE_NAME().equals(STORAGE_NAME)){
			  return ad;
			}
		  }
	  return null;
	  }

	/**
	 * Returns true if the given item has any AEnchantments.
	 * 
	 * @param item
	 * @return
	 */
	public static boolean isAEnchanted(ItemStack item){
		for(AEnchantment ench : AEnchantment.registeredAEnchantments){
			if(hasAEnchantment(item, ench)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the AEnchantment level of a given AEnchantment on a given ItemStack
	 * 
	 * @param item
	 * @param aenchantment
	 * @return
	 */
	public static int getAEnchantmentLevel(ItemStack item, AEnchantment aenchantment){
		ItemMeta im = item.getItemMeta();
		List<String> lore = im.getLore();
		for(String loreLine : lore){
			String[] words = loreLine.split(" ");
			if(loreLine.endsWith(AEnchantmentUtils.hideLoreData(aenchantment.getGlobalID())) && AEnchantmentUtils.isIntValue(words[words.length - 2]) && aenchantment.getGlobalID().equals(AEnchantmentUtils.retriveLoreData(words[words.length - 1]))){
				return AEnchantmentUtils.translateIntValue(words[words.length - 2]);
			}
		}
		return -1;
	}

	/**
	 * Returns true if the given ItemStack has the given AEnchantment
	 * 
	 * @param item
	 * @param aenchantment
	 * @return
	 */
	public static boolean hasAEnchantment(ItemStack item, AEnchantment aenchantment){
		if(item == null || item.getType() == Material.AIR){
			return false;
		}
		ItemMeta im = item.getItemMeta();
		List<String> lore = im.getLore();
		if(lore == null){
			return false;
		}
		for(String loreLine : lore){
			String[] words = loreLine.split(" ");
			if(loreLine.endsWith(AEnchantmentUtils.hideLoreData(aenchantment.getGlobalID())) && AEnchantmentUtils.isIntValue(words[words.length - 2]) && aenchantment.getGlobalID().equals(AEnchantmentUtils.retriveLoreData(words[words.length - 1]))){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the lore line index of a given AEnchantment on a given ItemStack.
	 * 
	 * @param item
	 * @param aenchantment
	 * @return
	 */
	public static int getLoreLine(ItemStack item, AEnchantment aenchantment){
		if(item == null || item.getType() == Material.AIR){
			return -1;
		}
		ItemMeta im = item.getItemMeta();
		List<String> lore = im.getLore();
		if(lore == null){
			return -1;
		}
		int it = 0;
		for(String loreLine : lore){
			String[] words = loreLine.split(" ");
			if(loreLine.endsWith(AEnchantmentUtils.hideLoreData(aenchantment.getGlobalID())) && AEnchantmentUtils.isIntValue(words[words.length - 2]) && aenchantment.getGlobalID().equals(AEnchantmentUtils.retriveLoreData(words[words.length - 1]))){
				return it;
			}
			it++;
		}
		return -1;
	}

	/**
	 * Tries to remove a given AEnchantment from a given ItemStack;
	 * 
	 * @param aenchanter
	 * @param item
	 * @param aenchantment
	 * @throws AEnchantmentInvalidAEnchantmentException
	 */
	public static void removeAEnchantment(AEnchanter aenchanter, ItemStack item, AEnchantment aenchantment) throws AEnchantmentInvalidAEnchantmentException{
		
		AEnchantmentDisenchantEvent event = new AEnchantmentDisenchantEvent(aenchanter, true, item, aenchantment);
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		if(event.isCancelled()){
			return;
		}
		
		if(!registeredAEnchantments.contains(aenchantment)){
			throw new AEnchantmentInvalidAEnchantmentException("Invalid AEnchantment!");
		}
		if(AEnchantment.hasAEnchantment(item, aenchantment)){
			ItemMeta im = item.getItemMeta();
			List<String> lore = im.getLore();
			if(lore == null){
				lore = new ArrayList<String>();
			}
			int it = 0;
			for(String loreLine : lore){
				String[] words = loreLine.split(" ");
				
				if(loreLine.endsWith(AEnchantmentUtils.hideLoreData(aenchantment.getGlobalID())) && aenchantment.getGlobalID().equals(AEnchantmentUtils.retriveLoreData(words[words.length - 1]))){
					lore.remove(it);
					break;
				}
				it++;
			}
			im.setLore(lore);
			item.setItemMeta(im);
		}
		
	}

	/**
	 * Tries to add the given AEnchantment unsafely to a given ItemStack. It will ignore the safe level and safe Material list. 
	 * 
	 * @param aenchanter
	 * @param item
	 * @param aenchantment
	 * @param level
	 * @throws AEnchantmentInvalidAEnchantmentException
	 * @throws AEnchantmentLevelException
	 */
	public static void addUnsafeAEnchantment(AEnchanter aenchanter, ItemStack item, AEnchantment aenchantment, int level) throws AEnchantmentInvalidAEnchantmentException, AEnchantmentLevelException{
		
		AEnchantmentEnchantEvent event = new AEnchantmentEnchantEvent(aenchanter, false, item, aenchantment, level);
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		if(event.isCancelled()){
			return;
		}
		
		if(!registeredAEnchantments.contains(aenchantment)){
			throw new AEnchantmentInvalidAEnchantmentException("Invalid AEnchantment!");
		}
		if(level <= 0){
			throw new AEnchantmentLevelException("Enchantment level must be greater than 0!");
		}
	
		if(AEnchantment.hasAEnchantment(item, aenchantment)){
			ItemMeta im = item.getItemMeta();
			List<String> lore = im.getLore();
			if(lore == null){
				lore = new ArrayList<String>();
			}
			int it = 0;
			for(String loreLine : lore){
				String[] words = loreLine.split(" ");
				if(loreLine.endsWith(AEnchantmentUtils.hideLoreData(aenchantment.getGlobalID())) && AEnchantmentUtils.isIntValue(words[words.length - 2]) && aenchantment.getGlobalID().equals(AEnchantmentUtils.retriveLoreData(words[words.length - 1]))){
					lore.remove(it);
					break;
				}
				it++;
			}
			lore.add(aenchantment.getDisplayName() + " " + AEnchantmentUtils.romanNumeralTranslator(level) + " " + AEnchantmentUtils.hideLoreData(aenchantment.getGlobalID()));
			im.setLore(lore);
			item.setItemMeta(im);
		}else{
			ItemMeta im = item.getItemMeta();
			List<String> lore = im.getLore();
			if(lore == null){
				lore = new ArrayList<String>();
			}
			lore.add(aenchantment.getDisplayName() + " " + AEnchantmentUtils.romanNumeralTranslator(level) + " " + AEnchantmentUtils.hideLoreData(aenchantment.getGlobalID()));
			im.setLore(lore);
			item.setItemMeta(im);
		}
		Bukkit.getServer().getPluginManager().callEvent(new AEnchantmentEnchantEvent(aenchanter, true, item, aenchantment, level));
	}

	/**
	 * Tries to add the given AEnchantment safely to a given ItemStack. It will not ignore the safe level or safe Material list. 
	 * 
	 * @param aenchanter
	 * @param item
	 * @param aenchantment
	 * @param level
	 * @throws AEnchantmentLevelException
	 * @throws AEnchantmentInvalidAEnchantmentException
	 */
	public static void addAEnchantment(AEnchanter aenchanter, ItemStack item, AEnchantment aenchantment, int level) throws AEnchantmentLevelException, AEnchantmentInvalidAEnchantmentException{
		
		AEnchantmentEnchantEvent event = new AEnchantmentEnchantEvent(aenchanter, true, item, aenchantment, level);
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		if(event.isCancelled()){
			return;
		}
		
		if(!AEnchantment.registeredAEnchantments.contains(aenchantment)){
			throw new AEnchantmentInvalidAEnchantmentException("Invalid AEnchantment!");
		}
		if(level <= 0){
			throw new AEnchantmentLevelException("Enchantment level must be greater than 0!");
		}
		
		if(!aenchantment.isSafeAEnchantment(item, level)){
			return;
		}
		if(AEnchantment.hasAEnchantment(item, aenchantment)){
			ItemMeta im = item.getItemMeta();
			List<String> lore = im.getLore();
			if(lore == null){
				lore = new ArrayList<String>();
			}
			int it = 0;
			for(String loreLine : lore){
				String[] words = loreLine.split(" ");
				if(loreLine.endsWith(AEnchantmentUtils.hideLoreData(aenchantment.getGlobalID())) && AEnchantmentUtils.isIntValue(words[words.length - 2]) && aenchantment.getGlobalID().equals(AEnchantmentUtils.retriveLoreData(words[words.length - 1]))){
					lore.remove(it);
					break;
				}
				it++;
			}
			lore.add(aenchantment.getDisplayName() + " " + AEnchantmentUtils.romanNumeralTranslator(level) + " " + AEnchantmentUtils.hideLoreData(aenchantment.getGlobalID()));
			im.setLore(lore);
			item.setItemMeta(im);
		}else{
			ItemMeta im = item.getItemMeta();
			List<String> lore = im.getLore();
			if(lore == null){
				lore = new ArrayList<String>();
			}
			lore.add(aenchantment.getDisplayName() + " " + AEnchantmentUtils.romanNumeralTranslator(level) + " " + AEnchantmentUtils.hideLoreData(aenchantment.getGlobalID()));
			im.setLore(lore);
			item.setItemMeta(im);
		}
	}

	/**
	 * Adds your AEnchantmentPlugin to the hooks list.
	 * 
	 * @param instance
	 */
	private static void registerHook(AEnchantmentPlugin instance){
		AEnchantmentConfigData.getHooks().add(instance.getName());
	}
	
	/**
	 * Returns a list of all registered AEnchantment global IDs.
	 * 
	 * @return
	 */
	public static List<String> globalIDs(){
		List<String> globalIDList = new ArrayList<>();
		for(AEnchantment a : registeredAEnchantments){
			globalIDList.add(a.getGlobalID());
		}
		return globalIDList;
	}
	
	/**
	 * Returns a list of all registered AEnchantments STORAGE_NAMEs and globalIDs.
	 * 
	 * @return
	 */
	public static List<String> getStorageNamesAndIDs(){
		List<String> snai = new ArrayList<>();
		for(AEnchantment a : registeredAEnchantments){
			snai.add(a.getSTORAGE_NAME() + " (ID: " + a.getGlobalID() + ")");
		}
		return snai;
	}
	
	/**
	 * Registers all of AEnchantments in the given AEnchantmentPlugin with the API.
	 * 
	 * @param instance
	 * @throws AEnchantmentInvalidGlobalIDException
	 * @throws AEnchantmentNameException
	 */
	public static void registerAEnchantments(AEnchantmentPlugin instance) throws AEnchantmentInvalidGlobalIDException, AEnchantmentNameException{
		
		if(AEnchantmentConfigData.getHooks().contains(instance.getName())){
			throw new AEnchantmentNameException("You cannot load a plugin with the same name as one that has already been loaded!");
		}
		
		AEnchantment.registerHook(instance);
		int it = 0;
		for(AEnchantment data : instance.getLocalAEnchantments()){
			
			if(instance.isGenerateGlobalIDs()){
				data.setGlobalID(instance.getName() + "." + it);
			}
			
			if(data.getLocalID() == null || data.getLocalID() == ""){
				throw new AEnchantmentInvalidGlobalIDException("You cannot have a global ID that is null or an empty String!");
			}
			
			if((data.getLocalID().contains(",") || data.getLocalID().contains(".") || data.getLocalID().contains(":") || data.getLocalID().contains(" "))){
				throw new AEnchantmentInvalidGlobalIDException("You cannot have a global ID that has a \",\", \".\", \" \", or \":\" in the name!");
			}
			
			if(globalIDs().contains(data.getGlobalID())){
				throw new AEnchantmentInvalidGlobalIDException("You cannot have duplicate global IDs!");
			}
			
			if(data.getSTORAGE_NAME().contains(",") || data.getSTORAGE_NAME().contains(".") || data.getSTORAGE_NAME().contains(":")){
				throw new AEnchantmentNameException("You cannot load an AEnchantment that has a \",\", \".\", or \":\" in the STORAGE_NAME!");
			}
			
			if(instance.getName().contains(",") || instance.getName().contains(".") || instance.getName().contains(":") || instance.getName().contains(" ")){
				throw new AEnchantmentNameException("You cannot load an AEnchantmentPlugin that has a \",\", \".\", \" \", or \":\" in the name!");
			}
			
		
			data.resolveDuplicateStorageNames();
			AEnchantment.registeredAEnchantments.add(data);
			it++;
		}
		AllosEnchantsMain.instance.getLogger().info(instance.getName() + " has been hooked in!");
	}
	
	/**
	 * View a list of all the registered AEnchantments.
	 * 
	 * @return
	 */
	public static List<AEnchantment> viewRegisteredAEnchantments(){
		return Collections.unmodifiableList(registeredAEnchantments);
	}
}
