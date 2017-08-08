package me.Allogeneous.AllosEnchantsAPI.objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.Allogeneous.AllosEnchantsAPI.core.AEnchantment;
import me.Allogeneous.AllosEnchantsAPI.exceptions.AEnchantmentInvalidAEnchantmentException;
import me.Allogeneous.AllosEnchantsAPI.exceptions.AEnchantmentLevelException;

public class AEnchantmentBook {
	
	private ItemStack book;
	
	/**
	 * Book object for making custom aenchanted books. To add more AEnchantments to a book, and another unsafe enchantment to the ItemStack.
	 * 
	 * @param aenchanter
	 * @param aenchantment
	 * @param level
	 * @throws AEnchantmentInvalidAEnchantmentException
	 * @throws AEnchantmentLevelException
	 */
	public AEnchantmentBook(AEnchanter aenchanter, AEnchantment aenchantment, int level) throws AEnchantmentInvalidAEnchantmentException, AEnchantmentLevelException{
		book = new ItemStack(Material.ENCHANTED_BOOK);
		AEnchantment.addUnsafeAEnchantment(aenchanter, book, aenchantment, level);
	}
	
	public ItemStack getBook(){
		return book;
	}
	
	public void setBook(ItemStack book){
		this.book = book;
	}
	
	@Override
	public String toString() {
		return "AEnchantmentBook [book=" + book.getType() + "]";
	}
	
}
