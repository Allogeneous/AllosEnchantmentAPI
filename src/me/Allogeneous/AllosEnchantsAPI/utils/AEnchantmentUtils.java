package me.Allogeneous.AllosEnchantsAPI.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import me.Allogeneous.AllosEnchantsAPI.core.AEnchantment;


public class AEnchantmentUtils{
	
	/**
	 * Returns the amount of AEnchantments on an ItemStack.
	 * 
	 * @param item
	 * @return
	 */
	public static int getAEnchantmentsAmount(ItemStack item){
		int amount = 0;
		for(AEnchantment ae : AEnchantment.viewRegisteredAEnchantments()){
			if(AEnchantment.hasAEnchantment(item, ae)){
				amount++;
			}
		}
		return amount;
	}
	
	/**
	 * Returns a list of AEnchantments on an ItemStack.
	 * 
	 * @param item
	 * @return
	 */
	public static List<AEnchantment> getAppliedAEnchantments(ItemStack item){
		List<AEnchantment> aenchants = new ArrayList<>();
		for(AEnchantment ae : AEnchantment.viewRegisteredAEnchantments()){
			if(AEnchantment.hasAEnchantment(item, ae)){
				aenchants.add(ae);
			}
		}
		return aenchants;
	}
	
	/**
	 * Simple 1 to 10 Roman Numeral converter.
	 * 
	 * @param level
	 * @return
	 */
	public static String romanNumeralTranslator(int level){
		switch(level){
			case 1: 
				return "I";
			case 2:
				return "II";
			case 3: 
				return "III";
			case 4:
				return "IV";
			case 5:
				return "V";
			case 6:
				return "VI";
			case 7: 
				return "VII";
			case 8:
				return "VIII";
			case 9: 
				return "IX";
			case 10: 
				return "X";
			default:
				return "aenchantment.level." + level;
			
		}
	}
	
	
	/**
	 * Checks to see if the Roman Numeral can be translated back to an Integer.
	 * 
	 * @param level
	 * @return
	 */
	public static boolean isIntValue(String level){
		switch(level){
		case "I": 
			return true;
		case "II":
			return true;
		case "III": 
			return true;
		case "IV":
			return true;
		case "V":
			return true;
		case "VI":
			return true;
		case "VII": 
			return true;
		case "VIII":
			return true;
		case "IX": 
			return true;
		case "X": 
			return true;
		default:
		}
		
		if(level.contains("aenchantment.level.")){
			String[] lvl = level.split("\\.");
			try{
				Integer.parseInt(lvl[2]);
				return true;
			}catch(Exception e){
				return false;
			}
		}
		
		return false;
	}
	
	/**
	 * Translates Roman Numeral back to an Integer.
	 * 
	 * @param level
	 * @return
	 */
	public static int translateIntValue(String level){
		switch(level){
		case "I": 
			return 1;
		case "II":
			return 2;
		case "III": 
			return 3;
		case "IV":
			return 4;
		case "V":
			return 5;
		case "VI":
			return 6;
		case "VII": 
			return 7;
		case "VIII":
			return 8;
		case "IX": 
			return 9;
		case "X": 
			return 10;
		default:
		}
		
		if(level.contains("aenchantment.level.")){
			String[] lvl = level.split("\\.");
			return Integer.parseInt(lvl[2]);
		}
		
		return -1;
	}
	
	/**
	 * Checks to see if the String can be parsed to an Integer.
	 * 
	 * @param inte
	 * @return
	 */
	public static boolean isInt(String inte){
		try{
			Integer.parseInt(inte);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}
	
	/**
	 * Used to parse an Integer.
	 * 
	 * @param inte
	 * @return
	 */
	public static int parseInt(String inte){
		return Integer.parseInt(inte); 
	}
	
	/**
	 * Returns invisible lore line Strings.
	 * 
	 * @param data
	 * @return
	 */
	public static String hideLoreData(String data) {
        StringBuilder hidden = new StringBuilder("");
        for (char c : data.toCharArray()) {
        	hidden.append(ChatColor.COLOR_CHAR + "" + c);
        }
        return hidden.toString();
    }
	
	/**
	 * Returns String from invisible lore line data.
	 * 
	 * @param data
	 * @return
	 */
	public static String retriveLoreData(String data) {
        return data.replace("§", "");
    }
	
	/**
	 * Basic way to calculate odds with a base change and a scale based on the level.
	 * 
	 * @param level
	 * @param chanceMult
	 * @return
	 */
	public static boolean calculateOdds(int level, float chanceMult){
		float odds = (float)level * chanceMult;
		if(odds >= 1){
			return true;
		}
		
		Random r = new Random();
		float rf = r.nextFloat();
		if(rf <= odds){
			return true;
		}
		return false;
		
	}	
}
