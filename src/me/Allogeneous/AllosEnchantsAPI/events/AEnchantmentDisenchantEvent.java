package me.Allogeneous.AllosEnchantsAPI.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import me.Allogeneous.AllosEnchantsAPI.core.AEnchantment;
import me.Allogeneous.AllosEnchantsAPI.objects.AEnchanter;

public class AEnchantmentDisenchantEvent extends Event implements Cancellable{
	private static final HandlerList handlers = new HandlerList();
	
    private ItemStack item;
    private AEnchantment AEnchantment;
    private boolean cancelled;
    private AEnchanter enchanter;

    /**
     * Called whenever an ItemSack has an AEnchantment removed from it.
     * 
     * @param aenchanter
     * @param unsafe
     * @param item
     * @param aenchantment
     */
    public  AEnchantmentDisenchantEvent(AEnchanter aenchanter, boolean unsafe, ItemStack item, AEnchantment aenchantment) {
        this.item = item;
        this.AEnchantment = aenchantment;
        this.setEnchanter(aenchanter);
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
    
	public ItemStack getItem() {
		return item;
	}

	public AEnchantment getAEnchantment() {
		return AEnchantment;
	}

	public AEnchanter getEnchanter() {
		return enchanter;
	}

	public void setEnchanter(AEnchanter enchanter) {
		this.enchanter = enchanter;
	}
}
