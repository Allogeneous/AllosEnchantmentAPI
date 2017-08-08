package me.Allogeneous.AllosEnchantsAPI.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import me.Allogeneous.AllosEnchantsAPI.core.AEnchantment;
import me.Allogeneous.AllosEnchantsAPI.objects.AEnchanter;

public class AEnchantmentEnchantEvent extends Event implements Cancellable{
	
	private static final HandlerList handlers = new HandlerList();
    private boolean unsafe;
    private ItemStack item;
    private AEnchantment AEnchantment;
    private int level;
    private AEnchanter enchanter;
    private boolean cancelled;
    
    /**
     * Called whenever an ItemSack has an AEnchantment added to it.
     * 
     * @param aenchanter
     * @param unsafe
     * @param item
     * @param aenchantment
     */
    public AEnchantmentEnchantEvent(AEnchanter aenchanter, boolean unsafe, ItemStack item, AEnchantment aenchantment, int level) {
        this.unsafe = unsafe;
        this.item = item;
        this.AEnchantment = aenchantment;
        this.setEnchanter(aenchanter);
        this.level = level;
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled;
    }
     
    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
	public boolean isUnsafe() {
		return unsafe;
	}


	public ItemStack getItem() {
		return item;
	}

	public AEnchantment getAEnchantment() {
		return AEnchantment;
	}


	public int getLevel() {
		return level;
	}

	public AEnchanter getEnchanter() {
		return enchanter;
	}

	public void setEnchanter(AEnchanter enchanter) {
		this.enchanter = enchanter;
	}

}
