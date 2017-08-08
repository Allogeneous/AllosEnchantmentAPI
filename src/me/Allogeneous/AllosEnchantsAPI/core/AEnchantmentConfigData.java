package me.Allogeneous.AllosEnchantsAPI.core;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Material;

//All config data is here stored in memory here.

public class AEnchantmentConfigData {
	
	private static Set<String> hooks = new TreeSet<>();
	public static final String NO_PERMISSION = "aenchants.caninteract.all";
	private static boolean loadedEnableAnvilShiftClick;
	private static boolean loadedEnchantmentTableShiftClick;
	private static boolean loadedCanEnchantItemsWithNoEnchantmentUsePermission;
	private static boolean no0Weights;
	private static int loadedtMaxSafeEnchantmentsOnItem;
	private static Material anvilBlock;
	private static Material anvilBorder;
	private static Material anvilActivator;
	private static Material anvilCostIndicator;
	private static Material anvilDecorationItem1;
	private static Material anvilDecorationItem2;
	private static Material enchantmentTableBlock;
	private static Material enchantmentTableBorder;
	private static Material enchantmentTableActivator;
	private static Material enchantmentTableCostIndicator;
	private static Material enchantmentTableDecorationItem1;
	private static Material enchantmentTableDecorationItem2;
	private static boolean alternateColorCodes;
	private static char colorCode;

	
	public static boolean isLoadedEnableAnvilShiftClick() {
		return loadedEnableAnvilShiftClick;
	}
	protected static void setLoadedEnableAnvilShiftClick(boolean loadedEnableAnvilShiftClick) {
		AEnchantmentConfigData.loadedEnableAnvilShiftClick = loadedEnableAnvilShiftClick;
	}
	public static boolean isLoadedEnchantmentTableShiftClick() {
		return loadedEnchantmentTableShiftClick;
	}
	protected static void setLoadedEnchantmentTableShiftClick(boolean loadedEnchantmentTableShiftClick) {
		AEnchantmentConfigData.loadedEnchantmentTableShiftClick = loadedEnchantmentTableShiftClick;
	}
	public static boolean isLoadedCanEnchantItemsWithNoEnchantmentUsePermission() {
		return loadedCanEnchantItemsWithNoEnchantmentUsePermission;
	}
	protected static void setLoadedCanEnchantItemsWithNoEnchantmentUsePermission(
			boolean loadedCanEnchantItemsWithNoEnchantmentUsePermission) {
		AEnchantmentConfigData.loadedCanEnchantItemsWithNoEnchantmentUsePermission = loadedCanEnchantItemsWithNoEnchantmentUsePermission;
	}
	public static int getLoadedtMaxSafeEnchantmentsOnItem() {
		return loadedtMaxSafeEnchantmentsOnItem;
	}
	protected static void setLoadedtMaxSafeEnchantmentsOnItem(int loadedtMaxSafeEnchantmentsOnItem) {
		AEnchantmentConfigData.loadedtMaxSafeEnchantmentsOnItem = loadedtMaxSafeEnchantmentsOnItem;
	}
	public static boolean isNo0Weights() {
		return no0Weights;
	}
	protected static void setNo0Weights(boolean no0Weights) {
		AEnchantmentConfigData.no0Weights = no0Weights;
	}
	public static Material getAnvilActivator() {
		return anvilActivator;
	}
	protected static void setAnvilActivator(Material anvilActivator) {
		AEnchantmentConfigData.anvilActivator = anvilActivator;
	}
	public static Material getAnvilCostIndicator() {
		return anvilCostIndicator;
	}
	protected static void setAnvilCostIndicator(Material anvilCostIndicator) {
		AEnchantmentConfigData.anvilCostIndicator = anvilCostIndicator;
	}
	public static Material getAnvilDecorationItem1() {
		return anvilDecorationItem1;
	}
	protected static void setAnvilDecorationItem1(Material anvilDecorationItem1) {
		AEnchantmentConfigData.anvilDecorationItem1 = anvilDecorationItem1;
	}
	public static Material getAnvilDecorationItem2() {
		return anvilDecorationItem2;
	}
	protected static void setAnvilDecorationItem2(Material anvilDecorationItem2) {
		AEnchantmentConfigData.anvilDecorationItem2 = anvilDecorationItem2;
	}
	public static Material getEnchantmentTableBlock() {
		return enchantmentTableBlock;
	}
	protected static void setEnchantmentTableBlock(Material enchantmentTableBlock) {
		AEnchantmentConfigData.enchantmentTableBlock = enchantmentTableBlock;
	}
	public static Material getEnchantmentTableBorder() {
		return enchantmentTableBorder;
	}
	protected static void setEnchantmentTableBorder(Material enchantmentTableBorder) {
		AEnchantmentConfigData.enchantmentTableBorder = enchantmentTableBorder;
	}
	public static Material getEnchantmentTableActivator() {
		return enchantmentTableActivator;
	}
	protected static void setEnchantmentTableActivator(Material enchantmentTableActivator) {
		AEnchantmentConfigData.enchantmentTableActivator = enchantmentTableActivator;
	}
	public static Material getEnchantmentTableCostIndicator() {
		return enchantmentTableCostIndicator;
	}
	protected static void setEnchantmentTableCostIndicator(Material enchantmentTableCostIndicator) {
		AEnchantmentConfigData.enchantmentTableCostIndicator = enchantmentTableCostIndicator;
	}
	public static Material getEnchantmentTableDecorationItem1() {
		return enchantmentTableDecorationItem1;
	}
	protected static void setEnchantmentTableDecorationItem1(Material enchantmentTableDecorationItem1) {
		AEnchantmentConfigData.enchantmentTableDecorationItem1 = enchantmentTableDecorationItem1;
	}
	public static Material getEnchantmentTableDecorationItem2() {
		return enchantmentTableDecorationItem2;
	}
	protected static void setEnchantmentTableDecorationItem2(Material enchantmentTableDecorationItem2) {
		AEnchantmentConfigData.enchantmentTableDecorationItem2 = enchantmentTableDecorationItem2;
	}
	public static Material getAnvilBlock() {
		return anvilBlock;
	}
	protected static void setAnvilBlock(Material anvilBlock) {
		AEnchantmentConfigData.anvilBlock = anvilBlock;
	}
	public static Material getAnvilBorder() {
		return anvilBorder;
	}
	protected static void setAnvilBorder(Material anvilBorder) {
		AEnchantmentConfigData.anvilBorder = anvilBorder;
	}
	public static boolean isAlternateColorCodes() {
		return alternateColorCodes;
	}
	protected static void setAlternateColorCodes(boolean alternateColorCodes) {
		AEnchantmentConfigData.alternateColorCodes = alternateColorCodes;
	}
	public static char getColorCode() {
		return colorCode;
	}
	protected static void setColorCode(char colorCode) {
		AEnchantmentConfigData.colorCode = colorCode;
	}
	public static Set<String> viewHooks() {
		return Collections.unmodifiableSet(hooks);
	}
	protected static Set<String> getHooks() {
		return hooks;
	}
	protected static void setHooks(Set<String> hooks) {
		AEnchantmentConfigData.hooks = hooks;
	}
}
