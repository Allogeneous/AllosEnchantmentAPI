package me.Allogeneous.AllosEnchantsAPI.objects;

public class AEnchanter {
	
	private Object aenchanter;
	
	/**
	 * Basically just a fancy wrapper for an Object, anything can be an AEnchanter.
	 * 
	 * @param enchanter
	 */
	public AEnchanter(Object aenchanter){
		this.setAEnchanter(aenchanter);
	}

	public Object getAEnchanter() {
		return aenchanter;
	}

	public void setAEnchanter(Object aenchanter) {
		this.aenchanter = aenchanter;
	}
	
	@Override
	public String toString() {
		return "AEnchanter [aenchanter=" + aenchanter + "]";
	}

}
